package paymentgateway.disbursement.moneytransfer;

import paymentgateway.disbursement.core.DisbursementDecorator;

import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.Table;

import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementComponent;

@Entity(name = "moneyTransfer_impl")
@Table(name = "moneyTransfer_impl")
public class MoneyTransferImpl extends DisbursementDecorator {
	protected String status;

	public MoneyTransferImpl(DisbursementComponent record, String status) {
		super(record);
		this.status = status;
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
