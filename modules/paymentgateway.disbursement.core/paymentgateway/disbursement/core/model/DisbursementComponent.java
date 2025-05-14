package paymentgateway.disbursement.core;

import java.util.*;
import vmj.routing.route.VMJExchange;

import org.hibernate.annotations.CreationTimestamp;
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
	protected UUID id;
	protected int userId;
	protected String accountNumber;
	protected String bankCode;
	protected double amount;
	protected String status;
	protected String vendorName;
	protected String vendorGeneratedId;
	
	@CreationTimestamp
	@Column(name = "createdAt", updatable = false)
	protected Date createdAt;
	
	public DisbursementComponent() { }

	public DisbursementComponent(
		UUID id, 
		int userId, 
		String accountNumber, 
		double amount, 
		String bankCode,
		String status,
		String vendorName,
		String vendorGeneratedId
	) {
		this.id = id;
		this.userId = userId;
		this.accountNumber = accountNumber;
		this.amount = amount;
		this.bankCode = bankCode;
		this.status = status;
		this.vendorName = vendorName;
		this.vendorGeneratedId = vendorGeneratedId;
	}

	public abstract UUID getId();
	public abstract void setId(UUID id);

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
	
	public abstract String getVendorName();

	public abstract void setVendorName(String vendorName);
	
//	public abstract Date getCreatedAt();
//	public abstract void setCreatedAt(Date createdAt);

	public abstract String getVendorGeneratedId();
	public abstract void setVendorGeneratedId(String vendorGeneratedId);

	public abstract HashMap<String, Object> toHashMap();
}
