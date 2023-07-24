package paymentgateway.fundtransfer.core;

import java.lang.Math;
import java.util.*;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name="fundtransfer_impl")
@Table(name="fundtransfer_impl")
public class FundTransferImpl extends FundTransferComponent {
	protected String status;
	protected String statusDescription;
	protected String reference;
	protected String description;
	protected Integer amount;
	protected String destinationCode;
	protected String destinationHolderName;
	protected String email;
	protected String senderId;
	protected String created;
	protected String destinationAccountNumber;

	public FundTransferImpl(
			String status,
			String statusDescription,
			String reference,
			String description,
			Integer amount,
			String destinationCode,
			String destinationHolderName,
			String email,
			String senderId,
			String created,
			String destinationAccountNumber
	) {
        Random r = new Random();
		this.id = Math.abs(r.nextInt());
		this.status = status;
		this.statusDescription = statusDescription;
		this.reference = reference;
		this.description = description;
		this.amount = amount;
		this.destinationCode = destinationCode;
		this.destinationHolderName = destinationHolderName;
		this.email = email;
		this.senderId = senderId;
		this.created = created;
		this.destinationAccountNumber = destinationAccountNumber;
	}
	
	public FundTransferImpl(
			int id,
			String status,
			String statusDescription,
			String reference,
			String description,
			Integer amount,
			String destinationCode,
			String destinationHolderName,
			String email,
			String senderId,
			String created,
			String destinationAccountNumber
	) {
		this.id = Math.abs(id);
		this.status = status;
		this.statusDescription = statusDescription;
		this.reference = reference;
		this.description = description;
		this.amount = amount;
		this.destinationCode = destinationCode;
		this.destinationHolderName = destinationHolderName;
		this.email = email;
		this.senderId = senderId;
		this.created = created;
		this.destinationAccountNumber = destinationAccountNumber;
	}
	
	public FundTransferImpl() {
		Random r = new Random();
		this.id = Math.abs(r.nextInt());
		this.status = "PENDING";
		this.statusDescription = "Waiting for Partner";
		this.reference = "DISB-" + Math.abs(r.nextInt());
		this.description = "";
		this.amount = 10000;
		this.destinationCode = "BCA";
		this.destinationHolderName = "Joe";
		this.email = "";
		this.senderId = "";
		this.created = "";
		this.destinationAccountNumber = "42345345";
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusDescription() {
		return this.statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}
	public String getReference() {
		return this.reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getAmount() {
		return this.amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getDestinationCode() {
		return this.destinationCode;
	}

	public void setDestinationCode(String destinationCode) {
		this.destinationCode = destinationCode;
	}
	public String getDestinationHolderName() {
		return this.destinationHolderName;
	}

	public void setDestinationHolderName(String destinationHolderName) {
		this.destinationHolderName = destinationHolderName;
	}
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenderId() {
		return this.senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getCreated() {
		return this.created;
	}

	public void setCreated(String created) {
		this.created = created;
	}
	public String getDestinationAccountNumber() {
		return this.destinationAccountNumber;
	}

	public void setDestinationAccountNumber(String destinationAccountNumber) {
		this.destinationAccountNumber = destinationAccountNumber;
	}
}
