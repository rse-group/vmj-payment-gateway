package paymentgateway.payment.core;

import java.util.*;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.Column;

@Entity
@Table(name="payment_comp")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PaymentComponent implements Payment {

	@Id
	protected UUID idTransaction;
	protected double amount;
	protected String vendorName;
	protected String status;
	
	@CreationTimestamp
	@Column(name = "createdAt", updatable = false)
	protected Date createdAt;
	

	public PaymentComponent() {

	}

	public abstract String getVendorName();

	public abstract void setVendorName(String vendorName);

	public UUID getIdTransaction() {
		return this.idTransaction;
	}

	public void setIdTransaction(UUID idTransaction) {
		this.idTransaction = idTransaction;
	}

	public abstract String getStatus();

	public abstract void setStatus(String status);

	public abstract Date getCreatedAt();


	public abstract HashMap<String, Object> toHashMap();
}
