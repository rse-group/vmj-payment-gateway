package paymentgateway.payment.core;
import java.util.*;

import vmj.hibernate.integrator.RepositoryUtil;
import vmj.routing.route.VMJExchange;
//add other required packages

import paymentgateway.payment.core.Payment;
public abstract class PaymentServiceComponent implements PaymentService{
	protected RepositoryUtil<Payment> PaymentRepository;

    public PaymentServiceComponent(){
        this.PaymentRepository = new RepositoryUtil<Payment>(paymentgateway.payment.core.PaymentComponent.class);
    }
    
    public abstract Payment createPayment(Map<String, Object> requestBody, String id, String status);
    public abstract Payment createPayment(CreatePaymentRequestBody requestBody);
    public abstract HashMap<String, Object> getPayment(GetPaymentRequestBody requestBody);
    public abstract List<HashMap<String, Object>> getAllPayment(GetAllPaymentRequestBody requestBody);
    public abstract List<HashMap<String, Object>> deletePayment(DeletePaymentRequestBody requestBody);
    public abstract HashMap<String, Object> updatePayment(UpdatePaymentRequestBody requestBody);
    public abstract List<HashMap<String, Object>> transformListToHashMap(List<Payment> List);
    public abstract Map<String, Object> sendTransaction(Map<String, Object> requestBody);
    public abstract Map<String, Object> checkPaymentStatus(CheckPaymentStatusRequestBody requestBody);
    public abstract HashMap<String, Object> getPaymentById(String id);
    public abstract void callback(VMJExchange vmjExchange);
}
