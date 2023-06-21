package paymentgateway.payment.core;
import java.util.*;

import vmj.routing.route.VMJExchange;

public interface PaymentResource {
	Payment createPayment(VMJExchange vmjExchange, int id, String productName);
	List<HashMap<String,Object>> getAll(VMJExchange vmjExchange);
	void deletePayment(VMJExchange vmjExchange);
}
