package paymentgateway.paymentinterface.oy_paymentrouting;

public class OyPaymentRoutingResponse {
	private OyPaymentRoutingStatus status;
	private String trx_id;
	private String partner_user_id;
	private String partner_trx_id;
	private int receive_amount;
	private String trx_expiration_time;
	private OyPaymentRoutingPaymentInfo payment_info;
	
	public void setStatus(OyPaymentRoutingStatus status) {
		this.status = status;
	}
	
	public OyPaymentRoutingStatus getStatus() {
		return this.status;
	}
	
	public void setTrx_id(String trx_id) {
		this.trx_id = trx_id;
	}
	
	public String getTrx_id() {
		return this.trx_id;
	}
	
	public void setPartner_user_id(String partner_user_id) {
		this.partner_user_id = partner_user_id;
	}
	
	public String getPartner_user_id() {
		return this.partner_user_id;
	}
	
	public void setPartner_trx_id(String partner_trx_id) {
		this.partner_trx_id = partner_trx_id;
	}
	
	public String getPartner_trx_id() {
		return this.partner_trx_id;
	}
	
	public void setReceive_amount(int receive_amount) {
		this.receive_amount = receive_amount;
	}
	
	public int getReceive_amount() {
		return this.receive_amount;
	}
	
	public void setTrx_expiration_time(String trx_expiration_time) {
		this.trx_expiration_time = trx_expiration_time;
	}
	
	public String getTrx_expiration_time() {
		return this.trx_expiration_time;
	}
	
	public void setPayment_info(OyPaymentRoutingPaymentInfo payment_info) {
		this.payment_info = payment_info;
	}
	
	public OyPaymentRoutingPaymentInfo getPayment_info() {
		return this.payment_info;
	}

}

class OyPaymentRoutingStatus {
	private String code;
	private String message;
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}

class OyPaymentRoutingPaymentInfo {
	private String payment_checkout_url;
	
	public void setPayment_checkout_url(String payment_checkout_url) {
		this.payment_checkout_url = payment_checkout_url;
	}
	
	public String getPayment_checkout_url() {
		return this.payment_checkout_url;
	}
}