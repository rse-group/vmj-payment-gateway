package paymentgateway.disbursement.core;

import java.math.BigInteger;
import java.util.*;

import vmj.routing.route.VMJExchange;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity(name = "disbursement_impl")
@Table(name = "disbursement_impl")
public class DisbursementImpl extends DisbursementComponent {
	protected String accountNumber;
	protected String bankCode;
	protected double amount;

	public DisbursementImpl(int id, int userId, String accountNumber, double amount, String bankCode) {
		this.id = id;
		this.userId = userId;
		this.objectName = DisbursementImpl.class.getName();
		this.accountNumber = accountNumber;
		this.amount = amount;
		System.out.println("lebah ganteng");
		System.out.println(amount);
		this.bankCode = bankCode;
	}

	// read this for more information why default constructor needed
	// https://stackoverflow.com/questions/44088360/org-hibernate-instantiationexception-no-default-constructor-for-entity-princ
	// https://stackoverflow.com/questions/25452018/hibernate-annotations-no-default-constructor-for-entity?rq=1
	public DisbursementImpl() { }

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getBankCode() {
		return bankCode;
	}
	
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public HashMap<String, Object> toHashMap() {
		HashMap<String, Object> disbursementHashMap = new HashMap<>();
		disbursementHashMap.put("id", getId());
		disbursementHashMap.put("user_id", getUserId());
		disbursementHashMap.put("bank_code", getBankCode());
		disbursementHashMap.put("account_number", getAccountNumber());
		disbursementHashMap.put("amount", getAmount());
		return disbursementHashMap;
	}
}
