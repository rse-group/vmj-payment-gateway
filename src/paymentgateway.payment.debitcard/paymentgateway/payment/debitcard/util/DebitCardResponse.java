package paymentgateway.payment.debitcard;

public class DebitCardResponse {

	private int id;
	private String status_code;
	private String status_message;
	private String transaction_id;
	private String order_id;
	private String redirect_url;
	private String merchant_id;
	private String gross_amount;
	private String currency;
	private String payment_type;
	private String transaction_time;
	private String transaction_status;
	private String fraud_status;
	
	public void setStatus_code(String status_code) {
		this.status_code = status_code;
	}
	
	public String getStatus_code() {
		return this.status_code;
	}
	
	public void setStatus_message(String status_message) {
		this.status_message = status_message;
	}
	
	public String getStatus_message() {
		return this.status_message;
	}
	
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	
	public String getTransaction_id() {
		return this.transaction_id;
	}
	
	public void setId(int order_id) {
		this.id = order_id;
	}
	
	public int getId() {
		if(id == 0) {
			return Integer.parseInt(order_id);
		}
		return id;
	}
	
	public void setRedirect_url(String redirect_url) {
		this.redirect_url = redirect_url;
	}
	
	public String getRedirect_url() {
		return this.redirect_url;
	}
	
	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}
	
	public String getMerchant_id() {
		return this.merchant_id;
	}
	
	public void setGross_amount(String gross_amount) {
		this.gross_amount = gross_amount;
	}
	
	public String getGross_amount() {
		return this.gross_amount;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getCurrency() {
		return this.currency;
	}
	
	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}
	
	public String getPayment_type() {
		return this.payment_type;
	}
	
	public void setTransaction_time(String transaction_time) {
		this.transaction_time = transaction_time;
	}
	
	public String getTransaction_time() {
		return this.transaction_time;
	}
	
	public void setTransaction_status(String transaction_status) {
		this.transaction_status = transaction_status;
	}
	
	public String getTransaction_status() {
		return this.transaction_status;
	}
	
	public void setFraud_status(String fraud_status) {
		this.fraud_status = fraud_status;
	}
	
	public String getFraud_status() {
		return this.fraud_status;
	}
}