package paymentgateway.fundtransfer.core;
import java.util.*;

import vmj.routing.route.VMJExchange;

public interface FundTransferResource {
    List<HashMap<String,Object>> saveFundTransfer(VMJExchange vmjExchange);
    HashMap<String, Object> updateFundTransfer(VMJExchange vmjExchange);
    HashMap<String, Object> getFundTransfer(VMJExchange vmjExchange);
    List<HashMap<String,Object>> getAllFundTransfer(VMJExchange vmjExchange);
    List<HashMap<String,Object>> deleteFundTransfer(VMJExchange vmjExchange);
}
