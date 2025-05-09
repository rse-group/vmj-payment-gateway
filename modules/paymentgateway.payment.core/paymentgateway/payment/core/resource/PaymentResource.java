package paymentgateway.payment.core;

import java.util.*;

import vmj.routing.route.VMJExchange;

public interface PaymentResource {
    int callback(VMJExchange vmjExchange);

    HashMap<String, Object> getPayment(VMJExchange vmjExchange);

    List<HashMap<String, Object>> getAllPayment(VMJExchange vmjExchange);

    List<HashMap<String, Object>> deletePayment(VMJExchange<DeletePaymentRequestBody> vmjExchange);

    HashMap<String, Object> updatePayment(VMJExchange<UpdatePaymentRequestBody> vmjExchange);

    HashMap<String, Object> payment(VMJExchange<CreatePaymentRequestBody> vmjExchange);
}
