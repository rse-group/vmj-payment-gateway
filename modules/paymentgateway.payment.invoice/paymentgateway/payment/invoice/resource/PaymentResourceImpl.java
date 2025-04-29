package paymentgateway.payment.invoice;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import vmj.routing.route.RequestMethod;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;
import paymentgateway.payment.core.CreatePaymentRequestBody;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentResourceDecorator;
import paymentgateway.payment.core.PaymentImpl;
import paymentgateway.payment.core.PaymentResourceComponent;
import paymentgateway.payment.core.PaymentServiceComponent;
import paymentgateway.payment.PaymentFactory;

public class PaymentResourceImpl extends PaymentResourceDecorator {
	
	protected String apiKey;
	protected String apiEndpoint;
	private PaymentServiceImpl paymentServiceImpl;

	public PaymentResourceImpl(PaymentResourceComponent record, PaymentServiceComponent recordService) {
		super(record);
		paymentServiceImpl = new PaymentServiceImpl(recordService);
	}

	
	@Route(url="call/invoice", method = RequestMethod.POST, requestBodyClass = CreateInvoiceRequestBody.class)
	public HashMap<String,Object> payment(VMJExchange<CreatePaymentRequestBody> vmjExchange) {
		CreatePaymentRequestBody requestBody = vmjExchange.getParsedPayload(); 
		Payment result = paymentServiceImpl.createPayment(requestBody);
		return result.toHashMap();
	}
}

