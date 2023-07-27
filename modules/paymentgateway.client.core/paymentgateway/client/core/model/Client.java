package paymentgateway.client.core;
import vmj.routing.route.VMJExchange;
import java.util.*;

public interface Client {
	public void sendTransfer(VMJExchange request);
	public Map<String, Object> buildPaymentGatewayRequest(VMJExchange request);
}
