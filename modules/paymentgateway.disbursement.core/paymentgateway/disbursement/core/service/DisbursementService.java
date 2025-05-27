package paymentgateway.disbursement.core;

import java.util.*;

import vmj.routing.route.VMJExchange;

public interface DisbursementService {
    int callback(Map<String, Object> requestBody);
    Disbursement createDisbursement(Map<String, Object> requestBody);
    Disbursement createDisbursement(Map<String, Object> requestBody, Map<String, Object> response);
    HashMap<String, Object> getDisbursement(String id);
    List<HashMap<String, Object>> getAllDisbursement(Map<String, String> queryParams);
    List<HashMap<String, Object>> deleteDisbursement(Map<String, Object> requestBody);
    HashMap<String, Object> updateDisbursement(Map<String, Object> requestBody);
    List<HashMap<String, Object>> transformListToHashMap(List<Disbursement> List);
    Map<String, Object> sendTransaction(Map<String, Object> requestBody);
    HashMap<String, Object> getDisbursementById(String id);
    String validateRequiredStringField(Map<String, Object> requestBody, String key);
}