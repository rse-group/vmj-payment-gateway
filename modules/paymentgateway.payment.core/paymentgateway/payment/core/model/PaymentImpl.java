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
	
	protected double amount;
	protected String vendorName;
	protected String status;

	public PaymentImpl(int idTransaction, String vendorName, double amount, String status) {
		this.idTransaction = idTransaction;
		this.vendorName = vendorName;
		this.amount = amount;
		this.status = status;
	}

	public  PaymentImpl(){

	}
	public int getId() {
		return idTransaction;
	}

	public void setId(int id) {
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
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public HashMap<String,Object> toHashMap() {
		HashMap<String,Object> interfaceMap = new HashMap<String,Object>();
		interfaceMap.put("id", getId());
		interfaceMap.put("vendorName", getVendorName());
		interfaceMap.put("amount", getAmount());
		interfaceMap.put("status",getStatus());
		return interfaceMap;
	}
}

