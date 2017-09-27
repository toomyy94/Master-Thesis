package pt.ptinovacao.arqospocket.core.network;

import com.google.gson.Gson;

import pt.ptinovacao.arqospocket.core.utils.JsonHelper;

/**
 * Wrapper class that contains the result of a ping request.
 * <p>
 * Created by Emílio Simões on 26-04-2017.
 */
public class PingResult {

    public static final PingResult EMPTY = new PingResult(2);

    private double minimum;

    private double average;

    private double maximum;

    private int sentPackets;

    private int receivedPackets;

    private int lostPackets;

    private int result;

    public PingResult() {
    }

    private PingResult(int result) {
        this.result = result;
    }

    public double getMinimum() {
        return minimum;
    }

    public void setMinimum(double minimum) {
        this.minimum = minimum;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public double getMaximum() {
        return maximum;
    }

    public void setMaximum(double maximum) {
        this.maximum = maximum;
    }

    public int getSentPackets() {
        return sentPackets;
    }

    public void setSentPackets(int sentPackets) {
        this.sentPackets = sentPackets;
    }

    public int getReceivedPackets() {
        return receivedPackets;
    }

    public void setReceivedPackets(int receivedPackets) {
        this.receivedPackets = receivedPackets;
    }

    public int getLostPackets() {
        return lostPackets;
    }

    public void setLostPackets(int lostPackets) {
        this.lostPackets = lostPackets;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String toString() {
        Gson gson = JsonHelper.getGsonInstance(true);
        return gson.toJson(this);
    }
}
