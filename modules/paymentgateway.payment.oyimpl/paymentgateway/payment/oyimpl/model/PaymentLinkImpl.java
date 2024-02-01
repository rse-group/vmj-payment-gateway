package paymentgateway.payment.oyimpl;

import paymentgateway.payment.core.PaymentDecorator;

import java.util.HashMap;

import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentComponent;

public class PaymentLinkImpl extends PaymentDecorator {

	protected String paymentMethods;
	protected String sourceOfFunds;
	protected String paymentLink;

	public PaymentLinkImpl(PaymentComponent record, String paymentLink, String paymentMethods, String sourceOfFunds) {
		super(record);
		this.paymentMethods = paymentMethods;
		this.sourceOfFunds = sourceOfFunds;
		this.paymentLink = paymentLink;
	}

	public void setPaymentMethods(String paymentMethods) {
		this.paymentMethods = paymentMethods;
	}

	public String getPaymentMethods() {
		return paymentMethods;
	}

	public void setSourceOfFunds(String sourceOfFunds) {
		this.sourceOfFunds = sourceOfFunds;
	}

	public String getSourceOfFunds() {
		return sourceOfFunds;
	}

	public String getPaymentLink() {
		return this.paymentLink;
	}

	public void setPaymentLink(String paymentLink) {
		this.paymentLink = paymentLink;
	}

	public HashMap<String, Object> toHashMap() {
		HashMap<String, Object> paymentLinkMap = record.toHashMap();
		paymentLinkMap.put("paymentLink", getPaymentLink());
		return paymentLinkMap;
	}

}
