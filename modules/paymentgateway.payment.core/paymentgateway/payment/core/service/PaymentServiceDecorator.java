package paymentgateway.payment.core;
import java.util.*;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

public abstract class PaymentServiceDecorator extends PaymentServiceComponent{
	protected PaymentServiceComponent record;

    public PaymentServiceDecorator(PaymentServiceComponent record) {
        this.record = record;
    }

    public Payment createPayment(Map<String, Object> requestBody, String id, String status, String vendorGeneratedId) {
		return record.createPayment(requestBody, id, status, vendorGeneratedId);
	}
    
    public Payment createPayment(Map<String, Object> requestBody){
        return record.createPayment(requestBody);
    }

    public HashMap<String, Object> getPayment(String id){
        return record.getPayment(id);
    }

    public List<HashMap<String, Object>> getAllPayment(){
        return record.getAllPayment();
    }

    public List<HashMap<String, Object>> deletePayment(Map<String, Object> requestBody){
        return record.deletePayment(requestBody);
    }

    public HashMap<String, Object> updatePayment(Map<String, Object> requestBody){
        return record.updatePayment(requestBody);
    }

    public List<HashMap<String, Object>> transformListToHashMap(List<Payment> List){
        return record.transformListToHashMap(List);
    }
    
    public Map<String, Object> sendTransaction(Map<String, Object> requestBody){
        return record.sendTransaction(requestBody);
    }
     
    public Map<String, Object> checkPaymentStatus(String id){
        return record.checkPaymentStatus(id);
    }

    public HashMap<String, Object> getPaymentById(String id){
        return record.getPaymentById(id);
    }
    
    public String validateVendorName(String vendorName) {
        return record.validateVendorName(vendorName);
    }
    
    public double validateAmount(Object amountObject) {
        return record.validateAmount(amountObject);
    }
    
    public String validateId(String id) {
        return record.validateId(id);
    }

    public void callback(VMJExchange vmjExchange) {
        record.callback(vmjExchange);
    }
}
