package paymentgateway.payment.paymentrouting;

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

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import paymentgateway.payment.PaymentFactory;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentResourceDecorator;
import paymentgateway.payment.core.PaymentImpl;
import paymentgateway.payment.core.PaymentResourceComponent;
import paymentgateway.payment.core.PaymentServiceComponent;
import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

public class PaymentResourceImpl extends PaymentResourceDecorator {
	private PaymentServiceImpl paymentServiceImpl;

	public PaymentResourceImpl(PaymentResourceComponent record, PaymentServiceComponent recordService) {
		super(record);
		paymentServiceImpl = new PaymentServiceImpl(recordService);
	}

	@Route(url="call/payment/paymentrouting")
	public HashMap<String,Object> payment(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")){
			Map<String, Object> requestBody = vmjExchange.getPayload(); 
			Payment result = paymentServiceImpl.createPayment(requestBody);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
	}

}

