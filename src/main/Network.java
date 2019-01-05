package main;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Vanyko on 11/15/18.
 */
public class Network {
    private ArrayList<Node> network;
    private ArrayList<Canal> canals;

    private ArrayList<Integer> possibleWeights = new ArrayList<>();
    double errorProbability = 0.000001;
    private static int nextLinkId = 0;

    public Network() {
        this.network = new ArrayList<>();
        this.canals = new ArrayList<>();

        possibleWeights.add(1);
        possibleWeights.add(2);
        possibleWeights.add(5);
        possibleWeights.add(6);
        possibleWeights.add(8);
        possibleWeights.add(9);
        possibleWeights.add(10);
        possibleWeights.add(12);
        possibleWeights.add(15);
        possibleWeights.add(18);
        possibleWeights.add(21);
        possibleWeights.add(25);
    }

    public ArrayList<Node> getNetwork() {
        return network;
    }

    public void generateAllNodes(int size){
        for (int i = 0; i < size; i++){
            network.add(new Node(i));
        }

        generateCanals();
    }

    public void generateCanals(){
        Random random = new Random();
        int power = 2;
        int size = network.size();
        for (int i = 0; i < size; i++){
            Node current_node = this.network.get(i);
            for(int j = current_node.getNeighbours().size() - 1; j < power; j++){
                int next_node_id = random.nextInt(size);
                while (current_node.getNeighbours().containsKey(new Node(next_node_id)) || (next_node_id == current_node.getId()) ) {
                    next_node_id = random.nextInt(size);
                }
                Node node = network.get(next_node_id);
                Canal canal = new Canal();
//                canal.setWeight(possibleWeights.get(random.nextInt(possibleWeights.size())));
                canal.setWeight(1);
                canal.setErrorProbability(canal.getWeight() * errorProbability);
                current_node.addNeighbour(node, canal);
                node.addNeighbour(current_node, canal);
                canals.add(canal);
            }
        }
    }

    public void generateConstantNetwork(int size){
        for (int i = 0; i < size; i++){
            network.add(new Node(i));
        }

        for (Node node : network){
            for (Node neighbour : network){
                if ((neighbour != node) && (!node.getNeighbours().containsKey(neighbour))){
                    Canal canal = new Canal();
                    canals.add(canal);
                    node.addNeighbour(neighbour, canal);
                    neighbour.addNeighbour(node, canal);
                }
            }
        }
    }

    public void generateConstantNetwork(){
        for (int i = 0; i < 4; i++){
            network.add(new Node(i));
        }

//        for (main.Node node : network){
//            for (main.Node neighbour : network){
//                if ((neighbour != node) && (!node.getNeighbours().containsKey(neighbour))){
//                    main.Canal canal = new main.Canal();
//                    canals.add(canal);
//                    node.addNeighbour(neighbour, canal);
//                    neighbour.addNeighbour(node, canal);
//                }
//            }
//        }

        Node node0 = network.get(0);
        Node node1 = network.get(1);
        Node node2 = network.get(2);
        Node node3 = network.get(3);

        Canal canal0 = new Canal();
        canals.add(canal0);
        node0.addNeighbour(node1, canal0);
        node1.addNeighbour(node0, canal0);

        Canal canal1 = new Canal();
        canals.add(canal1);
        node0.addNeighbour(node2, canal1);
        node2.addNeighbour(node0, canal1);

        Canal canal2 = new Canal();
        canals.add(canal2);
        node1.addNeighbour(node2, canal2);
        node2.addNeighbour(node1, canal2);

        Canal canal3 = new Canal();
        canals.add(canal3);
        node2.addNeighbour(node3, canal3);
        node3.addNeighbour(node2, canal3);
    }

    public double getPower(){
        double power = 0;
        for (Node node : network){
            power += node.getPower();
        }
        return power / network.size();
    }

    public Node getNode(int nodeId){
        if (nodeId >= network.size() || nodeId < 0){
            throw new ArrayIndexOutOfBoundsException("There is no such nodeAddr!");
        }
        return network.get(nodeId);
    }

    public void generatePacketsOnAllNodes(int time){
        for (int i = 0; i < network.size(); i++){
//            generatePacket(i);
            for (int j = 0; j < network.size(); j++){
                if (j == i){
                    continue;
                }
                generatePacket(i, j, time);
            }
        }
    }

