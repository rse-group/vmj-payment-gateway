package paymentgateway.paymentchannel.core;

import java.util.*;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import javax.persistence.OneToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.CascadeType;
//add other required packages

@MappedSuperclass
public abstract class PaymentChannelDecorator extends PaymentChannelComponent{
	protected PaymentChannelComponent record;
		
	public PaymentChannelDecorator (PaymentChannelComponent record) {
		this.record = record;
	}

	public String getIdTransaction() {
		return record.getIdTransaction();
	}
	public void setIdTransaction(String idTransaction) {
		record.setIdTransaction(idTransaction);
	}
	public int getAmount() {
		return record.getAmount();
	}
	public void setAmount(int amount) {
		record.setAmount(amount);
	}

}

