package paymentgateway.payment.ewallet;

import paymentgateway.payment.core.PaymentDecorator;

import java.util.HashMap;

import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentComponent;

public class PaymentImpl extends PaymentDecorator {

	protected String eWalletType;
	protected String eWalletUrl;
	public PaymentImpl(PaymentComponent record, String eWalletType, String eWalletUrl) {
		super(record);
		this.eWalletType = eWalletType;
		this.eWalletUrl = eWalletUrl;
	}

	public String getEWalletType() {
		return this.eWalletType;
	}

	public void setEWalletType(String eWalletType) {
		this.eWalletType = eWalletType;
	}
	public String getEWalletUrl() {
		return this.eWalletUrl;
	}

	public void setEWalletUrl(String eWalletUrl) {
		this.eWalletUrl = eWalletUrl;
	}

	public HashMap<String,Object> toHashMap() {
		HashMap<String,Object> ewalletMap = record.toHashMap();
		ewalletMap.put("eWalletType", getEWalletType());
		ewalletMap.put("eWalletUrl", getEWalletUrl());
		return ewalletMap;
	}
}
