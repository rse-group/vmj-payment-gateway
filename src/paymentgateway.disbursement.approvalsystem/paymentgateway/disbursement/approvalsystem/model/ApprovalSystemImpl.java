package paymentgateway.disbursement.moneytransfer;

import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.Table;

import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementDecorator;
import paymentgateway.disbursement.core.DisbursementComponent;

@Entity(name = "approvalsystem_impl")
@Table(name = "approvalsystem_impl")
public class ApprovalSystemImpl extends DisbursementDecorator {
	protected int approverId;

	public ApprovalSystemImpl(DisbursementComponent record, int approverId, String status) {
		super(record);
		this.approverId = approverId;
		this.status = status;
	}
	public ApprovalSystemImpl() {
		super();
	}

	public String getApproverId() {
		return this.approverId;
	}

	public void setApproverId(int approverId) {
		this.approverId = approverId;
	}

	public HashMap<String, Object> toHashMap() {
		HashMap<String, Object> ApprovalSystemMap = record.toHashMap();
		ApprovalSystemMap.put("approvedId", getApproverId());
		return ApprovalSystemMap;
	}

}
