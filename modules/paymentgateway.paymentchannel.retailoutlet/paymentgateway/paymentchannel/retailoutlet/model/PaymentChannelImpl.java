package paymentgateway.paymentchannel.retailoutlet;

import java.util.HashMap;

import paymentgateway.paymentchannel.core.PaymentChannelDecorator;
import paymentgateway.paymentchannel.core.PaymentChannel;
import paymentgateway.paymentchannel.core.PaymentChannelComponent;

public class PaymentChannelImpl extends PaymentChannelDecorator {

	protected String retailOutlet;
	protected String retailPaymentCode;
	public PaymentChannelImpl(PaymentChannelComponent record, String retailOutlet, String retailPaymentCode) {
		super(record);
		this.retailOutlet = retailOutlet;
		this.retailPaymentCode = retailPaymentCode;
	}

	public String getRetailOutlet() {
		return this.retailOutlet;
	}

	public void setRetailOutlet(String retailOutlet) {
		this.retailOutlet = retailOutlet;
	}
	public String getRetailPaymentCode() {
		return this.retailPaymentCode;
	}

	public void setRetailPaymentCode(String retailPaymentCode) {
		this.retailPaymentCode = retailPaymentCode;
	}
	
	public HashMap<String,Object> toHashMap() {
		HashMap<String,Object> retailOutletMap = record.toHashMap();
		retailOutletMap.put("retailOutlet", getRetailOutlet());
		retailOutletMap.put("retailPaymentCode", getRetailPaymentCode());
		return retailOutletMap;
	}

}
