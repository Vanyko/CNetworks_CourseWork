package main;

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
    private ArrayList<PacketsQueueEntity> delayedPackets;
    private ArrayList<LogicLinkTableEntity> logicLinksTable;
    private ArrayList<VirtualChanelTableEntity> virtualChanelsTable;
    private HashSet<Integer> deliveredPackets;
    private int currentTime;

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
        this.delayedPackets = new ArrayList<>();
        this.logicLinksTable = new ArrayList<>();
        this.virtualChanelsTable = new ArrayList<>();
    }

//    public main.Node(main.Node node) {
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
//        for (main.Node nodeAddr : neighbours.keySet()){
//            if (nodeAddr.getId() == id){
//                return nodeAddr;
//            }
//        }
        throw new IndexOutOfBoundsException("There is no such neighbour " + String.valueOf(id));
    }

    @Override
    public String toString() {
        String result =  "\nmain.Node{" + "id=" + id + ",neighbours=";
        for (Node node : neighbours.keySet()){
            result += "nodeId=";
            result += node.getId();
            result += ", canalId=";
            result += neighbours.get(node).getId();
            result += ",";
        }
        result += "main.RoutesTable{" + routesTable + "}" +
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

        if (entity.getPacket().getTransferType() == TransferType.LOGIC_CONNECTION) {
            if (entity.getPacket().getStatus() == Status.CONNECTION){
                // TODO: check if there is already no such entity
                logicLinksTable.add(new LogicLinkTableEntity(entity.getPacket().getLinkId(), currentTime, Status.CONNECTION, entity.getPacket().getDstAddr()));
            } else if (entity.getPacket().getStatus() == Status.DATA){

            } else if (entity.getPacket().getStatus() == Status.DISCONNECTION){
                LogicLinkTableEntity logicLink = getLogicLinkEntityById(entity.getPacket().getLinkId());
                if (logicLink != null){
                    logicLink.setStatus(Status.DISCONNECTION);
                }
            }
        }
    }

    public LogicLinkTableEntity getLogicLinkEntityById(int id){
        for (int i = 0; i < logicLinksTable.size(); i++){
            LogicLinkTableEntity buf = logicLinksTable.get(i);
            if (buf.getId() == id){
                return buf;
            }
        }
        return null;
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
//            outputPacketsQueue.add(new main.PacketsQueueEntity(entity));
            PacketsQueueEntity notDelivered = getNotDeliveredPacket(entity);
            entity.setTime(currentTime);
            outputPacketsQueue.add(notDelivered);
            notDeliveredPackets.remove(notDelivered);
        } else if (entity.getPacket().getStatus() == Status.REWRITEROUTE){
            routesTable.correctRoute(entity);
            int prevAddr = routesTable.getPrevAddrByRoute(entity.getPacket().getSrcAddr(), entity.getPacket().getDstAddr());
            if ((prevAddr != this.id) && (prevAddr != -1)){
                outputPacketsQueue.add(new PacketsQueueEntity(new Packet(entity.getPacket(), Status.REWRITEROUTE), prevAddr, currentTime));
            }
        } else if (entity.getPacket().getTransferType() == TransferType.LOGIC_CONNECTION){
            if (entity.getPacket().getStatus() == Status.CONNECTION) {
                if (entity.getPacket().getDstAddr() == this.id) {
                    // decide of make a connection
                    int srcAddr = entity.getPacket().getSrcAddr();
                    outputPacketsQueue.add(new PacketsQueueEntity(
                            new Packet(this.id, srcAddr, 1, 0,TransferType.LOGIC_CONNECTION, Status.CONNECTION_ACK), entity.getNodeAddr(), currentTime));
                } else {
                    route(entity);
                }
            } else if (entity.getPacket().getStatus() == Status.CONNECTION_ACK) {
                if (entity.getPacket().getDstAddr() == this.id) {
                    // connection successful
                    System.out.println("connection successful");
                    LogicLinkTableEntity logicLink = getLogicLinkEntityById(entity.getPacket().getLinkId());
                    if (logicLink != null){
                        logicLink.setStatus(Status.DATA);
                    }
                } else {
                    route(entity);
                }
            } else if (entity.getPacket().getStatus() == Status.DISCONNECTION) {
                if (entity.getPacket().getDstAddr() == this.id) {
                    // decide of make a disconnection
                    int srcAddr = entity.getPacket().getSrcAddr();
                    outputPacketsQueue.add(new PacketsQueueEntity(
                            new Packet(this.id, srcAddr, 1, 0, TransferType.LOGIC_CONNECTION, Status.DISCONNECTION_ACK), entity.getNodeAddr(), currentTime));
                } else {
                    route(entity);
                }
            } else if (entity.getPacket().getStatus() == Status.DISCONNECTION_ACK) {
                if (entity.getPacket().getDstAddr() == this.id) {
                    // connection successful
                    System.out.println("disconnection successful");
                    LogicLinkTableEntity logicLink = getLogicLinkEntityById(entity.getPacket().getLinkId());
                    if (logicLink != null){
                        logicLinksTable.remove(logicLink);
                    }
                } else {
                    route(entity);
                }
            }
        }
    }

    public void manageDeliveredPacket(PacketsQueueEntity entity){
//        outputPacketsQueue.add(new main.PacketsQueueEntity(new main.Packet(entity.getPacket(), main.Status.ACK), entity.getNodeAddr()));  // Send ACK to prev node
//        System.out.printf("main.Packet %s delivered!\n", entity.getPacket());
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
            }
        }
    }

    public Status getStatusByLogicLinkId(int id){
        for (LogicLinkTableEntity entity : logicLinksTable){
            if (entity.getId() == id){
                return entity.getStatus();
            }
        }
        return null;
    }

    public void route(PacketsQueueEntity entity){
        Packet packet = entity.getPacket();
        if ((packet.getTransferType() == TransferType.DATAGRAM) || (packet.getTransferType() == TransferType.LOGIC_CONNECTION)) {
            // route packet
            int nextAddr = routesTable.getNextAddrByRoute(packet.getSrcAddr(), packet.getDstAddr());
            if (nextAddr != -1) {
                outputPacketsQueue.add(new PacketsQueueEntity(packet, nextAddr, currentTime));
            } else {
                // route not found, or server not respond.
                // send to all neighbours
                sendToAllNeighbours(entity);
            }
        }
//        else if (packet.getTransferType() == main.TransferType.LOGIC_CONNECTION){
//            // if logic conn is in connection mode, then delay packet, else generate connection
////            if ((getStatusByLogicLinkId() != null) && (getStatusByLogicLinkId() == main.Status.CONNECTION)){
////
////            }
//        }

        deliveredPackets.add(packet.getId());
    }

    public void manageCorrectRoutePacket(PacketsQueueEntity entity){
        if (entity.getPacket().getCounter() < routesTable.getCounter(entity)) {
            Packet correctRoutePacket = new Packet(entity.getPacket(), Status.REWRITEROUTE);
            outputPacketsQueue.add(new PacketsQueueEntity(correctRoutePacket, entity.getNodeAddr(), currentTime));
        } else if (routesTable.getCounter(entity) <= 0){
            routesTable.addPacket(new Packet(entity.getPacket()), entity.getNodeAddr());
            Packet correctRoutePacket = new Packet(entity.getPacket(), Status.REWRITEROUTE);
            outputPacketsQueue.add(new PacketsQueueEntity(correctRoutePacket, entity.getNodeAddr(), currentTime));
        }
    }

    public boolean manageACK(PacketsQueueEntity entity){
        Packet packet = entity.getPacket();
        if (packet.isError()){
            outputPacketsQueue.add(new PacketsQueueEntity(new Packet(packet, Status.NACK), entity.getNodeAddr(),currentTime));
            return false;
        } else {
            outputPacketsQueue.add(new PacketsQueueEntity(new Packet(packet, Status.ACK), entity.getNodeAddr(), currentTime));
        }
        return true;
    }

    // Manages inputPacketsQueue.
    // Decides where to transfer a packet.
    // Return list of packets to remove in inputPacketsQueue.
    public ArrayList<PacketsQueueEntity> manageInputPackets(){
        ArrayList<PacketsQueueEntity> inputEntitiesToRemove = new ArrayList<>();
        for (PacketsQueueEntity entity : inputPacketsQueue){  // process input packets & manageInputPackets it
                if (entity.getPacket().isService()) {  // its a service packet. Acknowledgment or correct manageInputPackets.
                    manageServicePacket(entity);
                } else {
                    Packet packet = entity.getPacket();
                    packet.incCounter();
                    // Send ACK to prev node
                    if (manageACK(entity)) {
                        if (alreadyIn(entity)) {
                            // TODO: if new route is shorter, rewrite old route
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
                    canal.setPacket(packet, this.id - entity.getNodeAddr());
                    outputEntitiesToRemove.add(entity);
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
