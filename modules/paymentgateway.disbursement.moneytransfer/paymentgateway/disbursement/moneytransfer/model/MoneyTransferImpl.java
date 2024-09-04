package paymentgateway.disbursement.moneytransfer;

import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.Table;

import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementDecorator;
import paymentgateway.disbursement.core.DisbursementComponent;

@Entity(name = "moneytransfer_impl")
@Table(name = "moneytransfer_impl")
public class MoneyTransferImpl extends DisbursementDecorator {
	protected String status;

	public MoneyTransferImpl(DisbursementComponent record, int userId, String status) {
		super(record, userId, MoneyTransferImpl.class.getName());
		this.status = status;
	}

	public MoneyTransferImpl() {
		super();
	}

	@Override
	public int getId() {
		return super.getId();
	}

	@Override
	public void setUserId(int userId) {
		super.setUserId(userId);
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public HashMap<String, Object> toHashMap() {
		HashMap<String, Object> moneyTransferMap = record.toHashMap();
		moneyTransferMap.put("status", getStatus());
		return moneyTransferMap;
	}
}
