package paymentgateway.paymentchannel.oy_ewallet;

import paymentgateway.paymentchannel.core.PaymentChannelDecorator;

import java.util.HashMap;

import paymentgateway.paymentchannel.core.PaymentChannelComponent;

public class OyPaymentChannelImpl extends PaymentChannelDecorator {
	protected String idCustomer;
	protected String successRedirectUrl;
	public OyPaymentChannelImpl(PaymentChannelComponent record, String idCustomer, String successRedirectUrl) {
		super(record);
		this.idCustomer = idCustomer;
		this.successRedirectUrl = successRedirectUrl;
	}
	
	public String getIdCustomer() {
		return this.idCustomer;
	}
	
	public void setIdCustomer(String idCustomer) {
		this.idCustomer = idCustomer;
	}
	
	public String getSuccessRedirectUrl() {
		return this.successRedirectUrl;
	}
	
	public void setSuccessRedirectUrl(String successRedirectUrl) {
		this.successRedirectUrl = successRedirectUrl;
	}
	
	public HashMap<String,Object> toHashMap() {
		HashMap<String,Object> paymentChannelMap = record.toHashMap();
		paymentChannelMap.put("idCustomer", getIdCustomer());
		paymentChannelMap.put("successRedirectUrl", getSuccessRedirectUrl());
		return paymentChannelMap;
	}
}