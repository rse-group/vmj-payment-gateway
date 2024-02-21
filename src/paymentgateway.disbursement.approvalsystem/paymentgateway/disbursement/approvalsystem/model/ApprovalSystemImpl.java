package paymentgateway.disbursement.approvalsystem;

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
	protected String approvalStatus;

	public ApprovalSystemImpl(DisbursementComponent record, int approverId, String approvalStatus) {
		super(record);
		this.approverId = approverId;
		this.approvalStatus = approvalStatus;
	}
	public ApprovalSystemImpl() {
		super();
	}

	public int getApproverId() {
		return this.approverId;
	}

	public void setApproverId(int approverId) {
		this.approverId = approverId;
	}

	public String getApprovalStatus() {
		return this.approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public HashMap<String, Object> toHashMap() {
		HashMap<String, Object> ApprovalSystemMap = record.toHashMap();
		ApprovalSystemMap.put("approverId", getApproverId());
		ApprovalSystemMap.put("approvalStatus", getApprovalStatus());
		return ApprovalSystemMap;
	}

}
