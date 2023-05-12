package paymentgateway.disbursement.scheduledtransfer;

import java.util.HashMap;
import java.util.Date;

import paymentgateway.disbursement.core.DisbursementComponent;

import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.scheduled.ScheduledImpl;

public class ScheduledTransferImpl extends ScheduledImpl {


	public ScheduledTransferImpl(DisbursementComponent record, Date date) {
		super(record,date);
	}

}
