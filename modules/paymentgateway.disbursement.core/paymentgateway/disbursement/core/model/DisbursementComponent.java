package paymentgateway.disbursement.core;

import java.util.*;
import vmj.routing.route.VMJExchange;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "disbursement_comp")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class DisbursementComponent implements Disbursement {
	@Id
	protected  int id;
	protected int userId;
	protected String accountNumber;
	protected String bankCode;
	protected double amount;

	@Column(nullable = true)
	protected String objectName = null;

	public DisbursementComponent() { }

	public DisbursementComponent(int id, int userId, String accountNumber, double amount, String bankCode) {
		this.id = id;
		this.userId = userId;
		this.objectName = DisbursementComponent.class.getName();
		this.accountNumber = accountNumber;
		this.amount = amount;
		this.bankCode = bankCode;
	}

	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public abstract double getAmount();
	public abstract void setAmount(double amount);

	public abstract String getBankCode();
	public abstract void setBankCode(String bankCode);

	public abstract String getAccountNumber();
	public abstract void setAccountNumber(String accountNumber);

	public abstract HashMap<String, Object> toHashMap();
}
