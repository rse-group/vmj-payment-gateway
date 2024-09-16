package paymentgateway.disbursement.core;

import java.util.*;

import vmj.routing.route.VMJExchange;

public abstract class DisbursementServiceDecorator extends DisbursementServiceComponent {
    protected DisbursementServiceComponent record;

    public DisbursementServiceDecorator(DisbursementServiceComponent record){
        this.record = record;
    }

    public Disbursement createDisbursement(Map<String, Object> requestBody){
        return record.createDisbursement(requestBody);
    }

    public Disbursement createDisbursement(Map<String, Object> requestBody, Map<String, Object> response){
        return record.createDisbursement(requestBody, response);
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
    
    public Map<String, Object> sendTransaction(Map<String, Object> requestBody){
        return record.sendTransaction(requestBody);
    }

    public HashMap<String, Object> getDisbursementById(int id){
        return record.getDisbursementById(id);
    }
}