package paymentgateway.paymentchannel.core;

import java.util.*;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name="paymentchannel_comp")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PaymentChannelComponent implements PaymentChannel{
	
	@Id
	protected String idTransaction; 

	public PaymentChannelComponent() {

	} 

	public String getIdTransaction() {
		return this.idTransaction;
	}

	public void setIdTransaction(String idTransaction) {
		this.idTransaction = idTransaction;
	}
	
	public HashMap<String,Object> toHashMap() {
		HashMap<String,Object> channelMap = new HashMap<String,Object>();
		channelMap.put("idTransaction", getIdTransaction());
		channelMap.put("amount", getAmount());
		return channelMap;
	}
}
