package paymentgateway.payment.directdebit;

import paymentgateway.payment.core.PaymentDecorator;

import java.util.HashMap;

import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentComponent;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "directdebit_impl")
@Table(name = "directdebit_impl")
public class PaymentImpl extends PaymentDecorator {

	protected String statusDirectDebitPayment;
	protected String directDebitUrl;

	public PaymentImpl(PaymentComponent record, String statusDirectDebitPayment, String directDebitUrl) {
		super(record);
		this.statusDirectDebitPayment = statusDirectDebitPayment;
		this.directDebitUrl = directDebitUrl;
	}

	public PaymentImpl() {
		super();
	}

	public String getstatusDirectDebitPayment() {
		return this.statusDirectDebitPayment;
	}

	public void setstatusDirectDebitPayment(String statusDirectDebitPayment) {
		this.statusDirectDebitPayment = statusDirectDebitPayment;
	}

	public String getdirectDebitUrl() {
		return this.directDebitUrl;
	}

	public void setdirectDebitUrl(String directDebitUrl) {
		this.directDebitUrl = directDebitUrl;
	}

	public HashMap<String, Object> toHashMap() {
		HashMap<String, Object> directDebitMap = record.toHashMap();
		directDebitMap.put("statusDirectDebitPayment", getstatusDirectDebitPayment());
		directDebitMap.put("directDebitUrl", getdirectDebitUrl());
		return directDebitMap;
	}
}
