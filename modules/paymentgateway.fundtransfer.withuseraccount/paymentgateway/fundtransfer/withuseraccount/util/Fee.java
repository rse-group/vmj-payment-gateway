package paymentgateway.fundtransfer.withuseraccount;

public class Fee {
    private String name;
    private Amount amount;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Amount getAmount() {
        return amount;
    }
    public void setAmount(Amount amount) {
        this.amount = amount;
    }
}
