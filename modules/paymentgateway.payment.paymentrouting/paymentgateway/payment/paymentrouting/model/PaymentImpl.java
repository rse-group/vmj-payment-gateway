package paymentgateway.payment.paymentrouting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentComponent;
import paymentgateway.payment.core.PaymentDecorator;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.ElementCollection;

@Entity(name = "paymentrouting_impl")
@Table(name = "paymentrouting_impl")
public class PaymentImpl extends PaymentDecorator {

//	protected String paymentMethods;
//	protected String sourceOfFunds;
	protected String paymentCheckoutUrl;
	public PaymentImpl(
			PaymentComponent record,String paymentCheckoutUrl
			) {
		super(record);
//		this.paymentMethods = paymentMethods;
//		this.sourceOfFunds = sourceOfFunds;
		this.paymentCheckoutUrl = paymentCheckoutUrl;
	}

	public PaymentImpl(){
		super();
	}

//	public String getPaymentMethods() {
//		return this.paymentMethods;
//	}
//
//	public void setPaymentMethods(String paymentMethods) {
//		this.paymentMethods = paymentMethods;
//	}
//	public String getSourceOfFunds() {
//		return this.sourceOfFunds;
//	}
//
//	public void setSourceOfFunds(String sourceOfFunds) {
//		this.sourceOfFunds = sourceOfFunds;
//	}
	public String getPaymentCheckoutUrl() {
		return this.paymentCheckoutUrl;
	}

	public void setPaymentCheckoutUrl(String paymentCheckoutUrl) {
		this.paymentCheckoutUrl = paymentCheckoutUrl;
	}
	
	public HashMap<String,Object> toHashMap() {
		HashMap<String,Object> paymentRoutingMap = record.toHashMap();
//		paymentRoutingMap.put("paymentMethods", getPaymentMethods());
//		paymentRoutingMap.put("sourceOfFunds", getSourceOfFunds());
		paymentRoutingMap.put("paymentCheckoutUrl", getPaymentCheckoutUrl());
		
		return paymentRoutingMap;
	}
}
