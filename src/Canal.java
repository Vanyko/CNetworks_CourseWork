import java.util.Random;

/**
 * Created by Vanyko on 11/15/18.
 */
public class Canal {
    enum TYPE{FDUPLEX, HDUPLEX}

    private static final int ERROR_ACCURACY = 10000;

    private int weight;
    private double errorProbability;
    private TYPE type;
    private boolean isSatellite;

    private Packet packet1, packet2;
    private int timeToDeliver1, timeToDeliver2; // time when packet started transfer
    private int id;

    private static int nextId = 0;

    public Canal(){
        this.weight = 1;
        Random random = new Random();
        this.errorProbability = (double) weight / ERROR_ACCURACY;
        this.type = TYPE.FDUPLEX;
        this.isSatellite = false;
        this.packet1 = null;
        this.timeToDeliver1 = -1;
        this.packet2 = null;
        this.timeToDeliver2 = -1;
        this.id = getNextId();
    }

    public Canal(int weight, int errorProbability, TYPE type, boolean isSatellite) {
        this.weight = weight;
        this.errorProbability = errorProbability;
        this.type = type;
        this.isSatellite = isSatellite;
        this.packet1 = null;
        this.timeToDeliver1 = -1;
        this.packet2 = null;
        this.timeToDeliver2 = -1;
        this.id = getNextId();
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public double getErrorProbability() {
        return errorProbability;
    }

    public void setErrorProbability(double errorProbability) {
        this.errorProbability = errorProbability;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public boolean isSatellite() {
        return isSatellite;
    }

    public void setSatellite(boolean satellite) {
        isSatellite = satellite;
    }

    public boolean isFree(int difference){
        if (difference < 0){
            return isFree1();
        } else {
            return isFree2();
        }
    }

    public boolean isFree1(){
        return packet1 == null;
    }

    public boolean isFree2(){
        return packet2 == null;
    }

    public void setFree1(){
        packet1 = null;
        timeToDeliver1 = 0;
    }

    public void setFree2(){
        packet2 = null;
        timeToDeliver2 = 0;
    }

    public void decTime(){
        if (timeToDeliver1 > 0) --this.timeToDeliver1;
        if (timeToDeliver2 > 0) --this.timeToDeliver2;
    }

    public void setPacket(Packet packet, int difference){
        if (difference < 0){
            setPacket1(packet);
        } else {
            setPacket2(packet);
        }
    }

    public boolean almostDelivered1(){
        return !isFree1() && (timeToDeliver1 <= 0);
    }

    public boolean almostDelivered2(){
        return !isFree2() && (timeToDeliver2 <= 0);
    }

    public boolean almostDelivered(int difference){
        if (difference > 0) {
            return almostDelivered1();
        } else {
            return almostDelivered2();
        }
    }

    public Packet getPacket(int difference){
        Packet result = null;
        if (almostDelivered(difference)) {
            if (difference > 0) {
                result =  new Packet(packet1);
                setFree1();
            } else {
                result = new Packet(packet2);
                setFree2();
            }
        }

        return result;
    }

    public Packet getPacket1() {
        return packet1;
    }

    public void setPacket1(Packet packet1) {
        this.packet1 = packet1;
        this.timeToDeliver1 = packet1.getSize() + this.weight;
    }

    public Packet getPacket2() {
        return packet2;
    }

    public void setPacket2(Packet packet2) {
        this.packet2 = packet2;
        this.timeToDeliver2 = packet2.getSize();
    }

    public boolean tick(){
        decTime();
        Random random = new Random();
        if (packet1 != null){
            double err = random.nextDouble();
            double value = errorProbability * (weight + packet1.getSize()) / weight;
            if (err < value) packet1.setError(true);
        }

        if (packet2 != null){
            double err = random.nextDouble();
            double value = errorProbability * (weight + packet2.getSize()) / weight;
            if (err < value) packet2.setError(true);
        }

        return (packet1 != null) || (packet2 != null) || (timeToDeliver1 > 0) || (timeToDeliver2 > 0);

        //        if ((getTimeToDeliver() <= 0) && (!isFree())){
//            setFree();
//        }
    }

    private static int getNextId(){
        return nextId++;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Canal{" +
                "weight=" + weight +
                ", errorProbability=" + errorProbability +
                ", type=" + type +
                ", isSatellite=" + isSatellite +
                ", packet1=" + packet1 +
                ", packet2=" + packet2 +
                ", timeToDeliver1=" + timeToDeliver1 +
                ", timeToDeliver2=" + timeToDeliver2 +
                ", id=" + id +
                '}';
    }
}
