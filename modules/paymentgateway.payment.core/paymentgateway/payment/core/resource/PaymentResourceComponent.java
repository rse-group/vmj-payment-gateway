package paymentgateway.payment.core;
import java.util.*;

import vmj.hibernate.integrator.RepositoryUtil;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.VMJExchangeNew;

import paymentgateway.config.core.CreatePaymentRequestBody;
import paymentgateway.config.core.CheckPaymentStatusRequestBody;
import paymentgateway.config.core.GetPaymentRequestBody;
import paymentgateway.config.core.GetAllPaymentRequestBody;
import paymentgateway.config.core.UpdatePaymentRequestBody;
import paymentgateway.config.core.DeletePaymentRequestBody;
//add other required packages

import paymentgateway.payment.core.Payment;
public abstract class PaymentResourceComponent implements PaymentResource{

    public PaymentResourceComponent(){}
    
    public abstract int callback(VMJExchange vmjExchange);
    public abstract HashMap<String, Object> getPayment(VMJExchange<GetPaymentRequestBody> vmjExchange);
    public abstract List<HashMap<String, Object>> getAllPayment(VMJExchange<GetAllPaymentRequestBody> vmjExchange);
    public abstract List<HashMap<String, Object>> deletePayment(VMJExchange<DeletePaymentRequestBody> vmjExchange);
    public abstract HashMap<String, Object> updatePayment(VMJExchange<UpdatePaymentRequestBody> vmjExchange);
    public abstract HashMap<String, Object> payment(VMJExchange<CreatePaymentRequestBody> vmjExchange);
}
