import GUI.GUI;

import javax.swing.*;

/**
 * Created by Vanyko on 11/15/18.
 */
public class Main {
    public static void main(String[] args) {
        Network network = new Network();

//        network.generateAllNodes(30);
//        network.generateConstantNetwork(30);
        network.generateConstantNetwork();

        for (Node node : network.getNetwork()) {
            System.out.println(node.toString());
        }

        System.out.printf("Network power = %f\n", network.getPower());

//        network.generatePacketsOnAllNodes(0);

        int time = 0;

        for (int i = 0; i < network.getNetwork().size(); i++){
            for (int j = 0; j < network.getNetwork().size(); j++){
                if (i != j){
                    network.generatePacket(i, j, time);
                    boolean flag = true;
                    while (flag){
                        flag = network.tick(time);
                        time++;
                    }
                }
            }
        }



        System.out.println(network.toString());

        network.printAllShortestRoutes();

        System.out.printf("time = %d\n", time);

//        network.generatePacket(0, 3, time);
        network.generateMessages(0, 3, time, 10, 3, TransferType.DATAGRAM);
        boolean flag = true;
        while (flag){
            flag = network.tick(time);
            time++;
        }
//
        System.out.printf("time = %d\n", time);

        // TODO: duplex is not working fine
    }
}
