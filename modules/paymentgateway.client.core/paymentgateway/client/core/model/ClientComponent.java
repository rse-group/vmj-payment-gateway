package paymentgateway.client.core;

import java.util.*;
import vmj.routing.route.VMJExchange;

public abstract class ClientComponent implements Client{
	public ClientComponent() {} 
 
	public abstract void sendTransfer(VMJExchange request);

	public abstract Map<String, Object> buildPaymentGatewayRequest(VMJExchange request);
}
