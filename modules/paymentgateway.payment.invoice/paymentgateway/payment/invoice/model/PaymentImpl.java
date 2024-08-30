package paymentgateway.payment.invoice;

import paymentgateway.payment.core.PaymentDecorator;

import java.util.HashMap;

import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentComponent;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity(name = "invoice_impl")
@Table(name = "invoice_impl")
public class PaymentImpl extends PaymentDecorator {

	protected String transactionUrl;
	public PaymentImpl(PaymentComponent record, String transactionUrl) {
		super(record);
		this.transactionUrl = transactionUrl;
	}
	public PaymentImpl(){
		super();
	}

	public String getTransactionUrl() {
		return this.transactionUrl;
	}

	public void setTransactionUrl(String transactionUrl) {
		this.transactionUrl = transactionUrl;
	}
	
	public HashMap<String,Object> toHashMap() {
		HashMap<String,Object> invoiceMap = record.toHashMap();
		invoiceMap.put("transactionUrl", getTransactionUrl());
		return invoiceMap;
	}
}
