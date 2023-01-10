package paymentgateway.payment.core;
import java.util.*;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import paymentgateway.payment.PaymentFactory;
import prices.auth.vmj.annotations.Restricted;
//add other required packages

public class PaymentResourceImpl extends PaymentResourceComponent{
	protected PaymentResourceComponent record;

    public Payment createPayment(HashMap<String,Object> vmjExchange){
		String idTransaction = (String) vmjExchange.get("idTransaction");
		int amount = (int) vmjExchange.get("amount");
		
		Payment transaction = 
				PaymentFactory.createPayment("paymentgateway.payment.core.PaymentImpl", idTransaction, amount);
		sendTransaction();
		return transaction;
	}
    
    private void sendTransaction() {
		// to do implement this in deltas
	}
}
