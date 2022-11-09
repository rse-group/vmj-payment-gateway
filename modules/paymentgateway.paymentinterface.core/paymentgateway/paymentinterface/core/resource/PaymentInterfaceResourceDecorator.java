package paymentgateway.paymentinterface.core;
import java.util.*;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

public abstract class PaymentInterfaceResourceDecorator extends PaymentInterfaceResourceComponent{
	protected PaymentInterfaceResourceComponent record;

    public PaymentInterfaceResourceDecorator(PaymentInterfaceResourceComponent record) {
        this.record = record;
    }

    public PaymentInterface createTransaction(int amount, String idTransaction) {
    	return record.createTransaction(amount, idTransaction);
    }
}
