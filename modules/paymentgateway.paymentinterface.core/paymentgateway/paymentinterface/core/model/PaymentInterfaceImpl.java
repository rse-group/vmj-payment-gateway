package paymentgateway.paymentinterface.core;

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


@Entity(name="paymentinterface_impl")
@Table(name="paymentinterface_impl")
public class PaymentInterfaceImpl extends PaymentInterfaceComponent {
	// since implementation of API varies types of auth required,
	// it will be handled on API's delta
	//protected String apiKey;
	//protected String apiEndpoint;
	protected int amount;
	

	public PaymentInterfaceImpl(int amount, String idTransaction) {
		//this.apiKey = apiKey;
		this.amount = amount;
		this.idTransaction = idTransaction;
		//this.apiEndpoint = apiEndpoint;
	}

	//public String getApiKey() {
	//	return this.apiKey;
	//}

	//public void setApiKey(String apiKey) {
	//	this.apiKey = apiKey;
	//}
	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	//public String getApiEndpoint() {
	//	return this.apiEndpoint;
	//}

	//public void setApiEndpoint(String apiEndpoint) {
	//	this.apiEndpoint = apiEndpoint;
	//}

}

