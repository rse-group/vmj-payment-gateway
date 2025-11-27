package paymentgateway.disbursement.core;

import java.util.*;

import vmj.routing.route.VMJExchange;

public abstract class DisbursementResourceDecorator extends DisbursementResourceComponent {
    protected DisbursementResourceComponent record;

    public DisbursementResourceDecorator(DisbursementResourceComponent record){
        this.record = record;
    }

    public int callback(VMJExchange vmjExchange) {
        return record.callback(vmjExchange);
    }
    
    public HashMap<String, Object> getDisbursement(VMJExchange vmjExchange) {
        return record.getDisbursement(vmjExchange);
    }

    public List<HashMap<String, Object>> getAllDisbursement(VMJExchange vmjExchange) {
        return record.getAllDisbursement(vmjExchange);
    }

    public List<HashMap<String, Object>> deleteDisbursement(VMJExchange vmjExchange) {
        return record.deleteDisbursement(vmjExchange);
    }

    public HashMap<String, Object> updateDisbursement(VMJExchange vmjExchange) {
        return record.updateDisbursement(vmjExchange);
    }

    public HashMap<String, Object> disbursement(VMJExchange vmjExchange) {
        return record.disbursement(vmjExchange);
    }
}