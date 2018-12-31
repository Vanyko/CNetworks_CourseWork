import java.util.Random;

public class Packet {
    private int srcAddr, dstAddr, counter; // counter = nodes counter
    private int data;
    private boolean error;
    private static int nextId = 1;
    private int id;
    private int size;
    private Answer answer;
    private TransferType transferType;

    public Packet(int srcAddr, int dstAddr, int data) {
        this.srcAddr = srcAddr;
        this.dstAddr = dstAddr;
        this.counter = 0;
        this.error = false;
        this.data = data;
        this.size = data;
        this.id = getNextId();
        this.answer = Answer.DATA;
        this.transferType = TransferType.DATAGRAM;
    }

    public Packet(int srcAddr, int dstAddr, int data, TransferType transferType) {
        this.srcAddr = srcAddr;
        this.dstAddr = dstAddr;
        this.counter = 0;
        this.error = false;
        this.data = data;
        this.size = data;
        this.id = getNextId();
        this.answer = Answer.DATA;
        this.transferType = transferType;
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
        this.answer = Answer.DATA;
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
        this.answer = Answer.DATA;
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
        this.answer = packet.getAnswer();
        this.transferType = packet.getTransferType();
    }

    public Packet(Packet packet, Answer answer){
        this.srcAddr = packet.getSrcAddr();
        this.dstAddr = packet.getDstAddr();
        this.counter = packet.getCounter();
        this.error = packet.isError();
        this.data = packet.getData();
        this.size = packet.getSize();
        this.id = packet.reverseId();
        this.answer = answer;
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


    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

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
        return answer != Answer.DATA;
    }

    public boolean isACK(){
        return answer == Answer.ACK;
    }

    public boolean isNACK(){
        return answer == Answer.NACK;
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
                ", answer=" + answer +
                '}';
    }
}
