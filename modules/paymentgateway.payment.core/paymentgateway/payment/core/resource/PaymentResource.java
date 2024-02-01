package paymentgateway.payment.core;
import java.util.*;

import vmj.routing.route.VMJExchange;

public interface PaymentResource {
	public Payment createPayment(HashMap<String,Object> vmjExchange);
}
