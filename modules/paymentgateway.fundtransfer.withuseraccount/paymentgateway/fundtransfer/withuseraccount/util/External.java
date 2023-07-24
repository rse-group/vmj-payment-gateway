package paymentgateway.fundtransfer.withuseraccount;

public class External {
    private String reference_id;
    private String status;
    private String settlement_rail;
    private Error[] error;
    
    public String getReference_id() {
        return reference_id;
    }
    public void setReference_id(String reference_id) {
        this.reference_id = reference_id;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getSettlement_rail() {
        return settlement_rail;
    }
    public void setSettlement_rail(String settlement_rail) {
        this.settlement_rail = settlement_rail;
    }
    public Error[] getError() {
        return error;
    }
    public void setError(Error[] error) {
        this.error = error;
    }
}
