package paymentgateway.paymentinterface.oy_paymentlink;

public class OyPaymentLinkResponse {
	private String payment_link_id;
	private String message;
	private String email_status;
	private String url;
	private boolean status;
	
	public String getPayment_link_id() {
		return payment_link_id;
	}

	public void setPayment_link_id(String payment_link_id) {
		this.payment_link_id = payment_link_id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getEmail_status() {
		return email_status;
	}

	public void setEmail_status(String email_status) {
		this.email_status = email_status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}