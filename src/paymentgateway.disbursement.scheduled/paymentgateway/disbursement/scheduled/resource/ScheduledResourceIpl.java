package paymentgateway.disbursement.scheduled;

import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementResourceComponent;
import paymentgateway.disbursement.core.DisbursementResourceDecorator;

public class ScheduledResourceImpl extends DisbursementResourceDecorator {
    public ScheduledResourceImpl(DisbursementResourceComponent record){
        super(record);
    }

    public Disbursement createDisbursement(VMJExchange vmjExchange, String productName, String serviceName){
        // TODO: Modify moneytransfer with scheduled
    }
}