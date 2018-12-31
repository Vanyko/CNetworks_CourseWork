import java.util.ArrayList;

public class RoutesTable {
    public class Entity{
        private int srcAddr, dstAddr, prevAddr, nextAddr, counter;

        public Entity(int srcAddr, int dstAddr, int prevAddr, int nextAddr, int counter) {
            this.srcAddr = srcAddr;
            this.dstAddr = dstAddr;
            this.prevAddr = prevAddr;
            this.nextAddr = nextAddr;
            this.counter = counter;
        }

        public Entity(Packet packet){
            this.srcAddr = packet.getSrcAddr();
            this.dstAddr = packet.getDstAddr();
            this.prevAddr = -1;
            this.nextAddr = -1;
            this.counter = packet.getCounter();
        }

        public Entity(Packet packet, int prevAddr){
            this.srcAddr = packet.getSrcAddr();
            this.dstAddr = packet.getDstAddr();
            this.prevAddr = prevAddr;
            this.nextAddr = -1;
            this.counter = packet.getCounter();
        }

        public Entity(Packet packet, int prevAddr, int nextAddr){
            this.srcAddr = packet.getSrcAddr();
            this.dstAddr = packet.getDstAddr();
            this.prevAddr = prevAddr;
            this.nextAddr = nextAddr;
            this.counter = packet.getCounter();
        }

        public int getSrcAddr() {
            return srcAddr;
        }

        public void setSrcAddr(int srcAddr) {
            this.srcAddr = srcAddr;
        }

        public int getDstAddr() {
            return dstAddr;
        }

        public void setDstAddr(int dstAddr) {
            this.dstAddr = dstAddr;
        }

        public int getPrevAddr() {
            return prevAddr;
        }

        public void setPrevAddr(int prevAddr) {
            this.prevAddr = prevAddr;
        }

        public int getNextAddr() {
            return nextAddr;
        }

        public void setNextAddr(int nextAddr) {
            this.nextAddr = nextAddr;
        }

        public int getCounter() {
            return counter;
        }

        public void setCounter(int counter) {
            this.counter = counter;
        }

        @Override
        public String toString() {
            return "Entity{" +
                    "srcAddr=" + srcAddr +
                    ", dstAddr=" + dstAddr +
                    ", prevAddr=" + prevAddr +
                    ", nextAddr=" + nextAddr +
                    ", counter=" + counter +
                    '}';
        }
    }

    private ArrayList<Entity> table;
    private int nodeAddr;

    public RoutesTable(int nodeAddr) {
        this.table = new ArrayList<>();
        this.nodeAddr = nodeAddr;
    }

    public void addPacket(Packet packet, int prevAddr){
        int srcAddr = packet.getSrcAddr();
        int dstAddr = packet.getDstAddr();
        for (int i = 0; i < table.size(); i++){
            Entity entity = table.get(i);
            if ((entity.getSrcAddr() == srcAddr) && (entity.getDstAddr() == dstAddr)){
                return;
            }
        }
        table.add(new Entity(packet, prevAddr));
    }

