package paymentgateway.payment.midtransimpl;

import paymentgateway.payment.core.PaymentDecorator;

import java.util.HashMap;

import paymentgateway.payment.core.PaymentComponent;

public class PaymentLinkImpl extends PaymentDecorator {

	protected String note;

	public PaymentLinkImpl(PaymentComponent record, String note) {
		super(record);
		this.note = note;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public HashMap<String, Object> toHashMap() {
		HashMap<String, Object> paymentLinkMap = record.toHashMap();
		paymentLinkMap.put("note", getNote());
		return paymentLinkMap;
	}

}
