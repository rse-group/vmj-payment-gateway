package paymentgateway.payment.card;

import paymentgateway.payment.core.PaymentDecorator;

import java.util.HashMap;

import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentComponent;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "card_impl")
@Table(name = "card_impl")
public class PaymentImpl extends PaymentDecorator {

	protected String idToken;

	public PaymentImpl(PaymentComponent record, String idToken) {
		super(record);
		this.idToken = idToken;
	}

	public PaymentImpl() {
		super();
	}

	public String getIdToken() {
		return this.idToken;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}

	public HashMap<String, Object> toHashMap() {
		HashMap<String, Object> cardMap = record.toHashMap();
		cardMap.put("idToken", getIdToken());
		return cardMap;
	}
}