    public ArrayList<Entity> getTable() {
        return table;
    }

//    public int getPrevAddr(Packet packet){
//        for(Entity entity : table){
//            if (entity.getSrcAddr() == packet.getSrcAddr() && entity.getDstAddr() == packet.getDstAddr()){
//                return entity.getPrevAddr();
//            }
//        }
//        return -1;
//    }
//
//    public void correctRoute(Packet packet, boolean flag){
////        correctRoute(packet.getSrcAddr(), packet.getDstAddr(), nextAddr);
//        int srcAddr = packet.getSrcAddr();
//        int dstAddr = packet.getDstAddr();
//        int counter = packet.getCounter();
//        for (int i = 0; i < table.size(); i++){
//            Entity entity = table.get(i);
//            if ((entity.getSrcAddr() == srcAddr) && (entity.getDstAddr() == dstAddr)){
////                entity.setNextAddr(nextAddr);
//                if (flag) {
//                    entity.setCounter(counter);
//                }
//            }
////            if (entity.getDstAddr() == dstAddr){
////                table.get(i).setNextAddr(nextAddr);
////            }
//        }
//    }
//
//    public void correctCounter(Packet packet){
//        int srcAddr = packet.getSrcAddr();
//        int dstAddr = packet.getDstAddr();
//        int counter = packet.getCounter();
//        for (int i = 0; i < table.size(); i++){
//            Entity entity = table.get(i);
//            if ((entity.getSrcAddr() == srcAddr) && (entity.getDstAddr() == dstAddr)){
////                entity.setNextAddr(nextAddr);
//                entity.setCounter(counter);
//            }
////            if (entity.getDstAddr() == dstAddr){
////                table.get(i).setNextAddr(nextAddr);
////            }
//        }
//    }
//
//    public void correctRoute(Packet packet, int nextAddr, boolean flag){
////        correctRoute(packet.getSrcAddr(), packet.getDstAddr(), nextAddr);
//        int srcAddr = packet.getSrcAddr();
//        int dstAddr = packet.getDstAddr();
//        int counter = packet.getCounter();
//        for (int i = 0; i < table.size(); i++){
//            Entity entity = table.get(i);
//            if ((entity.getSrcAddr() == srcAddr) && (entity.getDstAddr() == dstAddr)){
//                entity.setNextAddr(nextAddr);
//                if (flag) {
//                    entity.setCounter(counter);
//                }
//            }
////            if (entity.getDstAddr() == dstAddr){
////                table.get(i).setNextAddr(nextAddr);
////            }
//        }
//    }
//
////    public void correctRoute(Packet packet, int prevAddr, int nextAddr, boolean flag){
//////        correctRoute(packet.getSrcAddr(), packet.getDstAddr(), nextAddr);
////        int srcAddr = packet.getSrcAddr();
////        int dstAddr = packet.getDstAddr();
////        int counter = packet.getCounter();
////        for (int i = 0; i < table.size(); i++){
////            Entity entity = table.get(i);
////            if ((entity.getSrcAddr() == srcAddr) && (entity.getDstAddr() == dstAddr)){
////                entity.setNextAddr(nextAddr);
////                entity.setPrevAddr(prevAddr);
////                if (flag) {
////                    entity.setCounter(counter);
////                }
////            }
//////            if (entity.getDstAddr() == dstAddr){
//////                table.get(i).setNextAddr(nextAddr);
//////            }
////        }
////    }
//
//    public void correctPrevAddr(Packet packet, int prevAddr){
//        int srcAddr = packet.getSrcAddr();
//        int dstAddr = packet.getDstAddr();
//        for (int i = 0; i < table.size(); i++){
//            Entity entity = table.get(i);
//            if ((entity.getSrcAddr() == srcAddr) && (entity.getDstAddr() == dstAddr)){
//                entity.setPrevAddr(prevAddr);
//            }
////            if (entity.getDstAddr() == dstAddr){
////                table.get(i).setNextAddr(nextAddr);
////            }
//        }
//    }
//
//    public void correctRoute(int srcAddr, int dstAddr, int nextAddr){
//        for (int i = 0; i < table.size(); i++){
//            Entity entity = table.get(i);
//            if ((entity.getSrcAddr() == srcAddr) && (entity.getDstAddr() == dstAddr)){
//                table.get(i).setNextAddr(nextAddr);
//            }
////            if (entity.getDstAddr() == dstAddr){
////                table.get(i).setNextAddr(nextAddr);
////            }
//        }
//    }
//
    public int getNextAddrByRoute(int src, int dst){
        for(Entity entity : table){
            if ((entity.getDstAddr() == dst) && (entity.getSrcAddr() == src)){
                return entity.getNextAddr();
            }
        }
        return -1;
    }

//    public int getNextAddrByDst(int dstAddr){
//        for(Entity entity : table){
//            if (entity.getDstAddr() == dstAddr){
//                return entity.getNextAddr();
//            }
//        }
//        return -1;
//    }
//
    public int getPrevAddrByRoute(int src, int dst){
        for(Entity entity : table){
            if ((entity.getDstAddr() == dst) && (entity.getSrcAddr() == src)){
                return entity.getPrevAddr();
            }
        }
        return -1;
    }

//    public boolean alreadyIn(Packet packet){
//        for(Entity entity : table){
//            if (entity.get() == dstAddr){
//                return entity.getNextAddr();
//            }
//        }
//        return -1;
//    }
//
////    public void setPacketDestination
//
//    // if rout already consists then return manageInputPackets counter, else -1
//    public int routeCount(Packet packet){
//        for (Entity entity : table){
//            if ((entity.getSrcAddr() == packet.getSrcAddr()) && (entity.getDstAddr() == packet.getDstAddr())){
//                return entity.getCounter();
//            }
////            if (entity.getDstAddr() == packet.getDstAddr()){
////                return entity.getCounter();
////            }
//        }
//        return -1;
//    }

    public void correctRoute(PacketsQueueEntity packetsQueueEntity) {
        Packet packet = packetsQueueEntity.getPacket();
        int srcAddr = packetsQueueEntity.getPacket().getSrcAddr();
        int dstAddr = packetsQueueEntity.getPacket().getDstAddr();
        int counter = packetsQueueEntity.getPacket().getCounter();
        int nextAddr = packetsQueueEntity.getNodeAddr();
        for (int i = 0; i < table.size(); i++){
            Entity entity = table.get(i);
            if ((entity.getSrcAddr() == srcAddr) && (entity.getDstAddr() == dstAddr)){
//                if ((entity.getCounter() > counter) || (entity.getNextAddr() == -1)) {
//                    System.out.printf("CorrectingRoute: (%s)\n", packetsQueueEntity.toString());
//                    System.out.println(this.toString());
                    entity.setNextAddr(nextAddr);
//                    entity.setCounter(counter);
//                    System.out.println(this.toString());
//                    System.out.flush();
                    return;
//                }
            }
        }
    }

    public int getCounter(PacketsQueueEntity packetsQueueEntity) {
        Packet packet = packetsQueueEntity.getPacket();
        int srcAddr = packetsQueueEntity.getPacket().getSrcAddr();
        int dstAddr = packetsQueueEntity.getPacket().getDstAddr();
        int counter = packetsQueueEntity.getPacket().getCounter();
        int nextAddr = packetsQueueEntity.getNodeAddr();
        for (int i = 0; i < table.size(); i++){
            Entity entity = table.get(i);
            if ((entity.getSrcAddr() == srcAddr) && (entity.getDstAddr() == dstAddr)){
                return entity.getCounter();
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return "RoutesTable{" +
                "nodeAddr=" + nodeAddr +
                ", table=" + table +
                '}';
    }
}
