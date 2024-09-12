package paymentgateway.disbursement.core;

import java.util.*;

import vmj.routing.route.VMJExchange;

public abstract class DisbursementControllerDecorator extends DisbursementControllerComponent {
    protected DisbursementControllerComponent record;

    public DisbursementControllerDecorator(DisbursementControllerComponent record){
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

    public HashMap<String, Object> moneyTransfer(VMJExchange vmjExchange) {
        return record.moneyTransfer(vmjExchange);
    }
}