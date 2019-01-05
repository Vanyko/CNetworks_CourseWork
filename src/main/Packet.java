package main;

import java.util.Random;

public class Packet {
    private int srcAddr, dstAddr, counter; // counter = nodes counter
    private int serviceLength;
    private int informationLength;
    private boolean error;
    private static int nextId = 1;
    private int id;
    private Status status;
    private TransferType transferType;
    private int linkId; // logic link id

    public Packet(int srcAddr, int dstAddr, int serviceLength, int informationLength) {
        this.srcAddr = srcAddr;
        this.dstAddr = dstAddr;
        this.counter = 0;
        this.error = false;
        this.serviceLength = serviceLength;
        this.informationLength = informationLength;
//        this.size = serviceLength + informationLength;
        this.id = getNextId();
        this.status = Status.DATA;
        this.transferType = TransferType.DATAGRAM;
        this.linkId = -1;
    }

    public Packet(int srcAddr, int dstAddr, int serviceLength, int informationLength, TransferType transferType) {
        this.srcAddr = srcAddr;
        this.dstAddr = dstAddr;
        this.counter = 0;
        this.error = false;
        this.serviceLength = serviceLength;
        this.informationLength = informationLength;
//        this.size = serviceLength + informationLength;
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

    public Packet(int srcAddr, int dstAddr, int serviceLength, int informationLength, TransferType transferType, Status status) {
        this.srcAddr = srcAddr;
        this.dstAddr = dstAddr;
        this.counter = 0;
        this.error = false;
        this.serviceLength = serviceLength;
        this.informationLength = informationLength;
//        this.size = serviceLength + informationLength;
        this.id = getNextId();
        this.status = status;
        this.transferType = transferType;
        this.linkId = -1;
    }

    public Packet(int srcAddr, int dstAddr, int serviceLength, int informationLength, TransferType transferType, Status status, int linkId) {
        this.srcAddr = srcAddr;
        this.dstAddr = dstAddr;
        this.counter = 0;
        this.error = false;
        this.serviceLength = serviceLength;
        this.informationLength = informationLength;
//        this.size = serviceLength + informationLength;
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
        this.serviceLength = 1;
        this.informationLength = 1;
//        this.size = serviceLength + informationLength;
        this.id = getNextId();
        this.status = Status.DATA;
        this.transferType = TransferType.DATAGRAM;
    }

    public Packet(int srcAddr, int dstAddr, int counter, int serviceLength, int informationLength) {
        this.srcAddr = srcAddr;
        this.dstAddr = dstAddr;
        this.counter = counter;
        this.error = false;
        this.serviceLength = serviceLength;
        this.informationLength = informationLength;
//        this.size = serviceLength + informationLength;
        this.id = getNextId();
        this.status = Status.DATA;
        this.transferType = TransferType.DATAGRAM;
    }

    public Packet(Packet packet){
        this.srcAddr = packet.getSrcAddr();
        this.dstAddr = packet.getDstAddr();
        this.counter = packet.getCounter();
        this.error = packet.isError();
        this.serviceLength = packet.getServiceLength();
        this.informationLength = packet.getInformationLength();
//        this.size = packet.getSize();
        this.id = packet.getId();
        this.status = packet.getStatus();
        this.transferType = packet.getTransferType();
    }

    public Packet(Packet packet, Status STATUS){
        this.srcAddr = packet.getSrcAddr();
        this.dstAddr = packet.getDstAddr();
        this.counter = packet.getCounter();
        this.error = packet.isError();
        this.serviceLength = packet.getServiceLength();
        this.informationLength = packet.getInformationLength();
//        this.size = packet.getSize();
        this.id = packet.reverseId();
        this.status = STATUS;
        this.transferType = packet.getTransferType();
    }

    public int getSize() {
        return serviceLength + informationLength;
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

    public void setSrcAddr(int srcAddr) {
        this.srcAddr = srcAddr;
    }

    public void setDstAddr(int dstAddr) {
        this.dstAddr = dstAddr;
    }

    public int getServiceLength() {
        return serviceLength;
    }

    public void setServiceLength(int serviceLength) {
        this.serviceLength = serviceLength;
    }

    public int getInformationLength() {
        return informationLength;
    }

    public void setInformationLength(int informationLength) {
        this.informationLength = informationLength;
    }

    public static void setNextId(int nextId) {
        Packet.nextId = nextId;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "main.Packet{" +
                "srcAddr=" + srcAddr +
                ", dstAddr=" + dstAddr +
                ", counter=" + counter +
                ", serviceLength=" + serviceLength +
                ", informationLength=" + informationLength +
                ", error=" + error +
                ", id=" + id +
                ", status=" + status +
                ", transferType=" + transferType +
                ", linkId=" + linkId +
                '}';
    }
}
