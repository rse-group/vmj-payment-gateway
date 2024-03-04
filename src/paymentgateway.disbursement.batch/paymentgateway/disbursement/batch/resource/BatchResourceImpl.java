package paymentgateway.disbursement.batch;

import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementResourceComponent;
import paymentgateway.disbursement.core.DisbursementResourceDecorator;
import paymentgateway.disbursement.core.MoneyTransferResponse;

public class BatchResourceImpl extends DisbursementResourceDecorator {
    public BatchResourceImpl(DisbursementResourceComponent record){
        super(record);
    }

    public Disbursement createDisbursement(VMJExchange vmjExchange, String productName, String serviceName){
        // TODO: Modify moneytransfer with batch
        System.out.println("----IN Batch Transfer ------");
        MoneyTransferResponse response = super.sendTransaction(vmjExchange,productName,serviceName);

        int id = response.getId();
        int user_id = response.getUser_id();
        // TODO: Wait for Midtrans
        

    }
}
