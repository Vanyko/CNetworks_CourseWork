public class PacketsQueueEntity implements Comparable<PacketsQueueEntity>{
    private Packet packet;
    private int nodeAddr;
    private int time;

    public PacketsQueueEntity(Packet packet, int nodeAddr, int time) {
        this.packet = packet;
        this.nodeAddr = nodeAddr;
        this.time = time;
    }

//    public PacketsQueueEntity(Packet packet, int nodeAddr) {
//        this.packet = packet;
//        this.nodeAddr = nodeAddr;
//        this.time = 0;
//    }

    public PacketsQueueEntity(PacketsQueueEntity entity) {
        this.packet = entity.getPacket();
        this.nodeAddr = entity.getNodeAddr();
        this.time = entity.getTime();
    }

    // TODO: return new Packet(packet) -> return packet;
    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public int getNodeAddr() {
        return nodeAddr;
    }

    public void setNodeAddr(int nodeAddr) {
        this.nodeAddr = nodeAddr;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "PacketsQueueEntity{" +
                "packet=" + packet +
                ", nodeAddr=" + nodeAddr +
                ", time=" + time +
                '}';
    }

    @Override
    public int compareTo(PacketsQueueEntity o) {
        return Math.abs(this.getPacket().getId()) - Math.abs(o.getPacket().getId());
    }
}
