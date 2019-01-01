import java.util.Random;

public class Packet {
    private int srcAddr, dstAddr, counter; // counter = nodes counter
    private int data;
    private boolean error;
    private static int nextId = 1;
    private int id;
    private int size;
    private Status status;
    private TransferType transferType;
    private int linkId; // logic link id

    public Packet(int srcAddr, int dstAddr, int data) {
        this.srcAddr = srcAddr;
        this.dstAddr = dstAddr;
        this.counter = 0;
        this.error = false;
        this.data = data;
        this.size = data;
        this.id = getNextId();
        this.status = Status.DATA;
        this.transferType = TransferType.DATAGRAM;
        this.linkId = -1;
    }

    public Packet(int srcAddr, int dstAddr, int data, TransferType transferType) {
        this.srcAddr = srcAddr;
        this.dstAddr = dstAddr;
        this.counter = 0;
        this.error = false;
        this.data = data;
        this.size = data;
        this.id = getNextId();
        this.status = Status.DATA;
        this.transferType = transferType;
        this.linkId = -1;
    }

    public int getLinkId() {
        return linkId;
    }

    public void setLinkId(int linkId) {
        this.linkId = linkId;
    }

    public Packet(int srcAddr, int dstAddr, int data, TransferType transferType, Status status) {
        this.srcAddr = srcAddr;
        this.dstAddr = dstAddr;
        this.counter = 0;
        this.error = false;
        this.data = data;
        this.size = data;
        this.id = getNextId();
        this.status = status;
        this.transferType = transferType;
        this.linkId = -1;
    }

    public Packet(int srcAddr, int dstAddr, int data, TransferType transferType, Status status, int linkId) {
        this.srcAddr = srcAddr;
        this.dstAddr = dstAddr;
        this.counter = 0;
        this.error = false;
        this.data = data;
        this.size = data;
        this.id = getNextId();
        this.status = status;
        this.transferType = transferType;
        this.linkId = linkId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Packet(int srcAddr, int dstAddr) {
        this.srcAddr = srcAddr;
        this.dstAddr = dstAddr;
        this.counter = 0;
        this.error = false;

        Random random = new Random();
//        this.data = (random.nextInt(10) + 1) * 10;
        this.data = 1;

        this.size = data;
        this.id = getNextId();
        this.status = Status.DATA;
        this.transferType = TransferType.DATAGRAM;
    }

    public Packet(int srcAddr, int dstAddr, int counter, int data) {
        this.srcAddr = srcAddr;
        this.dstAddr = dstAddr;
        this.counter = counter;
        this.data = data;
        this.error = false;
        this.size = data;
        this.id = getNextId();
        this.status = Status.DATA;
        this.transferType = TransferType.DATAGRAM;
    }

    public Packet(Packet packet){
        this.srcAddr = packet.getSrcAddr();
        this.dstAddr = packet.getDstAddr();
        this.counter = packet.getCounter();
        this.error = packet.isError();
        this.data = packet.getData();
        this.size = packet.getSize();
        this.id = packet.getId();
        this.status = packet.getStatus();
        this.transferType = packet.getTransferType();
    }

    public Packet(Packet packet, Status STATUS){
        this.srcAddr = packet.getSrcAddr();
        this.dstAddr = packet.getDstAddr();
        this.counter = packet.getCounter();
        this.error = packet.isError();
        this.data = packet.getData();
        this.size = packet.getSize();
        this.id = packet.reverseId();
        this.status = STATUS;
        this.transferType = packet.getTransferType();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSrcAddr() {
        return srcAddr;
    }

//    public void setIdSource(int srcAddr) {
//        this.srcAddr = srcAddr;
//    }

    public int getDstAddr() {
        return dstAddr;
    }

//    public void setIdReceiver(int dstAddr) {
//        this.dstAddr = dstAddr;
//    }


    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public Packet incCounter(){
        ++this.counter;
        return this;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public int getData() {
        return data;
    }

    public int getNextId(){
        return nextId++;
    }

    public int getId() {
        return id;
    }

    public int reverseId(){
        return -id;
    }

    public boolean isService(){
        return status != Status.DATA;
    }

    public boolean isACK(){
        return status == Status.ACK;
    }

    public boolean isNACK(){
        return status == Status.NACK;
    }

    public TransferType getTransferType() {
        return transferType;
    }

    public void setTransferType(TransferType transferType) {
        this.transferType = transferType;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "id = " + id +
                ", srcAddr=" + srcAddr +
                ", dstAddr=" + dstAddr +
                ", counter=" + counter +
                ", data=" + data +
                ", error=" + error +
                ", size=" + size +
                ", Status=" + status +
                '}';
    }
}
