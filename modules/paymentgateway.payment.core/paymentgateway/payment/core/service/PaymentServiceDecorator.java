package paymentgateway.payment.core;
import java.util.*;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

public abstract class PaymentServiceDecorator extends PaymentServiceComponent{
	protected PaymentServiceComponent record;

    public PaymentServiceDecorator(PaymentServiceComponent record) {
        this.record = record;
    }

    public Payment createPayment(CreatePaymentRequestBody requestBody, int id){
		return record.createPayment(requestBody, id);
	}
    
    public Payment createPayment(CreatePaymentRequestBody requestBody){
        return record.createPayment(requestBody);
    }

    public HashMap<String, Object> getPayment(GetPaymentRequestBody requestBody){
        return record.getPayment(requestBody);
    }

    public List<HashMap<String, Object>> getAllPayment(GetAllPaymentRequestBody requestBody){
        return record.getAllPayment(requestBody);
    }

    public List<HashMap<String, Object>> deletePayment(DeletePaymentRequestBody requestBody){
        return record.deletePayment(requestBody);
    }

    public HashMap<String, Object> updatePayment(UpdatePaymentRequestBody requestBody){
        return record.updatePayment(requestBody);
    }

    public List<HashMap<String, Object>> transformListToHashMap(List<Payment> List){
        return record.transformListToHashMap(List);
    }
    
    public Map<String, Object> sendTransaction(CreatePaymentRequestBody requestBody){
        return record.sendTransaction(requestBody);
    }
     
    public Map<String, Object> checkPaymentStatus(CheckPaymentStatusRequestBody requestBody){
        return record.checkPaymentStatus(requestBody);
    }

    public HashMap<String, Object> getPaymentById(int id){
        return record.getPaymentById(id);
    }
}
