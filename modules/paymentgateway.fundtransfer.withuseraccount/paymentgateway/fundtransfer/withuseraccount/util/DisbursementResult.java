package paymentgateway.fundtransfer.withuseraccount;

public class DisbursementResult {
    private DisbursementResponse disbursement;
    private Result result;
    
    public DisbursementResponse getDisbursement() {
        return disbursement;
    }
    public void setDisbursement(DisbursementResponse disbursement) {
        this.disbursement = disbursement;
    }
    public Result getResult() {
        return result;
    }
    public void setResult(Result result) {
        this.result = result;
    }
}
