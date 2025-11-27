package paymentgateway.payment.debitcard;

import paymentgateway.payment.core.PaymentDecorator;

import java.util.HashMap;

import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentComponent;

import javax.persistence.Entity;
import javax.persistence.Table;
@Entity(name = "debitcard_impl")
@Table(name = "debitcard_impl")
public class PaymentImpl extends PaymentDecorator {

	protected String bankCode;
	protected String directDebitUrl;
	public PaymentImpl(PaymentComponent record, String bankCode, String directDebitUrl) {
		super(record);
		this.bankCode = bankCode;
		this.directDebitUrl = directDebitUrl;
	}

	public PaymentImpl(){
		super();
	}

	public String getBankCode() {
		return this.bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getDirectDebitUrl() {
		return this.directDebitUrl;
	}

	public void setDirectDebitUrl(String directDebitUrl) {
		this.directDebitUrl = directDebitUrl;
	}

	public HashMap<String,Object> toHashMap() {
		HashMap<String,Object> debitCardMap = record.toHashMap();
		debitCardMap.put("bankCode", getBankCode());
		debitCardMap.put("directDebitUrl", getDirectDebitUrl());
		return debitCardMap;
	}
}
