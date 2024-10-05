package paymentgateway.disbursement.core;

import java.util.*;

import vmj.routing.route.VMJExchange;

public interface DisbursementService {
    Disbursement createDisbursement(Map<String, Object> requestBody);
    Disbursement createDisbursement(Map<String, Object> requestBody, Map<String, Object> response);
    HashMap<String, Object> getDisbursement(Map<String, Object> requestBody);
    List<HashMap<String, Object>> getAllDisbursement(Map<String, Object> requestBody);
    List<HashMap<String, Object>> deleteDisbursement(Map<String, Object> requestBody);
    HashMap<String, Object> updateDisbursement(Map<String, Object> requestBody);
    List<HashMap<String, Object>> transformListToHashMap(List<Disbursement> List);
    Map<String, Object> sendTransaction(Map<String, Object> requestBody);
    HashMap<String, Object> getDisbursementById(int id);
}