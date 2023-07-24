package paymentgateway.fundtransfer.withuseraccount;

public class FundTransferResponse {
    private DisbursementResult[] results;

    public DisbursementResult[] getResults() {
        return results;
    }

    public void setResults(DisbursementResult[] results) {
        this.results = results;
    }
}
