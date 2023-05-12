package paymentgateway.payment.core;
import java.util.*;

import vmj.hibernate.integrator.RepositoryUtil;
import vmj.routing.route.VMJExchange;
//add other required packages

import paymentgateway.payment.core.Payment;
public abstract class PaymentResourceComponent implements PaymentResource{
	protected RepositoryUtil<Payment> PaymentRepository;

    public PaymentResourceComponent(){
        this.PaymentRepository = new RepositoryUtil<Payment>(paymentgateway.payment.core.PaymentComponent.class);
    }
    
    public abstract Payment createPayment(HashMap<String,Object> vmjExchange);
//    public abstract HashMap<String,Object> getProductEnv();
}
