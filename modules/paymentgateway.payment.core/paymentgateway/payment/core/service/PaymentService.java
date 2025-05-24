package paymentgateway.payment.core;
import java.util.*;

import vmj.routing.route.VMJExchange;

public interface PaymentService {
	Payment createPayment(Map<String, Object> requestBody, String id, String status, String vendorGeneratedId);	
	Payment createPayment(Map<String, Object> requestBody);
    HashMap<String, Object> getPayment(String id);
    List<HashMap<String, Object>> getAllPayment();
    List<HashMap<String, Object>> deletePayment(Map<String, Object> requestBody);
    HashMap<String, Object> updatePayment(Map<String, Object> requestBody);
    List<HashMap<String, Object>> transformListToHashMap(List<Payment> List);
    Map<String, Object> sendTransaction(Map<String, Object> requestBody);
    Map<String, Object> checkPaymentStatus(String id);
    String validateVendorName(String vendorName);
    double validateAmount(Object amountObject);
    String validateId(String id);
    void callback(VMJExchange vmjExchange);
}
