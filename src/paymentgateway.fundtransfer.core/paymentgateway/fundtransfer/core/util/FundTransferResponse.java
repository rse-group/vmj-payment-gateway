package paymentgateway.fundtransfer.core;

public class FundTransferResponse {
    private FundTransferResponseStatus status;
    private String amount;
    private String recipient_bank;
    private String recipient_account;
    private String trx_id;
    private String partner_trx_id;
    private String timestamp;
    
    public FundTransferResponseStatus getStatus() {
        return status;
    }
    public void setStatus(FundTransferResponseStatus status) {
        this.status = status;
    }
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getRecipient_bank() {
        return recipient_bank;
    }
    public void setRecipient_bank(String recipient_bank) {
        this.recipient_bank = recipient_bank;
    }
    public String getRecipient_account() {
        return recipient_account;
    }
    public void setRecipient_account(String recipient_account) {
        this.recipient_account = recipient_account;
    }
    public String getTrx_id() {
        return trx_id;
    }
    public void setTrx_id(String trx_id) {
        this.trx_id = trx_id;
    }
    public String getPartner_trx_id() {
        return partner_trx_id;
    }
    public void setPartner_trx_id(String partner_trx_id) {
        this.partner_trx_id = partner_trx_id;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
