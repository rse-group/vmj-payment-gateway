package paymentgateway.payment.core;

import java.math.BigInteger;
import java.util.*;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import javax.persistence.OneToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.CascadeType;

@MappedSuperclass
public abstract class PaymentDecorator extends PaymentComponent{

	@OneToOne(cascade=CascadeType.REMOVE, optional=true, orphanRemoval = true)
	protected PaymentComponent record;
		
	public PaymentDecorator (PaymentComponent record) {
		this.record = record;
		this.idTransaction = UUID.randomUUID();
	}

	public PaymentDecorator () {
	}
	public UUID getIdTransaction() {
		return record.getIdTransaction();
	}
	public void setIdTransaction(UUID idTransaction) {
		record.setIdTransaction(idTransaction);
	}
	public double getAmount() {
		return record.getAmount();
	}
	public void setAmount(double amount) {
		record.setAmount(amount);
	}

	public String getVendorName(){
		return record.getVendorName();
	}
	public void setVendorName(String vendorName){
		record.setVendorName(vendorName);
	}

	public String getStatus() {
		return record.getStatus();
	}

	public void setStatus(String status) {
		record.setStatus(status);
	}

	public Date getCreatedAt() {
		return record.getCreatedAt();
	}
}

