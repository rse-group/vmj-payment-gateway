package paymentgateway.paymentinterface.core;
import java.util.*;

import vmj.routing.route.VMJExchange;

public interface PaymentInterfaceResource {
	public PaymentInterface createTransaction(int amount, String idTransaction);
}
