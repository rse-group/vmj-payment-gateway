package paymentgateway.payment.oyimpl;

public class PaymentLinkResponse {
	private Boolean status;
	private String url;
	private String message;
	private String email_status;
	private String payment_link_id;
	private String child_balance;

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Boolean getStatus() {
		return this.status;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setEmail_status(String email_status) {
		this.email_status = email_status;
	}

	public String getEmail_status() {
		return email_status;
	}

	public void setPayment_link_id(String payment_link_id) {
		this.payment_link_id = payment_link_id;
	}

	public String getPayment_link_id() {
		return payment_link_id;
	}

	public void setChild_balance(String child_balance) {
		this.child_balance = child_balance;
	}

	public String getChild_balance() {
		return child_balance;
	}

}
