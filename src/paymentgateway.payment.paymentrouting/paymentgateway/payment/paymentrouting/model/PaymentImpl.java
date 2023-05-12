package paymentgateway.payment.paymentrouting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentComponent;
import paymentgateway.payment.core.PaymentDecorator;

public class PaymentImpl extends PaymentDecorator {

	protected String paymentMethods;
	protected String sourceOfFunds;
	protected List<PaymentRoutingRecipient> routings;
	protected String paymentCheckoutUrl;
	public PaymentImpl(
			PaymentComponent record, String paymentMethods,
			String sourceOfFunds, List<PaymentRoutingRecipient> routings,
			String paymentCheckoutUrl
			) {
		super(record);
		this.paymentMethods = paymentMethods;
		this.sourceOfFunds = sourceOfFunds;
		this.routings = routings;
		this.paymentCheckoutUrl = paymentCheckoutUrl;
	}

	public String getPaymentMethods() {
		return this.paymentMethods;
	}

	public void setPaymentMethods(String paymentMethods) {
		this.paymentMethods = paymentMethods;
	}
	public String getSourceOfFunds() {
		return this.sourceOfFunds;
	}

	public void setSourceOfFunds(String sourceOfFunds) {
		this.sourceOfFunds = sourceOfFunds;
	}
	public List<PaymentRoutingRecipient> getPaymentRoutings() {
		return this.routings;
	}
	
	public void setPaymentRoutings(List<PaymentRoutingRecipient> routings) {
		this.routings = routings;
	}
	public String getPaymentCheckoutUrl() {
		return this.paymentCheckoutUrl;
	}

	public void setPaymentCheckoutUrl(String paymentCheckoutUrl) {
		this.paymentCheckoutUrl = paymentCheckoutUrl;
	}
	
	public HashMap<String,Object> toHashMap() {
		HashMap<String,Object> paymentRoutingMap = record.toHashMap();
		paymentRoutingMap.put("paymentMethods", getPaymentMethods());
		paymentRoutingMap.put("sourceOfFunds", getSourceOfFunds());
		List<PaymentRoutingRecipient> paymentRoutings = new ArrayList<PaymentRoutingRecipient>();
		for(PaymentRoutingRecipient x : this.routings) {
			paymentRoutings.add(x);
		}
		paymentRoutingMap.put("routings", paymentRoutings);
		paymentRoutingMap.put("paymentCheckoutUrl", getPaymentCheckoutUrl());
		
		return paymentRoutingMap;
	}
}
