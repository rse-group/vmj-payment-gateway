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

//	@Id
//	protected int id;
	protected double amount;
	protected String productName;

	public PaymentImpl(int idTransaction, String productName, double amount) {
		this.idTransaction = idTransaction;
		this.productName = productName;
		this.amount = amount;
	}

	public  PaymentImpl(){

	}
	public int getId() {
		return idTransaction;
	}
//
	public void setId(int id) {
		this.idTransaction = id;
	}

	public double getAmount() {
		return this.amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public HashMap<String,Object> toHashMap() {
		HashMap<String,Object> interfaceMap = new HashMap<String,Object>();
		interfaceMap.put("id", getId());
		interfaceMap.put("productName", getProductName());
		interfaceMap.put("amount", getAmount());
		return interfaceMap;
	}
}

