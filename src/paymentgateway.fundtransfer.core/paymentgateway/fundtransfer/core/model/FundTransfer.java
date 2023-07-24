package paymentgateway.fundtransfer.core;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import java.util.*;

public interface FundTransfer {
	int getId();
	void setId(int id);
	String getDestinationCode();
	void setDestinationCode(String destinationCode);
	String getReference();
	void setReference(String reference);
	String getDescription();
	void setDescription(String description);
	String getDestinationHolderName();
	void setDestinationHolderName(String destinationHolderName);
	String getDestinationAccountNumber();
	void setDestinationAccountNumber(String destinationAccountNumber);
	String getStatus();
	void setStatus(String status);
	String getStatusDescription();
	void setStatusDescription(String statusDescription);
	String getEmail();
	void setEmail(String email);
	Integer getAmount();
	void setAmount(Integer amount);
	String getSenderId();
	void setSenderId(String senderId);
	String getCreated();
	void setCreated(String created);
	
	HashMap<String,Object> toHashMap();
}
