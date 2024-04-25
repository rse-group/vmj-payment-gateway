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

	protected String transactionToken;
	public PaymentImpl(PaymentComponent record, String transactionToken) {
		super(record);
		this.transactionToken = transactionToken;
	}
	public PaymentImpl(){
		super();
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
