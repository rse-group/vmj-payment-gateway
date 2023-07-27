package paymentgateway.client.oy;

class OyResponseStatus {
    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

public class FundTransferResponse {
	 private OyResponseStatus status;
	    private String amount;
	    private String recipient_bank;
	    private String recipient_account;
	    private String trx_id;
	    private String partner_trx_id;
	    private String timestamp;

	    public OyResponseStatus getStatus() {
	        return status;
	    }

	    public void setStatus(OyResponseStatus status) {
	        this.status = status;
	    }

	    public String getAmount() {
	        return amount;
	    }

	    public void setAmount(String amount) {
	        this.amount = amount;
	    }

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

	    public String getTrxId() {
	        return trx_id;
	    }

	    public void setTrxId(String trxId) {
	        this.trx_id = trxId;
	    }

	    public String getPartnerTrxId() {
	        return partner_trx_id;
	    }

	    public void setPartnerTrxId(String partnerTrxId) {
	        this.partner_trx_id = partnerTrxId;
	    }

	    public String getTimestamp() {
	        return timestamp;
	    }

	    public void setTimestamp(String timestamp) {
	        this.timestamp = timestamp;
	    }
}