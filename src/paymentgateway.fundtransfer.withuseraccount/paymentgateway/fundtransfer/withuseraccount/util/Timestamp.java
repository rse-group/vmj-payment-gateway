package paymentgateway.fundtransfer.withuseraccount;

public class Timestamp {
    private int seconds;
    private int nanos;
    public int getSeconds() {
        return seconds;
    }
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
    public int getNanos() {
        return nanos;
    }
    public void setNanos(int nanos) {
        this.nanos = nanos;
    }
}

