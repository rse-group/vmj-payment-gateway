package paymentgateway.disbursement.core;

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

@Entity(name = "disbursement_impl")
@Table(name = "disbursement_impl")
public class DisbursementImpl extends DisbursementComponent {
//	@Id
//	protected String id;
//	protected String userId;
	protected String accountNumber;
	protected String bankCode;
	protected int amount;

	public DisbursementImpl(String id, String userId, String accountNumber, int amount, String bankCode) {
//		super(id,userId);
		this.id = id;
		this.userId = userId;
		this.accountNumber = accountNumber;
		this.amount = amount;
		System.out.println("lebah ganteng");
		System.out.println(amount);
		this.bankCode = bankCode;
	}

//	public String getId() {
//		return id;
//	}
//
//	public String getUserId() {
//		return userId;
//	}
//
//	public void setUserId(String userId) {
//		this.userId = userId;
//	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	// public void sendDisbursement(String reference, String description, String
	// destinationCode,
	// String destinationHolderName,
	// String destinationAccountNumber, String email, Real amount) {
	// // TODO: implement this method
	// }

	// public void getDisbursementByReference(String reference) {
	// // TODO: implement this method
	// }

	// public void getDisbursementByID(String id) {
	// // TODO: implement this method
	// }

}
