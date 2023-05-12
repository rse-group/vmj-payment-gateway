package paymentgateway.disbursement.core;

import java.util.*;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import javax.persistence.OneToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.CascadeType;
//add other required packages

@MappedSuperclass
public abstract class DisbursementDecorator extends DisbursementComponent {
//	@OneToOne(mappedBy = "disbursement_comp")
	protected DisbursementComponent record;

	public DisbursementDecorator(DisbursementComponent record) {
		this.record = record;
	}


	public String getId() {
		return record.getId();
	}

	public String getUserId() {
		return record.getUserId();
	}

	public void setUserId(String userId) {
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

	 public int getAmount() {
	 return record.getAmount();
	 }
	 public void setAmount(int amount) {
	 record.setAmount(amount);
	 }
	 public String getDestinationAccountNumber() {
		return record.getAccountNumber();
	}
	 public void setDestinationAccountNumber(String destinationAccountNumber) {
		record.setAccountNumber(destinationAccountNumber);
	}

	// public void sendDisbursement(String reference, String description, String
	// destinationCode,
	// String destinationHolderName,
	// String destinationAccountNumber, String email, Real amount) {
	// return record.sendDisbursement(reference, description, destinationCode,
	// destinationHolderName,
	// destinationAccountNumber, email, amount);
	// }

	// public void getDisbursementByReference(String reference) {
	// return record.getDisbursementByReference(reference);
	// }

	// public void getDisbursementByID(String id) {
	// return record.getDisbursementByID(id);
	// }

}
