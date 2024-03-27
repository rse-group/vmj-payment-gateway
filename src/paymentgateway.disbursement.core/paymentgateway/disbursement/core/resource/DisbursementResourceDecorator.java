package paymentgateway.disbursement.core;

import java.util.*;

import vmj.routing.route.VMJExchange;

public abstract class DisbursementResourceDecorator extends DisbursementResourceComponent {
    protected DisbursementResourceComponent record;

    public DisbursementResourceDecorator(DisbursementResourceComponent record) {
        this.record = record;
    }

    public Disbursement createDisbursement(VMJExchange vmjExchange,int id, int userId) {
        return record.createDisbursement(vmjExchange, id, userId);
    }

    public HashMap<String, Object> getDisbursement(VMJExchange vmjExchange){
        return record.getDisbursement(vmjExchange);
    }
    public List<HashMap<String, Object>> getAllDisbursement(VMJExchange vmjExchange){
        return record.getAllDisbursement(vmjExchange);
    }
    public List<HashMap<String,Object>> transformListToHashMap(List<Disbursement> List){
        return record.transformListToHashMap(List);
    }
    public List<HashMap<String,Object>> deleteDisbursement(VMJExchange vmjExchange){
        return record.deleteDisbursement(vmjExchange);
    }
    public HashMap<String, Object> updateDisbursement(VMJExchange vmjExchange){
        return record.updateDisbursement(vmjExchange);
    }
    public GetAllDisbursementResponse getAllDataFromAPI(String name){
        return record.getAllDataFromAPI(name);
    }
    public MoneyTransferResponse sendTransaction(VMJExchange vmjExchange, String serviceName){
        return record.sendTransaction(vmjExchange, serviceName);
    }

    public String getParamsUrlEncoded(Map<String, Object> vmjExchange){
        return record.getParamsUrlEncoded(vmjExchange);
    }
    public HashMap<String, Object> getDisbursementById(int id){
        return record.getDisbursementById(id);
    }
}