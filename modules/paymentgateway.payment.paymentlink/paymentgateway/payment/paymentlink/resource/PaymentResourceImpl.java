package paymentgateway.payment.paymentlink;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import vmj.routing.route.RequestMethod;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import paymentgateway.payment.PaymentFactory;
import paymentgateway.payment.PaymentResourceFactory;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentResourceDecorator;
import paymentgateway.payment.core.PaymentResourceComponent;
import paymentgateway.payment.core.PaymentServiceComponent;
import paymentgateway.config.core.CreatePaymentRequestBody;
import paymentgateway.config.core.CreatePaymentLinkRequestBody;
import paymentgateway.config.core.GetPaymentLinksByVendorNameRequestBody;
import paymentgateway.config.core.GetPaymentLinkByIdRequestBody;
import paymentgateway.config.core.DeletePaymentRequestBody;

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

public class PaymentResourceImpl extends PaymentResourceDecorator {
	private PaymentServiceImpl paymentServiceImpl;

	public PaymentResourceImpl(PaymentResourceComponent record, PaymentServiceComponent recordService) {
		super(record);
		paymentServiceImpl = new PaymentServiceImpl(recordService);
	}

	@Route(url = "call/paymentlink", method = RequestMethod.POST, requestBodyClass = CreatePaymentLinkRequestBody.class)
	public HashMap<String, Object> payment(VMJExchange<CreatePaymentRequestBody> vmjExchange) {
		CreatePaymentRequestBody requestBody = vmjExchange.getParsedPayload();
		Payment result = paymentServiceImpl.createPayment(requestBody);
		return result.toHashMap();
	}

	@Route(url = "call/paymentlink/vendorname", method = RequestMethod.GET, requestBodyClass = GetPaymentLinksByVendorNameRequestBody.class)
	public List<PaymentLinkImpl> getByVendorName(VMJExchange<GetPaymentLinksByVendorNameRequestBody> vmjExchange) {
		GetPaymentLinksByVendorNameRequestBody requestBody = vmjExchange.getParsedPayload();
		return paymentServiceImpl.getByVendorName(requestBody);
	}

	@Route(url = "call/paymentlink/detail", method = RequestMethod.GET, requestBodyClass = GetPaymentLinkByIdRequestBody.class)
	public HashMap<String, Object> getById(VMJExchange<GetPaymentLinkByIdRequestBody> vmjExchange) {
		GetPaymentLinkByIdRequestBody requestBody = vmjExchange.getParsedPayload();
		return paymentServiceImpl.getById(requestBody);
	}

	@Route(url = "call/paymentlink/delete", method = RequestMethod.DELETE, requestBodyClass = DeletePaymentRequestBody.class)
	public String deletePaymentLinkById(VMJExchange<DeletePaymentRequestBody> vmjExchange) {
		DeletePaymentRequestBody requestBody = vmjExchange.getParsedPayload();
		return paymentServiceImpl.deletePaymentLinkById(requestBody);
	}

	@Route(url = "call/paymentlink/deleted", method = RequestMethod.DELETE, requestBodyClass = DeletePaymentRequestBody.class)
	public List<HashMap<String, Object>> deletePaymentLinkByIdTransaction(
			VMJExchange<DeletePaymentRequestBody> vmjExchange) {
		DeletePaymentRequestBody requestBody = vmjExchange.getParsedPayload();
		return paymentServiceImpl.deletePaymentLinkByIdTransaction(requestBody);
	}
}
