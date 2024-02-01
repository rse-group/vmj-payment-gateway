package paymentgateway.payment.core;
import java.util.*;

import vmj.routing.route.VMJExchange;

public interface PaymentResource {
	Payment createPayment(VMJExchange vmjExchange, int id, String productName);
	List<HashMap<String,Object>> getAll(VMJExchange vmjExchange);
//	Map<String, Object>  processRequestMap(VMJExchange vmjExchange, String productName, String serviceName);
	void deletePayment(VMJExchange vmjExchange);
}
