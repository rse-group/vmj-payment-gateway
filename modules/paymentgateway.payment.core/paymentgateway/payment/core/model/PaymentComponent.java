package paymentgateway.payment.core;

import java.util.*;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name="payment_comp")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PaymentComponent implements Payment {

	@Id
	protected int paymentId;
	protected double amount;
	protected String vendorName;
	

	public PaymentComponent() {

	}

	public abstract String getVendorName();

	public abstract void setVendorName(String vendorName);

	public int getPaymentId() {
		return this.paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public abstract HashMap<String, Object> toHashMap();
}
