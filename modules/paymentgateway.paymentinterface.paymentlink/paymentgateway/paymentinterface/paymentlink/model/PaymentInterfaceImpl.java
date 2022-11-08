package paymentgateway.paymentinterface.paymentlink;

import paymentgateway.paymentinterface.core.PaymentInterfaceDecorator;

import java.util.HashMap;

import paymentgateway.paymentinterface.core.PaymentInterface;
import paymentgateway.paymentinterface.core.PaymentInterfaceComponent;

public class PaymentInterfaceImpl extends PaymentInterfaceDecorator {

	protected String paymentLink;
	public PaymentInterfaceImpl(PaymentInterfaceComponent record, String paymentLink) {
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
