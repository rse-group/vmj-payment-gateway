package paymentgateway.paymentinterface.invoice;

import java.util.*;

import paymentgateway.paymentinterface.core.PaymentInterfaceDecorator;
import paymentgateway.paymentinterface.core.PaymentInterface;
import paymentgateway.paymentinterface.core.PaymentInterfaceComponent;

public class PaymentInterfaceImpl extends PaymentInterfaceDecorator {

	protected String transactionToken;
	public PaymentInterfaceImpl(PaymentInterfaceComponent record, String transactionToken) {
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
