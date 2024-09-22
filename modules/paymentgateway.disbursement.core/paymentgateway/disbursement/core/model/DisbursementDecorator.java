package paymentgateway.disbursement.core;

import java.util.*;
import java.math.BigInteger;

import javax.persistence.OneToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.CascadeType;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class DisbursementDecorator extends DisbursementComponent {    
    @Transient
    protected DisbursementComponent record;

    public DisbursementDecorator(DisbursementComponent record) {
        this.id = record.getId();
        this.record = record;
    }

    public DisbursementDecorator() { }

    public int getId() {
        return record.getId();
    }

    public void setId(int id){
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

    public DisbursementComponent getRecord() {
        return this.record;
    }
}
