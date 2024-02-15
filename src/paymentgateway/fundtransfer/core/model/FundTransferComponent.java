package paymentgateway.disbursement.core;

import java.util.*;
import vmj.routing.route.VMJExchange;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "fundtransfer_comp")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class FundTransferComponent implements FundTransfer {

	@Id
	protected  int id;
	protected int userId;

	//
	protected String destinationCode;
	protected String reference;
	protected String description;
	protected String destinationHolderName;
	protected String destinationAccountNumber;
	protected String status;
	protected String statusDescription;
	protected String email;
	protected int amount;
	protected String senderId;
	protected String created;

	public FundTransferComponent() {

	}

	public int getId(){return id;}
	public void setId(int id){this.id = id;}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public abstract void setAmount(double amount);
	public abstract double getAmount();
	public abstract String getBankCode();
	public abstract void setBankCode(String bankCode);
	public abstract String getAccountNumber();
	public abstract void setAccountNumber(String accountNumber);
	//
	public abstract void sendTransfer();
	public abstract void getTransferByReference();
	public abstract void getTransferById();
	//
	public abstract HashMap<String, Object> toHashMap();
}
