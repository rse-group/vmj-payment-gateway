package paymentgateway.paymentchannel.core;
import java.util.*;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import paymentgateway.paymentchannel.PaymentChannelFactory;
import prices.auth.vmj.annotations.Restricted;
//add other required packages

public class PaymentChannelResourceImpl extends PaymentChannelResourceComponent{
	protected PaymentChannelResourceComponent record;

    public PaymentChannel createChannel(HashMap<String,Object> pgExchange) {
    	int amount = (int) pgExchange.get("amount");
    	String idTransaction = (String) pgExchange.get("idTransaction");
    	
    	PaymentChannel channel = 
    			PaymentChannelFactory.createPaymentChannel("paymentgateway.paymentchannel.core.PaymentChannelImpl", amount, idTransaction);
    	
    	return channel;
    }
}
