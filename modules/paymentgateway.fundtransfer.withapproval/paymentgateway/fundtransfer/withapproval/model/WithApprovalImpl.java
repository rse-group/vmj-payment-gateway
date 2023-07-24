package paymentgateway.fundtransfer.withapproval;

import paymentgateway.fundtransfer.core.WithApprovalDecorator;
import paymentgateway.fundtransfer.core.WithApproval;
import paymentgateway.fundtransfer.core.WithApprovalComponent;

@Entity(name="fundtransfer_withapproval")
@Table(name="fundtransfer_withapproval")
public class WithApprovalImpl extends WithApprovalDecorator {

	protected String approverId;
	public WithApprovalImpl(WithApprovalComponent record, String approverId, FundTransferImpl fundtransferimpl) {
		super(record);
		this.approverId = approverId;
	}

	public String getApproverId() {
		return this.approverId;
	}

	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}

	public void approveTransfer(String transactionId) {
		// TODO: implement this method
	}

	public void rejectTransfer(String transactionId) {
		// TODO: implement this method
	}

}
