package paymentgateway.client.core;

import java.util.*;
import vmj.routing.route.VMJExchange;

//add other required packages

public abstract class ClientDecorator extends ClientComponent{

	public void sendTransfer(VMJExchange request) {}

	public Map<String, Object> buildPaymentGatewayRequest(VMJExchange request) {
		return new HashMap<String, Object>();
	}

}
