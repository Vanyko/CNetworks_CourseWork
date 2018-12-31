import java.util.*;

/**
 * Created by Vanyko on 11/15/18.
 */
public class Node implements Comparable<Node>{
    private int id;
    private LinkedHashMap<Node, Canal> neighbours;
    private RoutesTable routesTable;
    private ArrayList<PacketsQueueEntity> inputPacketsQueue;
    private ArrayList<PacketsQueueEntity> outputPacketsQueue;
    private ArrayList<PacketsQueueEntity> notDeliveredPackets;
    private HashSet<Integer> deliveredPackets;
    int currentTime;

    private static final int TIME_TO_WAIT = 3500;

    public Node(int id) {
        this.id = id;
        this.neighbours = new LinkedHashMap<>();
        this.routesTable = new RoutesTable(id);
        this.inputPacketsQueue = new ArrayList<>();
        this.outputPacketsQueue = new ArrayList<>();
        this.notDeliveredPackets = new ArrayList<>();
        this.deliveredPackets = new HashSet<>();
        this.currentTime = 0;
    }

//    public Node(Node node) {
//        this.id = node.id;
//        this.neighbours = node.neighbours;
//        this.routesTable = node.routesTable;
//        this.inputPacketsQueue = node.inputPacketsQueue;
//        this.outputPacketsQueue = node.outputPacketsQueue;
//        this.notDeliveredPackets = node.inputPacketsQueue;
//        this.deliveredPackets = node.deliveredPackets;
//    }

    public int getId() {
        return id;
    }

    public Map<Node, Canal> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(HashMap<Node, Canal> neighbours) {
        this.neighbours.clear();
        this.neighbours.putAll(neighbours);
    }

    public void addNeighbour(Node node, Canal canal){
        this.neighbours.put(node, canal);
    }

    public int getPower(){
        return neighbours.size();
    }

    @Override
    public int compareTo(Node o) {
        return this.getId() - o.getId();
    }

    public RoutesTable getRoutesTable() {
        return routesTable;
    }

    public Node getNeighbourById(int id){
        Object[] neighboursArray = neighbours.keySet().toArray();
        for (int i = 0; i < neighbours.size(); i++){
            Node node = (Node) neighboursArray[i];
            if (node.getId() == id){
                return node;
            }
        }
//        for (Node nodeAddr : neighbours.keySet()){
//            if (nodeAddr.getId() == id){
//                return nodeAddr;
//            }
//        }
        throw new IndexOutOfBoundsException("There is no such neighbour " + String.valueOf(id));
    }

    @Override
    public String toString() {
        String result =  "\nNode{" + "id=" + id + ",neighbours=";
        for (Node node : neighbours.keySet()){
            result += "nodeId=";
            result += node.getId();
            result += ", canalId=";
            result += neighbours.get(node).getId();
            result += ",";
        }
        result += "RoutesTable{" + routesTable + "}" +
                ", inputPacketsQueue=" + inputPacketsQueue +
                ", outputPacketsQueue=" + outputPacketsQueue +
                ", notDeliveredPackets=" + notDeliveredPackets;
        result += '}';

        return result;
    }

    public void sendAck(int srcAddr, boolean ack, int nodesCounter){

    }

    public boolean addPacket(Packet packet, int prevAddr){
        // If all is OK, return true. If error occur, return false.
        inputPacketsQueue.add(new PacketsQueueEntity(packet, prevAddr, currentTime));
        return true;
    }

    public void removeInputEntities(ArrayList<PacketsQueueEntity> entitiesToRemove){
        for (PacketsQueueEntity entity : entitiesToRemove){
            inputPacketsQueue.remove(entity);
        }
    }

    public void removeOutputEntities(ArrayList<PacketsQueueEntity> entitiesToRemove){
        for (PacketsQueueEntity entity : entitiesToRemove){
            outputPacketsQueue.remove(entity);
            if (!entity.getPacket().isService()) {
                notDeliveredPackets.add(entity);
            }
        }
    }

