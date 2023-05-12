package paymentgateway.disbursement.approvaltransfer;

import java.util.HashMap;
import java.util.Date;

import paymentgateway.disbursement.core.DisbursementComponent;

import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.approval.ApprovalImpl;

public class ApprovalTransferImpl extends ApprovalImpl {


	public ApprovalTransferImpl(DisbursementComponent record, String approvalId) {
		super(record,approvalId);
	}

}
