package paymentgateway.disbursement.core;

import java.util.*;

import vmj.routing.route.VMJExchange;

public interface DisbursementResource {
    Disbursement createDisbursement(VMJExchange vmjExchange,int id, int userId);
    HashMap<String, Object> getDisbursement(VMJExchange vmjExchange);
    List<HashMap<String, Object>> getAllDisbursement(VMJExchange vmjExchange);
    List<HashMap<String,Object>> deleteDisbursement(VMJExchange vmjExchange);
    HashMap<String, Object> updateDisbursement(VMJExchange vmjExchange);
    List<HashMap<String,Object>> transformListToHashMap(List<Disbursement> List);
    GetAllDisbursementResponse getAllDataFromAPI(String name);
    MoneyTransferResponse sendTransaction(VMJExchange vmjExchange, String serviceName);
    String getParamsUrlEncoded(VMJExchange vmjExchange);
    HashMap<String, Object> getDisbursementById(int id);

}
