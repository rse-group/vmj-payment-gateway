package paymentgateway.disbursement.approval;

import paymentgateway.disbursement.core.DisbursementDecorator;

import java.util.HashMap;
import java.util.Date;

import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementComponent;

//@Entity(name = "scheduled_impl")
//@Table(name = "scheduled_impl")
public class ApprovalImpl extends DisbursementDecorator {

	protected String approvalId;
	public ApprovalImpl(DisbursementComponent record, String approvalId) {
		super(record);
		this.approvalId = approvalId;
	}

	public String getApprovalId() {
		return this.approvalId;
	}

	public void setApprovalId(String approvalId){this.approvalId = approvalId;
	}

	public HashMap<String, Object> toHashMap() {
		HashMap<String, Object> moneyTransferMap = record.toHashMap();
		moneyTransferMap.put("approvalId", approvalId);
		return moneyTransferMap;
	}

}
