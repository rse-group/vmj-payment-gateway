package paymentgateway.disbursement.scheduled;

import paymentgateway.disbursement.core.DisbursementDecorator;

import java.util.HashMap;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementComponent;

@Entity(name = "scheduled_impl")
@Table(name = "scheduled_impl")
public class ScheduledImpl extends DisbursementDecorator {

	protected Date date;
	public ScheduledImpl(DisbursementComponent record, Date date) {
		super(record);
		this.date = date;
	}

	public ScheduledImpl() {
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public HashMap<String, Object> toHashMap() {
		HashMap<String, Object> moneyTransferMap = record.toHashMap();
		moneyTransferMap.put("date", getDate());
		return moneyTransferMap;
	}

}
