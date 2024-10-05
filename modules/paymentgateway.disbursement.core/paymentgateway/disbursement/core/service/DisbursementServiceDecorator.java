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

    public HashMap<String, Object> getDisbursement(Map<String, Object> requestBody){
        return record.getDisbursement(requestBody);
    }

    public List<HashMap<String, Object>> getAllDisbursement(Map<String, Object> requestBody){
        return record.getAllDisbursement(requestBody);
    }

    public List<HashMap<String, Object>> deleteDisbursement(Map<String, Object> requestBody){
        return record.deleteDisbursement(requestBody);
    }

    public HashMap<String, Object> updateDisbursement(Map<String, Object> requestBody){
        return record.updateDisbursement(requestBody);
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