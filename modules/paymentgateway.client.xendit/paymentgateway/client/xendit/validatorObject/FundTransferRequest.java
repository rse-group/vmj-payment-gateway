package paymentgateway.client.xendit;

class DisbursementItem {
    private String amount;
    private String bank_code;
    private String bank_account_name;
    private String bank_account_number;
    private String description;
    private String external_id;
    private String[] email_to;
    private String[] email_cc;
    private String[] email_bcc;
    
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getBank_code() {
        return bank_code;
    }
    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }
    public String getBank_account_name() {
        return bank_account_name;
    }
    public void setBank_account_name(String bank_account_name) {
        this.bank_account_name = bank_account_name;
    }
    public String getBank_account_number() {
        return bank_account_number;
    }
    public void setBank_account_number(String bank_account_number) {
        this.bank_account_number = bank_account_number;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getExternal_id() {
        return external_id;
    }
    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }
    public String[] getEmail_to() {
        return email_to;
    }
    public void setEmail_to(String[] email_to) {
        this.email_to = email_to;
    }
    public String[] getEmail_cc() {
        return email_cc;
    }
    public void setEmail_cc(String[] email_cc) {
        this.email_cc = email_cc;
    }
    public String[] getEmail_bcc() {
        return email_bcc;
    }
    public void setEmail_bcc(String[] email_bcc) {
        this.email_bcc = email_bcc;
    }
}

public class FundTransferRequest {
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