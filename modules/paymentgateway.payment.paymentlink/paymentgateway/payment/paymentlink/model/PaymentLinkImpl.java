package paymentgateway.payment.paymentlink;

import paymentgateway.payment.core.PaymentDecorator;

import java.util.HashMap;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;

import paymentgateway.payment.core.PaymentComponent;

@Entity(name = "paymentlink_impl")
@Table(name = "paymentlink_impl")
public class PaymentLinkImpl extends PaymentDecorator {

	protected int id;
	protected String paymentLink;
	public PaymentLinkImpl(PaymentComponent record, int id, String paymentLink) {
		super(record);
		this.id = id;
		this.paymentLink = paymentLink;
	}

	public PaymentLinkImpl(){
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPaymentLink() {
		return this.paymentLink;
	}

	public void setPaymentLink(String paymentLink) {
		this.paymentLink = paymentLink;
	}
	
	public HashMap<String,Object> toHashMap() {
		HashMap<String,Object> paymentLinkMap = record.toHashMap();
		paymentLinkMap.put("paymentLink", getPaymentLink());
		return paymentLinkMap;
	}

}
