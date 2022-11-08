package paymentgateway.paymentinterface.core;

import java.util.*;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import javax.persistence.OneToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.CascadeType;
//add other required packages

@MappedSuperclass
public abstract class PaymentInterfaceDecorator extends PaymentInterfaceComponent{
	protected PaymentInterfaceComponent record;
		
	public PaymentInterfaceDecorator (PaymentInterfaceComponent record) {
		this.record = record;
	}

	public String getApiKey() {
		return record.getApiKey();
	}
	public void setApiKey(String apiKey) {
		record.setApiKey(apiKey);
	}
	public int getAmount() {
		return record.getAmount();
	}
	public void setAmount(int amount) {
		record.setAmount(amount);
	}
	public String getIdTransaction() {
		return record.getIdTransaction();
	}
	public void setIdTransaction(String idTransaction) {
		record.setIdTransaction(idTransaction);
	}
	public String getApiEndpoint() {
		return record.getApiEndpoint();
	}
	public void setApiEndpoint(String apiEndpoint) {
		record.setApiEndpoint(apiEndpoint);
	}
}

