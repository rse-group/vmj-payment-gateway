package paymentgateway.client.brankas;

class DisbursementResponse{
    private String disbursement_id;
    private String reference_id;
    private String type;
    private SourceBankAccountDetails source_account;
    private DestinationBankAccountDetails destination_account;
    private Amount destination_amount;
    private String description;
    private Fee[] fees;
    private External external;
    private String status;
    private Timestamp created;
    private Timestamp updated;
    private String disbursement_request_id;
    private String note;
    private String merchant_txn_id;

    public String getDisbursement_id() {
        return disbursement_id;
    }

    public void setDisbursement_id(String disbursement_id) {
        this.disbursement_id = disbursement_id;
    }

    public String getReference_id() {
        return reference_id;
    }

    public void setReference_id(String reference_id) {
        this.reference_id = reference_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SourceBankAccountDetails getSource_account() {
        return source_account;
    }

    public void setSource_account(SourceBankAccountDetails source_account) {
        this.source_account = source_account;
    }

    public DestinationBankAccountDetails getDestination_account() {
        return destination_account;
    }

    public void setDestination_account(DestinationBankAccountDetails destination_account) {
        this.destination_account = destination_account;
    }

    public Amount getDestination_amount() {
        return destination_amount;
    }

    public void setDestination_amount(Amount destination_amount) {
        this.destination_amount = destination_amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Fee[] getFees() {
        return fees;
    }

    public void setFees(Fee[] fees) {
        this.fees = fees;
    }

    public External getExternal() {
        return external;
    }

    public void setExternal(External external) {
        this.external = external;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public String getDisbursement_request_id() {
        return disbursement_request_id;
    }

    public void setDisbursement_request_id(String disbursement_request_id) {
        this.disbursement_request_id = disbursement_request_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMerchant_txn_id() {
        return merchant_txn_id;
    }

    public void setMerchant_txn_id(String merchant_txn_id) {
        this.merchant_txn_id = merchant_txn_id;
    }
}

class DisbursementResult {
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

class Result {
    private String success;
    private Error[] error;
    private String message;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Error[] getError() {
        return error;
    }

    public void setError(Error[] error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

public class FundTransferResponse {
    private DisbursementResult[] results;

    public DisbursementResult[] getResults() {
        return results;
    }

    public void setResults(DisbursementResult[] results) {
        this.results = results;
    }
}
