package paymentgateway.disbursement.special;

import paymentgateway.disbursement.core.DisbursementDecorator;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.HashMap;
import java.util.Date;

import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementComponent;

@Entity(name = "special_impl")
@Table(name = "special_impl")
public class SpecialImpl extends DisbursementDecorator {
	protected int sender_country;
	protected String sender_name;
	protected String sender_address;
	protected String sender_job;
	protected String direction;

	public SpecialImpl(DisbursementComponent record, int sender_country, String sender_name, 
			String sender_address, String sender_job, String direction) {
		super(record);
		this.sender_country = sender_country;
		this.sender_name = sender_name;
		this.sender_address = sender_address;
		this.sender_job = sender_job;
		this.direction = direction;
	}

	public SpecialImpl() {
		super();
	}

	public int getSenderCountry() {
		return sender_country;
	}

	public void setSenderCountry(int sender_country) {
		this.sender_country = sender_country;
	}

	public String getSenderName() {
		return sender_name;
	}

	public void setSenderName(String sender_name) {
		this.sender_name = sender_name;
	}

	public String getSenderAddress() {
		return sender_address;
	}

	public void setSenderAddress(String sender_address) {
		this.sender_address = sender_address;
	}

	public String getSenderJob() {
		return sender_job;
	}

	public void setSenderJob(String sender_job) {
		this.sender_job = sender_job;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public HashMap<String, Object> toHashMap() {
		HashMap<String, Object> specialMap = record.toHashMap();
		specialMap.put("sender_country", getSenderCountry());
		specialMap.put("sender_name", getSenderName());
		specialMap.put("sender_address", getSenderAddress());
		specialMap.put("sender_job", getSenderJob());
		specialMap.put("direction", getDirection());
		return specialMap;
	}
}
