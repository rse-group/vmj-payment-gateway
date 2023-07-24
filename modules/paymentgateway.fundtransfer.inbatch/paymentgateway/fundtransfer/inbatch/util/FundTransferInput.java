package paymentgateway.fundtransfer.inbatch;

public class FundTransferInput {
    private String reference;
    private DisbursementItem[] disbursements;
    
    public String getReference() {
        return reference;
    }
    public void setReference(String reference) {
        this.reference = reference;
    }
    public DisbursementItem[] getDisbursements() {
        return disbursements;
    }
    public void setDisbursements(DisbursementItem[] disbursements) {
        this.disbursements = disbursements;
    }
}
