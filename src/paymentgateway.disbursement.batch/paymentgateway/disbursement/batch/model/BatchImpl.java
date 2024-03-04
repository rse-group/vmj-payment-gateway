package paymentgateway.disbursement.batch.model;

import java.util.HashMap;

import paymentgateway.disbursement.core.DisbursementComponent;
import paymentgateway.disbursement.core.DisbursementDecorator;
import paymentgateway.disbursement.moneytransfer.*;

@Entity(name = "batch_impl")
@Table(name = "batch_impl")
public class BatchImpl extends DisbursementDecorator{
    protected List<MoneyTransferImpl> payouts;

    public BatchImpl(DisbursementComponent record, List<MoneyTransferImpl> payouts){
        super(record);
        this.payouts = payouts;
    }

    public BatchImpl(){

    }

    public void setPayouts(List<MoneyTransferImpl> payouts){
        this.payouts = payouts;
    }

    public List<MoneyTransferImpl> getPayouts(){
        return payouts;
    }

    public HashMap<String, Object> toHashMap(){
        HashMap<String, Object> specialMap = record.toHashMap();
        specialMap.put("payouts", payouts);
        return specialMap;
    }
    
}
