package paymentgateway.fundtransfer.scheduled;

import paymentgateway.fundtransfer.core.ScheduledDecorator;
import paymentgateway.fundtransfer.core.Scheduled;
import paymentgateway.fundtransfer.core.ScheduledComponent;

@Entity(name="fundtransfer_scheduled")
@Table(name="fundtransfer_scheduled")
public class ScheduledImpl extends ScheduledDecorator {

	public String scheduleDate;
	public ScheduledImpl(ScheduledComponent record, String scheduleDate, FundTransferImpl fundtransferimpl) {
		super(record);
		this.scheduleDate = scheduleDate;
	}


	public void updateScheduledTransfer() {
		// TODO: implement this method
	}

	public void cancelScheduleTransfer() {
		// TODO: implement this method
	}

	public void retryScheduledTransfer() {
		// TODO: implement this method
	}

}
