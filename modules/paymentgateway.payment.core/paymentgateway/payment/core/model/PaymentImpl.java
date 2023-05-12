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
	protected int amount;

	public PaymentImpl(String idTransaction, int amount) {
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

