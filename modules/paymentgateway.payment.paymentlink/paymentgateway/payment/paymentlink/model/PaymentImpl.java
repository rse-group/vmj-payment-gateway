package paymentgateway.payment.paymentlink;

import paymentgateway.payment.core.PaymentDecorator;

import java.util.HashMap;

import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentComponent;

public class PaymentImpl extends PaymentDecorator {

	protected String paymentLink;
	public PaymentImpl(PaymentComponent record, String paymentLink) {
		super(record);
		this.paymentLink = paymentLink;
	}

	public String getPaymentLink() {
		return this.paymentLink;
	}

	public void setPaymentLink(String paymentLink) {
		this.paymentLink = paymentLink;
	}
	
	public HashMap<String,Object> toHashMap() {
		HashMap<String,Object> paymentLinkMap = record.toHashMap();
		paymentLinkMap.put("paymentLink", getPaymentLink());
		return paymentLinkMap;
	}

}
