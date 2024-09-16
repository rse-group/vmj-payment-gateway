package paymentgateway.disbursement.core;

import java.util.*;

import vmj.routing.route.VMJExchange;

public interface DisbursementService {
    Disbursement createDisbursement(Map<String, Object> requestBody);
    Disbursement createDisbursement(Map<String, Object> requestBody, Map<String, Object> response);
    HashMap<String, Object> getDisbursement(VMJExchange vmjExchange);
    List<HashMap<String, Object>> getAllDisbursement(VMJExchange vmjExchange);
    List<HashMap<String, Object>> deleteDisbursement(VMJExchange vmjExchange);
    HashMap<String, Object> updateDisbursement(VMJExchange vmjExchange);
    List<HashMap<String, Object>> transformListToHashMap(List<Disbursement> List);
    Map<String, Object> sendTransaction(Map<String, Object> requestBody);
    HashMap<String, Object> getDisbursementById(int id);
}