package paymentgateway.disbursement.core;

import java.util.*;

import vmj.routing.route.VMJExchange;

public interface DisbursemeantResource {
    Disbursement createDisbursement(VMJExchange vmjExchange);
    Disbursement createDisbursement(VMJExchange vmjExchange, Map<String, Object> response);
    HashMap<String, Object> getDisbursement(VMJExchange vmjExchange);
    List<HashMap<String, Object>> getAllDisbursement(VMJExchange vmjExchange);
    List<HashMap<String, Object>> deleteDisbursement(VMJExchange vmjExchange);
    HashMap<String, Object> updateDisbursement(VMJExchange vmjExchange);
    List<HashMap<String, Object>> transformListToHashMap(List<Disbursement> List);
    Map<String, Object> sendTransaction(VMJExchange vmjExchange);
    HashMap<String, Object> getDisbursementById(int id);
}