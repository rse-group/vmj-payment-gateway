package paymentgateway.disbursement.core;

import java.util.*;

import vmj.routing.route.VMJExchange;

public interface DisbursementResource {
	int callback(VMJExchange vmjExchange);
	HashMap<String, Object> getDisbursement(VMJExchange vmjExchange);
    List<HashMap<String, Object>> getAllDisbursement(VMJExchange vmjExchange);
    List<HashMap<String, Object>> deleteDisbursement(VMJExchange vmjExchange);
    HashMap<String, Object> updateDisbursement(VMJExchange vmjExchange);
    HashMap<String, Object> disbursement(VMJExchange vmjExchange);
}