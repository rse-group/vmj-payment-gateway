package paymentgateway.disbursement.core;

import java.util.*;

import vmj.routing.route.VMJExchange;

public interface DisbursementService {
    int callback(Map<String, Object> requestBody);
    Disbursement createDisbursement(Map<String, Object> requestBody);
    Disbursement createDisbursement(Map<String, Object> requestBody, Map<String, Object> response);
    HashMap<String, Object> getDisbursement(String id);
    List<HashMap<String, Object>> getAllDisbursement();
    List<HashMap<String, Object>> getAllDisbursement(String tableName);
    List<HashMap<String, Object>> deleteDisbursement(Map<String, Object> requestBody);
    HashMap<String, Object> updateDisbursement(Map<String, Object> requestBody);
    List<HashMap<String, Object>> transformListToHashMap(List<Disbursement> List);
    Map<String, Object> sendTransaction(Map<String, Object> requestBody);
    HashMap<String, Object> getDisbursementById(String id);
    HashMap<String, Object> findById(List<HashMap<String, Object>> disbursements, String id);
    String validateVendorName(String vendorName);
    double validateAmount(Object amountObject);
    String validateId(Object idObject);
}