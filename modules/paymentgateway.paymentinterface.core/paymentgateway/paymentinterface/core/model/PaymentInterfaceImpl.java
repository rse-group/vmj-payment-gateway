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
	protected String apiKey;
	protected int amount;
	protected String apiEndpoint;

	public PaymentInterfaceImpl(String apiKey, int amount, String idTransaction, String apiEndpoint) {
		this.apiKey = apiKey;
		this.amount = amount;
		this.idTransaction = idTransaction;
		this.apiEndpoint = apiEndpoint;
	}

	public String getApiKey() {
		return this.apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getApiEndpoint() {
		return this.apiEndpoint;
	}

	public void setApiEndpoint(String apiEndpoint) {
		this.apiEndpoint = apiEndpoint;
	}

}

