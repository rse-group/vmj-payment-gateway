package paymentgateway.payment.core;
import java.util.*;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

public abstract class PaymentResourceDecorator extends PaymentResourceComponent{
	protected PaymentResourceComponent record;

    public PaymentResourceDecorator(PaymentResourceComponent record) {
        this.record = record;
    }

    public Payment createPayment(VMJExchange vmjExchange, int id){
		return record.createPayment(vmjExchange, id);
	}

    public  List<HashMap<String,Object>> getAll(VMJExchange vmjExchange){
        return record.getAll(vmjExchange);
    }
//    public Map<String, Object>  processRequestMap(VMJExchange vmjExchange, String productName, String serviceName){
//        return record.processRequestMap(VMJExchange vmjExchange, String productName, String serviceName);
//    }
    public void deletePayment(VMJExchange vmjExchange){
        record.deletePayment(vmjExchange);
    }
}
