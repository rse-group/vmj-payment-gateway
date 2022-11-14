package paymentgateway.paymentchannel.core;

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


@Entity(name="paymentchannel_impl")
@Table(name="paymentchannel_impl")
public class PaymentChannelImpl extends PaymentChannelComponent {
	protected int amount;

	public PaymentChannelImpl(int amount, String idTransaction) {
		this.idTransaction = idTransaction;
		this.amount = amount;
	}

	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}

