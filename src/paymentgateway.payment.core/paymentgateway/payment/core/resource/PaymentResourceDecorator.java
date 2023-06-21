package paymentgateway.payment.core;
import java.util.*;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

public abstract class PaymentResourceDecorator extends PaymentResourceComponent{
	protected PaymentResourceComponent record;

    public PaymentResourceDecorator(PaymentResourceComponent record) {
        this.record = record;
    }

    public Payment createPayment(VMJExchange vmjExchange, int id, String productName){
		return record.createPayment(vmjExchange, id, productName);
	}

    public  List<HashMap<String,Object>> getAll(VMJExchange vmjExchange){
        return record.getAll(vmjExchange);
    }
    public void deletePayment(VMJExchange vmjExchange){
        record.deletePayment(vmjExchange);
    }
}
