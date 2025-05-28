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
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentResourceDecorator;
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

	
	@Route(url="call/invoice", method = RequestMethod.POST)
	public HashMap<String,Object> payment(VMJExchange vmjExchange) {
		Map<String, Object> requestBody = vmjExchange.getPayload(); 
		Payment result = paymentServiceImpl.createPayment(requestBody);
		return result.toHashMap();
	}

	@Route(url = "call/invoice/vendorname", method = RequestMethod.GET)
	public List<PaymentImpl> getByVendorName(VMJExchange vmjExchange) {
		Map<String, String> queryParams = vmjExchange.queryToMap();
		return paymentServiceImpl.getByVendorName(queryParams);
	}

	@Route(url = "call/invoice/detail", method = RequestMethod.GET)
	public HashMap<String, Object> getById(VMJExchange vmjExchange) {
		Map<String, String> queryParams = vmjExchange.queryToMap();
		return paymentServiceImpl.getById(queryParams);
	}
}

