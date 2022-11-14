package paymentgateway.paymentchannel.ewallet;

import java.util.HashMap;

import paymentgateway.paymentchannel.core.PaymentChannelDecorator;
import paymentgateway.paymentchannel.core.PaymentChannel;
import paymentgateway.paymentchannel.core.PaymentChannelComponent;

public class PaymentChannelImpl extends PaymentChannelDecorator {

	protected String eWalletType;
	protected String eWalletUrl;
	public PaymentChannelImpl(PaymentChannelComponent record, String eWalletType, String eWalletUrl) {
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
