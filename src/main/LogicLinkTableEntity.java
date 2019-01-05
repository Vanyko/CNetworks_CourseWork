package main;

public class LogicLinkTableEntity {
    private int id;
    private int time;
    private Status status;
    private int dstAddr;

    public LogicLinkTableEntity(int id, int time, Status status, int dstAddr) {
        this.id = id;
        this.time = time;
        this.status = status;
        this.dstAddr = dstAddr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getdstAddr() {
        return dstAddr;
    }

    public void setdstAddr(int dstAddr) {
        this.dstAddr = dstAddr;
    }

    @Override
    public String toString() {
        return "main.LogicLinkTableEntity{" +
                "id=" + id +
                ", time=" + time +
                ", status=" + status +
                ", dstAddr=" + dstAddr +
                '}';
    }
}
