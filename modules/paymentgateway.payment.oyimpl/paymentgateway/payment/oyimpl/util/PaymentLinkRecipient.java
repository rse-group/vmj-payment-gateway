package paymentgateway.payment.oyimpl;

import java.util.HashMap;

public class PaymentLinkRecipient {
	protected String recipient_account;
	protected String recipient_bank;
	protected int recipient_amount;
	protected String recipient_note;
	protected String recipient_email;

	public PaymentLinkRecipient() {
	}

	public void setRecipient_account(String recipient_account) {
		this.recipient_account = recipient_account;
	}

	public String getRecipient_account() {
		return this.recipient_account;
	}

	public void setRecipient_bank(String recipient_bank) {
		this.recipient_bank = recipient_bank;
	}

	public String getRecipient_bank() {
		return this.recipient_bank;
	}

	public void setRecipient_amount(int recipient_amount) {
		this.recipient_amount = recipient_amount;
	}

	public int getRecipient_amount() {
		return this.recipient_amount;
	}

	public void setRecipient_note(String recipient_note) {
		this.recipient_note = recipient_note;
	}

	public String getRecipient_note() {
		return this.recipient_note;
	}

	public void setRecipient_email(String recipient_email) {
		this.recipient_email = recipient_email;
	}

	public String getRecipient_email() {
		return this.recipient_email;
	}

	public HashMap<String, Object> toHashMap() {
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("recipient_bank", getRecipient_bank());
		result.put("recipient_account", getRecipient_account());
		result.put("recipient_amount", getRecipient_amount());
		result.put("recipient_note", getRecipient_note());
		result.put("recipient_email", getRecipient_email());
		return result;
	}
}