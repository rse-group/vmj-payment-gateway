package paymentgateway.paymentinterface.paymentrouting;

import paymentgateway.paymentinterface.core.PaymentInterfaceDecorator;

import java.util.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import paymentgateway.paymentinterface.core.PaymentInterface;
import paymentgateway.paymentinterface.core.PaymentInterfaceComponent;

public class PaymentInterfaceImpl extends PaymentInterfaceDecorator {

	protected String paymentMethods;
	protected String sourceOfFunds;
	protected List<PaymentRoutingRecipient> paymentRoutings;
	protected String paymentCheckoutUrl;
	public PaymentInterfaceImpl(
			PaymentInterfaceComponent record, String paymentMethods,
			String sourceOfFunds, List<PaymentRoutingRecipient> paymentRoutings,
			String paymentCheckoutUrl) {
		super(record);
		this.paymentMethods = paymentMethods;
		this.sourceOfFunds = sourceOfFunds;
		this.paymentCheckoutUrl = paymentCheckoutUrl;
		this.paymentRoutings = paymentRoutings;
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
	
	public String getPaymentCheckoutUrl() {
		return this.paymentCheckoutUrl;
	}

	public void setPaymentCheckoutUrl(String paymentCheckoutUrl) {
		this.paymentCheckoutUrl = paymentCheckoutUrl;
	}
	
	public void setPaymentRoutings(List<PaymentRoutingRecipient> paymentRoutings) {
		this.paymentRoutings = paymentRoutings;
	}
	
	public List<PaymentRoutingRecipient> getPaymentRoutings() {
		return this.paymentRoutings;
	}
	
	public HashMap<String,Object> toHashMap() {
		HashMap<String,Object> paymentRoutingMap = record.toHashMap();
		paymentRoutingMap.put("paymentMethods", getPaymentMethods());
		paymentRoutingMap.put("sourceOfFunds", getSourceOfFunds());
		paymentRoutingMap.put("paymentCheckoutUrl", getPaymentCheckoutUrl());
		List<Map<String,Object>> paymentRoutings = new ArrayList<>();
		for(PaymentRoutingRecipient x : this.paymentRoutings) {
			paymentRoutings.add(x.toHashMap());
		}
		paymentRoutingMap.put("paymentRoutings", paymentRoutings);
		return paymentRoutingMap;
	}

}
