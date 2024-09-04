package paymentgateway.disbursement.core;

import java.util.*;

import vmj.routing.route.VMJExchange;

public abstract class DisbursementResourceDecorator extends DisbursementResourceComponent {
    protected DisbursementResourceComponent record;

    public DisbursementResourceDecorator(DisbursementResourceComponent record){
        this.record = record;
    }

    public Disbursement createDisbursement(VMJExchange vmjExchange){
        return record.createDisbursement(vmjExchange);
    }

    public Disbursement createDisbursement(VMJExchange vmjExchange, Map<String, Object> response){
        return record.createDisbursement(vmjExchange, response);
    }

    public HashMap<String, Object> getDisbursement(VMJExchange vmjExchange){
        return record.getDisbursement(vmjExchange);
    }

    public List<HashMap<String, Object>> getAllDisbursement(VMJExchange vmjExchange){
        return record.getAllDisbursement(vmjExchange);
    }

    public List<HashMap<String, Object>> deleteDisbursement(VMJExchange vmjExchange){
        return record.deleteDisbursement(vmjExchange);
    }

    public HashMap<String, Object> updateDisbursement(VMJExchange vmjExchange){
        return record.updateDisbursement(vmjExchange);
    }

    public List<HashMap<String, Object>> transformListToHashMap(List<Disbursement> List){
        return record.transformListToHashMap(List);
    }
    
    public Map<String, Object> sendTransaction(VMJExchange vmjExchange){
        return record.sendTransaction(vmjExchange);
    }

    public HashMap<String, Object> getDisbursementById(int id){
        return record.getDisbursementById(id);
    }
}