    public void generatePacket(int srcAddr){
        int dstAddr = 0;
        Random random = new Random();
        while (dstAddr == srcAddr){
            dstAddr = random.nextInt(network.size());
        }
        Packet packet = new Packet(srcAddr, dstAddr);
        System.out.println(packet);
    }

    public void generateMessages(int nodeId, int dstAddr, int time, int msgSize, int serviceSize, int infSize, TransferType transferType){
        while (msgSize != 0){
            int iSize = 0;
            if (msgSize - infSize <= 0){
                iSize = msgSize;
            } else {
                iSize = infSize;
            }
            msgSize -= iSize;
            Packet packet = new Packet(nodeId, dstAddr, serviceSize, iSize, transferType);
            generatePacket(nodeId, time, packet);
        }
    }

    public void generatePacket(int srcAddr, int dstAddr, int time){
        Packet packet = new Packet(srcAddr, dstAddr);
        getNode(srcAddr).generatePacket(new PacketsQueueEntity(packet, srcAddr, time));
    }

    public void generatePacket(int srcAddr, int time, Packet p){
        Packet packet = new Packet(p);
        getNode(srcAddr).generatePacket(new PacketsQueueEntity(packet, srcAddr, time));
    }

    public void generatePacket(int srcAddr, int dstAddr, int time, int serviceSize, int infSize, TransferType transferType, Status status, int linkId){
        Packet packet = new Packet(srcAddr, dstAddr, serviceSize, infSize, transferType, status, linkId);
        getNode(srcAddr).generatePacket(new PacketsQueueEntity(packet, srcAddr, time));
    }

    public void generateLogicLinkConnection(int srcAddr, int dstAddr, int time, int linkId, int servceSize){
        Packet packet = new Packet(srcAddr, dstAddr, servceSize, 0, TransferType.LOGIC_CONNECTION, Status.CONNECTION, linkId);
        getNode(srcAddr).generatePacket(new PacketsQueueEntity(packet, srcAddr, time));
    }

    public void closeLogicLinkConnection(int srcAddr, int dstAddr, int time, int linkId, int servceSize){
        int servicePacketSize = 1;
        Packet packet = new Packet(srcAddr, dstAddr, servceSize, 0, TransferType.LOGIC_CONNECTION, Status.DISCONNECTION, linkId);
        getNode(srcAddr).generatePacket(new PacketsQueueEntity(packet, srcAddr, time));
    }

    public void printAllShortestRoutes(){
        for (Node node : network){
            printShortestRoutesFromNode(node);
        }
    }

    public void printShortestRoutesFromNode(Node node){
        for (int i = 0; i < node.getRoutesTable().getTable().size(); i++){
//        for (main.RoutesTable.Entity entity : nodeAddr.getRoutesTable().getTable()){
            Node buf = node;
            RoutesTable.Entity entity = buf.getRoutesTable().getTable().get(i);
            int helper = 0;
            if (entity.getSrcAddr() == buf.getId()){
                // print all manageInputPackets
                int srcAddr = buf.getId();
                int dstAddr = entity.getDstAddr();
                int nextAddr = entity.getNextAddr();
                while(buf.getId() != dstAddr){
                    System.out.printf("%d->", buf.getId());
                    if (nextAddr == -1){
                        System.out.println("no route available!");
                        System.err.println("no route available!");
                        break;
                    }
                    buf = network.get(nextAddr);
                    nextAddr = buf.getRoutesTable().getNextAddrByRoute(srcAddr, dstAddr);
                    helper++;
                    if (helper > 35){
                        break;
                    }
                }
                System.out.printf("%d!\n", buf.getId());
            }
        }

//        for (int i = 0; i < node.getRoutesTable().getTable().size(); i++){
//            for (main.RoutesTable.Entity entity : node.getRoutesTable().getTable()){
//                main.Node buf = node;
//
//
//            }
//        }
    }

    public boolean tick(int currentTime){ // returns true if needs one more tick()
        boolean result = false;
        for (Canal canal : canals){
            if (canal.tick()){
                result = true;
            }
        }

        for (Node node : network){
            if (node.tick(currentTime)){
                result = true;
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return "main.Network{" +
                "network=" + network +
                '}';
    }


}
