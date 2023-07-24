package paymentgateway.fundtransfer.core;

import java.util.*;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import javax.persistence.OneToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.CascadeType;
//add other required packages

@MappedSuperclass
public abstract class FundTransferDecorator extends FundTransferComponent{
	@OneToOne(cascade=CascadeType.ALL)
	protected FundTransferComponent record;
	
	public FundTransferDecorator (FundTransferComponent record) {
		this.record = record;
		Random r = new Random();
		this.id = Math.abs(r.nextInt());
	}
	
    public FundTransferDecorator(int id, FundTransferComponent record) {
        this.id = id;
        this.record = record;
    }
    
    public FundTransferDecorator() {
    	super();
    	this.record = new FundTransferImpl();
    	Random r = new Random();
    	this.id = Math.abs(r.nextInt());
    }
    
    public FundTransferComponent getRecord() {
        return this.record;
    }

    public void setRecord(FundTransferComponent record) {
        this.record = record;
    } 

	public int getId() {
		return record.getId();
	}
	public void setId(int id) {
		record.setId(id);
	}
	public String getStatus() {
		return record.getStatus();
	}
	public void setStatus(String status) {
		record.setStatus(status);
	}
	public String getStatusDescription() {
		return record.getStatusDescription();
	}
	public void setStatusDescription(String statusDescription) {
		record.setStatusDescription(statusDescription);
	}
	public String getReference() {
		return record.getReference();
	}
	public void setReference(String reference) {
		record.setReference(reference);
	}
	public String getDescription() {
		return record.getDescription();
	}
	public void setDescription(String description) {
		record.setDescription(description);
	}
	public Integer getAmount() {
		return record.getAmount();
	}
	public void setAmount(Integer amount) {
		record.setAmount(amount);
	}
	public String getDestinationCode() {
		return record.getDestinationCode();
	}
	public void setDestinationCode(String destinationCode) {
		record.setDestinationCode(destinationCode);
	}
	public String getDestinationHolderName() {
		return record.getDestinationHolderName();
	}
	public void setDestinationHolderName(String destinationHolderName) {
		record.setDestinationHolderName(destinationHolderName);
	}
	public String getEmail() {
		return record.getEmail();
	}
	public void setEmail(String email) {
		record.setEmail(email);
	}
	public String getSenderId() {
		return record.getSenderId();
	}
	public void setSenderId(String senderId) {
		record.setSenderId(senderId);
	}
	public String getCreated() {
		return record.getCreated();
	}
	public void setCreated(String created) {
		record.setCreated(created);
	}
	public String getDestinationAccountNumber() {
		return record.getDestinationAccountNumber();
	}
	public void setDestinationAccountNumber(String destinationAccountNumber) {
		record.setDestinationAccountNumber(destinationAccountNumber);
	}
	
    public HashMap<String, Object> toHashMap() {
        return this.record.toHashMap();
    }
}
