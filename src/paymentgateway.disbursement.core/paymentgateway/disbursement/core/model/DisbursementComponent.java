package paymentgateway.disbursement.core;

import java.util.*;
import vmj.routing.route.Route;
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
	protected String id;
	protected String userId;
//	protected String accountNumber;
//	protected String bankCode;
//	protected int amount;

	public DisbursementComponent() {

	}

	public String getId(){return id;}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


//	public void setAmount(int amount) {
//		this.amount = amount;
//	}
//
//	public int getAmount() {
//		return amount;
//	}

//	public void setBankCode(String bankCode) {
//		this.bankCode = bankCode;
//	}
//
//	public String getBankCode() {
//		return bankCode;
//	}

//	public String getAccountNumber() {
//		return accountNumber;
//	}
//
//	public void setAccountNumber(String accountNumber) {
//		this.accountNumber = accountNumber;
//	}

	public HashMap<String, Object> toHashMap() {
		HashMap<String, Object> interfaceMap = new HashMap<String, Object>();
		interfaceMap.put("id", getId());
		interfaceMap.put("account_number", getAccountNumber());
		interfaceMap.put("bank_code", getBankCode());
		interfaceMap.put("amount", getAmount());
		return interfaceMap;
	}

	// public abstract void sendDisbursement(String reference, String description,
	// String destinationCode,
	// String destinationHolderName, String destinationAccountNumber, String email,
	// Real amount);

	// public abstract void getDisbursementByID(String id);
}
