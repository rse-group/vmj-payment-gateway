package paymentgateway.paymentinterface.core;

import java.util.*;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name="paymentinterface_comp")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PaymentInterfaceComponent implements PaymentInterface{
	
	@Id
	protected String idTransaction; 

	public PaymentInterfaceComponent() {

	} 

	public String getIdTransaction() {
		return this.idTransaction;
	}

	public void setIdTransaction(String idTransaction) {
		this.idTransaction = idTransaction;
	}
	
	public HashMap<String,Object> toHashMap() {
		HashMap<String,Object> interfaceMap = new HashMap<String,Object>();
		interfaceMap.put("idTransaction", getIdTransaction());
		interfaceMap.put("amount", getAmount());
		return interfaceMap;
	}
}
