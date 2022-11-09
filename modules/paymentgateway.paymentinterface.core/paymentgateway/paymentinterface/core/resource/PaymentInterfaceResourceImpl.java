package paymentgateway.paymentinterface.core;
import java.util.*;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import paymentgateway.paymentinterface.PaymentInterfaceFactory;
import prices.auth.vmj.annotations.Restricted;
//add other required packages

public class PaymentInterfaceResourceImpl extends PaymentInterfaceResourceComponent{
	protected PaymentInterfaceResourceComponent record;

	public PaymentInterface createTransaction(int amount, String idTransaction) {
		PaymentInterface transaction = 
				PaymentInterfaceFactory.createPaymentInterface("paymentgateway.paymentinterface.core.PaymentInterfaceImpl", amount, idTransaction);
		sendTransaction();
		return transaction;
	}
	
	private void sendTransaction() {
		// to do implement this in deltas
	}
}
