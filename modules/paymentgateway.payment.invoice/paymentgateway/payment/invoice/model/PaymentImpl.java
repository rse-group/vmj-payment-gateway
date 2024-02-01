package paymentgateway.payment.invoice;

import paymentgateway.payment.core.PaymentDecorator;

import java.util.HashMap;

import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentComponent;

public class PaymentImpl extends PaymentDecorator {

	protected String transactionToken;
	public PaymentImpl(PaymentComponent record, String transactionToken) {
		super(record);
		this.transactionToken = transactionToken;
	}

	public String getTransactionToken() {
		return this.transactionToken;
	}

	public void setTransactionToken(String transactionToken) {
		this.transactionToken = transactionToken;
	}
	
	public HashMap<String,Object> toHashMap() {
		HashMap<String,Object> invoiceMap = record.toHashMap();
		invoiceMap.put("transactionToken", getTransactionToken());
		return invoiceMap;
	}
}