    public boolean notDeliveredPacketsContains(PacketsQueueEntity entity){
        for (PacketsQueueEntity e : notDeliveredPackets){
            if (Math.abs(e.getPacket().getId()) == Math.abs(entity.getPacket().getId())){
                return true;
            }
        }
        return false;
    }

    public boolean alreadyIn(PacketsQueueEntity entity){
//        for (int i : deliveredPackets){
//            if (Math.abs(i) == Math.abs(entity.getPacket().getId())){
//                return true;
//            }
//        }
//        return false;
        return deliveredPackets.stream()
                .anyMatch(i -> Math.abs(i) == Math.abs(entity.getPacket().getId()));
    }

    public PacketsQueueEntity getNotDeliveredPacket(PacketsQueueEntity entity){
        for (PacketsQueueEntity e : notDeliveredPackets){
            if ((Math.abs(e.getPacket().getId()) == Math.abs(entity.getPacket().getId())) && (e.getNodeAddr() == entity.getNodeAddr())){
                return e;
            }
        }
        return null;
    }

    public void generatePacket(PacketsQueueEntity entity){
        Packet packet = new Packet(entity.getPacket());
//        notDeliveredPackets.add(entity);
        int nextAddr = routesTable.getNextAddrByRoute(packet.getSrcAddr(), packet.getDstAddr());
        if (nextAddr != -1){
            outputPacketsQueue.add(new PacketsQueueEntity(packet, nextAddr, currentTime));
            inputPacketsQueue.remove(entity);
        } else {
            // manageInputPackets not found, or server not respond.
            // manageOutputPackets to all neighbours
            routesTable.addPacket(entity.getPacket(), entity.getNodeAddr());
            Object[] neighboursArray = neighbours.keySet().toArray();
            int prevNodeAddr = entity.getNodeAddr();
            for (int i = 0; i < neighbours.size(); i++){
                Node node = (Node) neighboursArray[i];
                if (node.getId() != prevNodeAddr){
                    outputPacketsQueue.add(new PacketsQueueEntity(packet, node.getId(), currentTime));
                    inputPacketsQueue.remove(entity);
                }
            }
        }
    }

    public void manageServicePacket(PacketsQueueEntity entity){
        if (entity.getPacket().isACK()){
            PacketsQueueEntity entityToRemove = getNotDeliveredPacket(entity);
            if (entityToRemove != null) {
                // packet successfully delivered to the next node
                notDeliveredPackets.remove(entityToRemove);

//                // Correct routes table
//                routesTable.correctRoute(entity);
            }
        } else if (entity.getPacket().isNACK()){
//            outputPacketsQueue.add(new PacketsQueueEntity(entity));
            PacketsQueueEntity notDelivered = getNotDeliveredPacket(entity);
            entity.setTime(currentTime);
            outputPacketsQueue.add(notDelivered);
            notDeliveredPackets.remove(notDelivered);
        } else if (entity.getPacket().getAnswer() == Answer.REWRITEROUTE){
            routesTable.correctRoute(entity);
            int prevAddr = routesTable.getPrevAddrByRoute(entity.getPacket().getSrcAddr(), entity.getPacket().getDstAddr());
            if ((prevAddr != this.id) && (prevAddr != -1)){
                outputPacketsQueue.add(new PacketsQueueEntity(new Packet(entity.getPacket(), Answer.REWRITEROUTE), prevAddr, currentTime));
            }
        }
    }

    public void manageDeliveredPacket(PacketsQueueEntity entity){
//        outputPacketsQueue.add(new PacketsQueueEntity(new Packet(entity.getPacket(), Answer.ACK), entity.getNodeAddr()));  // Send ACK to prev node
//        System.out.printf("Packet %s delivered!\n", entity.getPacket());
        // Add it to routes table
//                        // TODO: check next line
//                        routesTable.addPacket(packet, entity.getNodeAddr());
//                    }
    }

