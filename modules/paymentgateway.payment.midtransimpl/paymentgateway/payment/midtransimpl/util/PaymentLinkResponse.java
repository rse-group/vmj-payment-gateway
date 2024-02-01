package paymentgateway.payment.midtransimpl;

public class PaymentLinkResponse {
	private String order_id;
	private String payment_url;

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setPayment_url(String payment_url) {
		this.payment_url = payment_url;
	}

	public String getPayment_url() {
		return payment_url;
	}

	@Override
	public String toString() {
		return "PaymentLinkResponse [order_id="
				+ order_id
				+ ", payment_url="
				+ payment_url
				+ "]";
	}
}