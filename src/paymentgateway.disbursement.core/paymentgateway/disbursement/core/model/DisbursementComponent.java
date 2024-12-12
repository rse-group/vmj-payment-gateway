package paymentgateway.disbursement.core;

import java.util.*;
import vmj.routing.route.VMJExchange;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

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
	protected String status;

	public DisbursementComponent() { }

	public DisbursementComponent(
		int id, 
		int userId, 
		String accountNumber, 
		double amount, 
		String bankCode,
		String status
	) {
		this.id = id;
		this.userId = userId;
		this.accountNumber = accountNumber;
		this.amount = amount;
		this.bankCode = bankCode;
		this.status = status;
	}

	public abstract int getId();
	public abstract void setId(int id);

	public abstract int getUserId();
	public abstract void setUserId(int userId);
	
	public abstract double getAmount();
	public abstract void setAmount(double amount);

	public abstract String getBankCode();
	public abstract void setBankCode(String bankCode);

	public abstract String getAccountNumber();
	public abstract void setAccountNumber(String accountNumber);

	public abstract String getStatus();
	public abstract void setStatus(String status);

	public abstract HashMap<String, Object> toHashMap();
}