    public void sendToAllNeighbours(PacketsQueueEntity entity){
        Object[] neighboursArray = neighbours.keySet().toArray();
        int prevNodeAddr = entity.getNodeAddr();
        for (int i = 0; i < neighbours.size(); i++) {
            Node node = (Node) neighboursArray[i];
            if (node.getId() != prevNodeAddr) {
                outputPacketsQueue.add(new PacketsQueueEntity(entity.getPacket(), node.getId(), currentTime));
//                                    inputEntitiesToRemove.add(entity);
            }
        }
    }

    public void route(PacketsQueueEntity entity){
        Packet packet = entity.getPacket();
//        packet.incCounter();
//        // Send ACK to prev node
//        outputPacketsQueue.add(new PacketsQueueEntity(new Packet(packet, Answer.ACK), entity.getNodeAddr()));
        // Add it to routes table. If it's already consists, don't do anything.
//        if (alreadyIn(entity)) {
//            // Correct routes table
////                    // TODO: manage prev node (correct table in prev node)
//            routesTable.correctRoute(entity);
//        } else {
//
//
////                        // TODO: check next line
//            routesTable.addPacket(packet, entity.getNodeAddr());
            // route packet
//                        Packet packet = entity.getPacket();
            int nextAddr = routesTable.getNextAddrByRoute(packet.getSrcAddr(), packet.getDstAddr());
            if (nextAddr != -1) {
                outputPacketsQueue.add(new PacketsQueueEntity(packet, nextAddr, currentTime));
                //                    inputPacketsQueue.remove(entity);
//                            inputEntitiesToRemove.add(entity);
            } else {
                // route not found, or server not respond.
                // send to all neighbours
                sendToAllNeighbours(entity);
            }
//        }

        deliveredPackets.add(packet.getId());
    }

    public void manageCorrectRoutePacket(PacketsQueueEntity entity){
        if (entity.getPacket().getCounter() < routesTable.getCounter(entity)) {
            Packet correctRoutePacket = new Packet(entity.getPacket(), Answer.REWRITEROUTE);
            outputPacketsQueue.add(new PacketsQueueEntity(correctRoutePacket, entity.getNodeAddr(), currentTime));
        } else if (routesTable.getCounter(entity) <= 0){
            routesTable.addPacket(new Packet(entity.getPacket()), entity.getNodeAddr());
            Packet correctRoutePacket = new Packet(entity.getPacket(), Answer.REWRITEROUTE);
            outputPacketsQueue.add(new PacketsQueueEntity(correctRoutePacket, entity.getNodeAddr(), currentTime));
        }
    }

    public boolean manageACK(PacketsQueueEntity entity){
        Packet packet = entity.getPacket();
        if (packet.isError()){
            outputPacketsQueue.add(new PacketsQueueEntity(new Packet(packet, Answer.NACK), entity.getNodeAddr(),currentTime));
            return false;
        } else {
            outputPacketsQueue.add(new PacketsQueueEntity(new Packet(packet, Answer.ACK), entity.getNodeAddr(), currentTime));
        }
        return true;
    }

    // Manages inputPacketsQueue.
    // Decides where to transfer a packet.
    // Return list of packets to remove in inputPacketsQueue.
    public ArrayList<PacketsQueueEntity> manageInputPackets(){
        ArrayList<PacketsQueueEntity> inputEntitiesToRemove = new ArrayList<>();
        for (PacketsQueueEntity entity : inputPacketsQueue){  // process input packets & manageInputPackets it
//            if (neighbours.get(getNeighbourById(entity.getNodeAddr())).isFree(this.id - entity.getNodeAddr())) {
//                System.out.printf("Received from %d in %d : %s\n", entity.getNodeAddr(), this.id, entity.getPacket().toString());
                if (entity.getPacket().isService()) {  // its a service packet. Acknowledgment or correct manageInputPackets.
                    manageServicePacket(entity);
                } else {
                    Packet packet = entity.getPacket();
                    packet.incCounter();
                    // Send ACK to prev node
                    if (manageACK(entity)) {
//                        outputPacketsQueue.add(new PacketsQueueEntity(new Packet(packet, Answer.ACK), entity.getNodeAddr()));
//                Packet packet = new Packet(entity.getPacket());
                        if (alreadyIn(entity)) {
                            // if counter less, correct prev node table
//                        int oldCounter = routesTable.getCounter(entity);
//                        int newCounter = entity.getPacket().getCounter();
//                        if (newCounter > oldCounter) {
//                            // send CorrectRoute to prev node
////                            manageCorrectRoutePacket(entity);
//                        }
                        } else {
                            if (packet.getDstAddr() == this.id) {
                                manageDeliveredPacket(entity);
                                manageCorrectRoutePacket(entity);
                            } else {
                                route(entity);
                            }
                            routesTable.addPacket(new Packet(entity.getPacket()), entity.getNodeAddr());
                        }
                    }
                }
                inputEntitiesToRemove.add(entity);
//            }

//            // TODO: manage this, prev node (correct table in prev node)
//            int nextAddr = routesTable.getNextAddrByDst(entity.getPacket().getDstAddr());
//            if (nextAddr == -1){
//                routesTable.addPacket(entity.getPacket(), entity.getNodeAddr());
//            } else {
//                routesTable.correctRoute(entity);
//            }
        }
        return inputEntitiesToRemove;
    }

