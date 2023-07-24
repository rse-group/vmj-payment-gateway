package paymentgateway.fundtransfer.withuseraccount;

import java.util.*;

import paymentgateway.fundtransfer.core.FundTransferDecorator;
import paymentgateway.fundtransfer.core.FundTransfer;
import paymentgateway.fundtransfer.core.FundTransferComponent;
import paymentgateway.fundtransfer.core.FundTransferImpl;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Lob;

@Entity(name="fundtransfer_withuseraccount")
@Table(name="fundtransfer_withuseraccount")
public class WithUserAccountImpl extends FundTransferDecorator {
	public String bankAccountId;
	
	public WithUserAccountImpl(FundTransferComponent record, String bankAccountId) {
		super(record);
		this.bankAccountId = bankAccountId;
	}
	
	public WithUserAccountImpl(int id, FundTransferComponent record, String bankAccountId) {
		super(id, record);
		this.bankAccountId = bankAccountId;
	}
	
    public WithUserAccountImpl() {
    	super();
    }
    
    public String getBankAccountId() {
    	return this.bankAccountId;
    }
    
    public void setBankAccountId(String bankAccountId) {
    	this.bankAccountId = bankAccountId;
    }
    
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> fundTransferMap = record.toHashMap();
        fundTransferMap.put("id", id);
        fundTransferMap.put("bankAccountId", getBankAccountId());
        return fundTransferMap;
    }
}
