public class VirtualChanelTableEntity extends LogicLinkTableEntity {
    int nextAddr;

    public VirtualChanelTableEntity(int id, int time, Status status, int dstAddr, int nextAddr) {
        super(id, time, status, dstAddr);
        this.nextAddr = nextAddr;
    }
}
