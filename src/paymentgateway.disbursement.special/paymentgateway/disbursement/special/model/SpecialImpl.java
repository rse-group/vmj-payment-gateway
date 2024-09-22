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

	public int getSender_country() {
		return sender_country;
	}

	public void setSender_country(int sender_country) {
		this.sender_country = sender_country;
	}

	public String getSender_name() {
		return sender_name;
	}

	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}

	public String getSender_address() {
		return sender_address;
	}

	public void setSender_address(String sender_address) {
		this.sender_address = sender_address;
	}

	public String getSender_job() {
		return sender_job;
	}

	public void setSender_job(String sender_job) {
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
		specialMap.put("sender_country", getSender_country());
		specialMap.put("sender_name", getSender_name());
		specialMap.put("sender_address", getSender_address());
		specialMap.put("sender_job", getSender_job());
		specialMap.put("direction", getDirection());
		return specialMap;
	}
}
