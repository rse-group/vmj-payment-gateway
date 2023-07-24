package paymentgateway.fundtransfer.core;

import java.util.*;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name="fundtransfer_comp")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class FundTransferComponent implements FundTransfer {
	
	@Id
	protected int id;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public abstract String getDestinationCode();
	public abstract void setDestinationCode(String destinationCode);
	public abstract String getReference();
	public abstract void setReference(String reference);
	public abstract String getDescription();
	public abstract void setDescription(String description);
	public abstract String getDestinationHolderName();
	public abstract void setDestinationHolderName(String destinationHolderName);
	public abstract String getDestinationAccountNumber();
	public abstract void setDestinationAccountNumber(String destinationAccountNumber);
	public abstract String getStatus();
	public abstract void setStatus(String status);
	public abstract String getStatusDescription();
	public abstract void setStatusDescription(String statusDescription);
	public abstract String getEmail();
	public abstract void setEmail(String email);
	public abstract Integer getAmount();
	public abstract void setAmount(Integer amount);
	public abstract void setSenderId(String senderId);
	public abstract String getCreated();
	public abstract void setCreated(String created);
	
	public HashMap<String,Object> toHashMap() {
		HashMap<String,Object> interfaceMap = new HashMap<String,Object>();
		interfaceMap.put("id", getId());
		interfaceMap.put("amount", getAmount());
		interfaceMap.put("destinationCode", getDestinationCode());
		interfaceMap.put("destinationAccountNumber", getDestinationAccountNumber());
		return interfaceMap;
	}
}
