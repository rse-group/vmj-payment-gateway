package paymentgateway.client.oy;

public class FundTransferRequest {
    private String recipient_bank;
    private String recipient_account;
    private Integer amount;
    private String note;
    private String partner_trx_id;
    private String email;
    private String child_balance;

    public String getRecipientBank() {
        return recipient_bank;
    }

    public void setRecipientBank(String recipientBank) {
        this.recipient_bank = recipientBank;
    }

    public String getRecipientAccount() {
        return recipient_account;
    }

    public void setRecipientAccount(String recipientAccount) {
        this.recipient_account = recipientAccount;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPartnerTrxId() {
        return partner_trx_id;
    }

    public void setPartnerTrxId(String partnerTrxId) {
        this.partner_trx_id = partnerTrxId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChildBalance() {
        return child_balance;
    }

    public void setChildBalance(String childBalance) {
        this.child_balance = childBalance;
    }
}