    // Manages outputPacketsQueue.
    // Try to transfer a packets.
    // Return list of packets to remove in outputPacketsQueue.
    public ArrayList<PacketsQueueEntity> manageOutputPackets(){
        // TODO: check if packet already was in there
        // TODO: check if server response OK
        ArrayList<PacketsQueueEntity> outputEntitiesToRemove = new ArrayList<>();
        for (PacketsQueueEntity entity : outputPacketsQueue){  // transfer packet to next nodeAddr
            try {
                Node neighbour = getNeighbourById(entity.getNodeAddr());
                Canal canal = neighbours.get(neighbour);
                if (canal.isFree(this.id - entity.getNodeAddr())) {
                    Packet packet = new Packet(entity.getPacket());
    //                neighbour.addPacket(packet, this.id);
                    canal.setPacket(packet, this.id - entity.getNodeAddr());
                    outputEntitiesToRemove.add(entity);
    //                System.out.printf("Send from %d to %d : %s\n", this.id, neighbour.getId(), packet.toString());
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return outputEntitiesToRemove;
    }

    public ArrayList<PacketsQueueEntity> manageNotDeliveredPackets(int currentTime){
        ArrayList<PacketsQueueEntity> entitiesToRemove = new ArrayList<>();
        for (int i = 0; i < notDeliveredPackets.size(); i++){
            PacketsQueueEntity entity = notDeliveredPackets.get(i);
            if (entity.getTime() + TIME_TO_WAIT < currentTime){
                entity.setTime(currentTime);
                outputPacketsQueue.add(entity);
                entitiesToRemove.add(new PacketsQueueEntity(entity));
                System.err.printf("Not delivered yet:%s\n", entity.toString());
            }
        }
        return entitiesToRemove;
    }

    public void removeNotDeliveredPackets(ArrayList<PacketsQueueEntity> entitiesToRemove){
        for (PacketsQueueEntity entity : entitiesToRemove){
            notDeliveredPackets.remove(entity);
        }
    }

    public boolean tick(int cTime){
        boolean res = false;
        this.currentTime = cTime;

        for (Node neighbour : neighbours.keySet()) {
            Canal canal = neighbours.get(neighbour);
            Packet packet = canal.getPacket(this.id - neighbour.getId());
            if (packet != null) {
                inputPacketsQueue.add(new PacketsQueueEntity(packet, neighbour.getId(), currentTime));
            }
        }

        ArrayList<PacketsQueueEntity> inEntities = manageInputPackets();

        ArrayList<PacketsQueueEntity> notDeliveredPackets = manageNotDeliveredPackets(currentTime);
        removeNotDeliveredPackets(notDeliveredPackets);

        ArrayList<PacketsQueueEntity> outEntities = manageOutputPackets();

        if ((outEntities.size() > 0) || (inEntities.size() > 0) || (notDeliveredPackets.size() > 0)){
            res = true;
        }

        removeOutputEntities(outEntities);
        removeInputEntities(inEntities);

        return res;
    }
}
