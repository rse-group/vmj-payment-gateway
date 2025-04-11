package paymentgateway.payment.core;
import java.util.*;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.VMJExchangeNew;

import paymentgateway.config.core.CreatePaymentRequestBody;
import paymentgateway.config.core.CheckPaymentStatusRequestBody;
import paymentgateway.config.core.GetPaymentRequestBody;
import paymentgateway.config.core.GetAllPaymentRequestBody;
import paymentgateway.config.core.UpdatePaymentRequestBody;
import paymentgateway.config.core.DeletePaymentRequestBody;

public abstract class PaymentResourceDecorator extends PaymentResourceComponent{
	protected PaymentResourceComponent record;

    public PaymentResourceDecorator(PaymentResourceComponent record) {
        this.record = record;
    }

    public int callback(VMJExchange vmjExchange) {
        return record.callback(vmjExchange);
    }
    
    public HashMap<String, Object> getPayment(VMJExchange<GetPaymentRequestBody> vmjExchange) {
        return record.getPayment(vmjExchange);
    }

    public List<HashMap<String, Object>> getAllPayment(VMJExchange<GetAllPaymentRequestBody> vmjExchange) {
        return record.getAllPayment(vmjExchange);
    }

    public List<HashMap<String, Object>> deletePayment(VMJExchange<DeletePaymentRequestBody> vmjExchange) {
        return record.deletePayment(vmjExchange);
    }

    public HashMap<String, Object> updatePayment(VMJExchange<UpdatePaymentRequestBody> vmjExchange) {
        return record.updatePayment(vmjExchange);
    }

    public HashMap<String, Object> payment(VMJExchange<CreatePaymentRequestBody> vmjExchange) {
        return record.payment(vmjExchange);
    }
}
