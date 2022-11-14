package paymentgateway.paymentchannel.debitcard;

import paymentgateway.paymentchannel.core.PaymentChannelDecorator;

import java.util.HashMap;

import paymentgateway.paymentchannel.core.PaymentChannel;
import paymentgateway.paymentchannel.core.PaymentChannelComponent;

public class PaymentChannelImpl extends PaymentChannelDecorator {

	protected String bankCode;
	protected String directDebitUrl;
	public PaymentChannelImpl(PaymentChannelComponent record, String bankCode, String directDebitUrl) {
		super(record);
		this.bankCode = bankCode;
		this.directDebitUrl = directDebitUrl;
	}

	public String getBankCode() {
		return this.bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getDirectDebitUrl() {
		return this.directDebitUrl;
	}

	public void setDirectDebitUrl(String directDebitUrl) {
		this.directDebitUrl = directDebitUrl;
	}
	
	public HashMap<String,Object> toHashMap() {
		HashMap<String,Object> debitCardMap = record.toHashMap();
		debitCardMap.put("bankCode", getBankCode());
		debitCardMap.put("directDebitUrl", getDirectDebitUrl());
		return debitCardMap;
	}
}
