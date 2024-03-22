package paymentgateway.payment.paymentlink;

public class PaymentLinkResponse {
	private int id;
	// Midtrans
	private String order_id;
	private String payment_url;
	
	// Oy
	private boolean status;
	private String url;

	private String payment_link_id;
	private String child_balance;
	private String email;
	private String message;


	public boolean getStatus() {
		return status;
	}

	public String getEmail() {
		return email;
	}

	public String getChild_balance() {
		return child_balance;
	}

	public String getPayment_link_id() {
		return payment_link_id;
	}

	public String getUrl() {
		if (url == null){
			return payment_url;
		}
		return url;
	}

	public String getMessage() {
		return message;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setChild_balance(String child_balance) {
		this.child_balance = child_balance;
	}

	public void setPayment_link_id(String payment_link_id) {
		this.payment_link_id = payment_link_id;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id){
		this.id = id;
	}
	
	public void setPayment_url(String payment_url) {
		this.payment_url = payment_url;
	}
	
//	public String getPayment_url() {
//		return payment_url;
//	}
	
	@Override
	public String toString() {
		return "PaymentLinkResponse [order_id="
				+ order_id
				+ ", payment_url="
				+ payment_url
				+ "]";
	}
}