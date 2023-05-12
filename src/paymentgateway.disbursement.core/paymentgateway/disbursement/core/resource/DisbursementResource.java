package paymentgateway.disbursement.core;

import java.util.*;

import vmj.routing.route.VMJExchange;

public interface DisbursementResource {
    Disbursement createDisbursement(VMJExchange vmjExchange, String userId);

    // HashMap<String, Object> updateFundDisbursement(VMJExchange vmjExchange);

    // HashMap<String, Object> getFundDisbursement(VMJExchange vmjExchange);

    // List<HashMap<String, Object>> getAllFundDisbursement(VMJExchange
    // vmjExchange);

    // List<HashMap<String, Object>> deleteFundDisbursement(VMJExchange
    // vmjExchange);
}
