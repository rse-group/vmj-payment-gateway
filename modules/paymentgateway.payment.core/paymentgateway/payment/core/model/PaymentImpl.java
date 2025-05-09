package paymentgateway.payment.core;

import java.lang.Math;
import java.util.*;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


@Entity(name="payment_impl")
@Table(name="payment_impl")
public class PaymentImpl extends PaymentComponent {

	public PaymentImpl(UUID idTransaction, String vendorName, double amount, String status) {
		this.idTransaction = idTransaction;
		this.vendorName = vendorName;
		this.amount = amount;
		this.status = status;
	}

	public  PaymentImpl(){

	}
	public UUID getId() {
		return idTransaction;
	}

	public void setId(UUID id) {
		this.idTransaction = id;
	}

	public double getAmount() {
		return this.amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public HashMap<String,Object> toHashMap() {
		HashMap<String,Object> interfaceMap = new HashMap<String,Object>();
		interfaceMap.put("id", getId().toString());
		interfaceMap.put("vendorName", getVendorName());
		interfaceMap.put("amount", getAmount());
		interfaceMap.put("status", getStatus());
		interfaceMap.put("createdAt", getCreatedAt().toString());
		return interfaceMap;
	}
}

