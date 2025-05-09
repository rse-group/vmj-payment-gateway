package paymentgateway.payment.core;
import java.util.*;

import vmj.routing.route.VMJExchange;

public interface PaymentService {
	Payment createPayment(Map<String, Object> requestBody, String id, String status);	
	Payment createPayment(CreatePaymentRequestBody requestBody);
    HashMap<String, Object> getPayment(GetPaymentRequestBody requestBody);
    List<HashMap<String, Object>> getAllPayment(GetAllPaymentRequestBody requestBody);
    List<HashMap<String, Object>> deletePayment(DeletePaymentRequestBody requestBody);
    HashMap<String, Object> updatePayment(UpdatePaymentRequestBody requestBody);
    List<HashMap<String, Object>> transformListToHashMap(List<Payment> List);
    Map<String, Object> sendTransaction(Map<String, Object> requestBody);
    Map<String, Object> checkPaymentStatus(CheckPaymentStatusRequestBody requestBody);
    void callback(VMJExchange vmjExchange);
}
