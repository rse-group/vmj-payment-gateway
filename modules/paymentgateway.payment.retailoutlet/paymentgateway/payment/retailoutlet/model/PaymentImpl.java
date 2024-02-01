package paymentgateway.payment.retailoutlet;

import paymentgateway.payment.core.PaymentDecorator;

import java.util.HashMap;

import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentComponent;

public class PaymentImpl extends PaymentDecorator {

	protected String retailOutlet;
	protected String retailPaymentCode;
	public PaymentImpl(PaymentComponent record, String retailOutlet, String retailPaymentCode) {
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
