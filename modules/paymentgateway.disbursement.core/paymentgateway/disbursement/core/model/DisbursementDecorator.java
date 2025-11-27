package paymentgateway.disbursement.core;

import java.util.*;
import java.math.BigInteger;

import javax.persistence.OneToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.CascadeType;

@MappedSuperclass
public abstract class DisbursementDecorator extends DisbursementComponent {
    //https://stackoverflow.com/questions/9116630/one-to-one-delete-on-cascade
    @OneToOne(cascade=CascadeType.REMOVE, optional=true)
    protected DisbursementComponent record;

    public DisbursementDecorator(DisbursementComponent record) {
        this.id = UUID.randomUUID();
        this.record = record;
    }

    public DisbursementDecorator() { }

    public UUID getId() {
        return record.getId();
    }

    public void setId(UUID id){
        record.setId(id);
    }

    public int getUserId() {
        return record.getUserId();
    }

    public void setUserId(int userId) {
        record.setUserId(userId);
    }

    public String getAccountNumber() {
        return record.getAccountNumber();
    }

    public void setAccountNumber(String accountNumber) {
        record.setAccountNumber(accountNumber);
    }

    public String getBankCode() {
        return record.getBankCode();
    }

    public void setBankCode(String bankCode) {
        record.setBankCode(bankCode);
    }

    public double getAmount() {
        return record.getAmount();
    }
    
    public void setAmount(double amount) {
        record.setAmount(amount);
    }

    public String getStatus() {
		return record.getStatus();
	}

	public void setStatus(String status) {
		record.setStatus(status);
	}
	
	public String getVendorName(){
		return record.getVendorName();
	}
	public void setVendorName(String vendorName){
		record.setVendorName(vendorName);
	}

    public String getVendorGeneratedId(){
		return record.getVendorGeneratedId();
	}
	public void setVendorGeneratedId(String vendorGeneratedId){
		record.setVendorGeneratedId(vendorGeneratedId);
	}

    public Date getCreatedAt() {
        return record.getCreatedAt();
    }
	
    public DisbursementComponent getRecord() {
        return this.record;
    }
}
