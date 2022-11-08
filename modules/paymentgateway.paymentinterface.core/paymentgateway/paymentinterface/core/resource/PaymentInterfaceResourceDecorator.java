package paymentgateway.paymentinterface.core;
import java.util.*;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

public abstract class PaymentInterfaceResourceDecorator extends PaymentInterfaceResourceComponent{
	protected PaymentInterfaceResourceComponent record;

    public PaymentInterfaceResourceDecorator(PaymentInterfaceResourceComponent record) {
        this.record = record;
    }

    public PaymentInterface createTransaction(String apiKey, int amount, String idTransaction, String apiEndpoint) {
    	return record.createTransaction(apiKey, amount, idTransaction, apiEndpoint);
    }
}
