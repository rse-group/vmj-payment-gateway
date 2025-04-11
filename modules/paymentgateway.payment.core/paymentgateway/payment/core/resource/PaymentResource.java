package paymentgateway.payment.core;
import java.util.*;

import vmj.routing.route.VMJExchange;
import vmj.routing.route.VMJExchangeNew;

import paymentgateway.config.core.CreatePaymentRequestBody;
import paymentgateway.config.core.CheckPaymentStatusRequestBody;
import paymentgateway.config.core.GetPaymentRequestBody;
import paymentgateway.config.core.GetAllPaymentRequestBody;
import paymentgateway.config.core.UpdatePaymentRequestBody;
import paymentgateway.config.core.DeletePaymentRequestBody;

public interface PaymentResource {
	int callback(VMJExchange vmjExchange);
	HashMap<String, Object> getPayment(VMJExchange<GetPaymentRequestBody> vmjExchange);
    List<HashMap<String, Object>> getAllPayment(VMJExchange<GetAllPaymentRequestBody> vmjExchange);
    List<HashMap<String, Object>> deletePayment(VMJExchange<DeletePaymentRequestBody> vmjExchange);
    HashMap<String, Object> updatePayment(VMJExchange<UpdatePaymentRequestBody> vmjExchange);
    HashMap<String, Object> payment(VMJExchange<CreatePaymentRequestBody> vmjExchange);
}
