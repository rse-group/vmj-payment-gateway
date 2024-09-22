package paymentgateway.disbursement.core;

import java.util.*;
import vmj.routing.route.VMJExchange;

import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class DisbursementComponent implements Disbursement {
	@Id
	protected  int id;

	public DisbursementComponent() { }

	public DisbursementComponent(int id) {
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id = id;
	}

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
