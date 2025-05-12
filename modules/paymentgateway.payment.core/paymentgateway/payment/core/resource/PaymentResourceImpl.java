package paymentgateway.payment.core;

import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import vmj.routing.route.RequestMethod;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import paymentgateway.payment.PaymentFactory;
import vmj.auth.annotations.Restricted;
import vmj.routing.route.exceptions.*;

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

public class PaymentResourceImpl extends PaymentResourceComponent {
	protected PaymentResourceComponent record;

	private PaymentServiceImpl paymentServiceImpl = new PaymentServiceImpl();

	@Route(url = "call/payment", method = RequestMethod.POST)
	public HashMap<String, Object> payment(VMJExchange vmjExchange) {
		Map<String, Object> requestBody = vmjExchange.getPayload();
		Payment result = paymentServiceImpl.createPayment(requestBody);
		return result.toHashMap();
	}

	@Route(url = "call/paymentstatus", method = RequestMethod.POST)
	public Map<String, Object> paymentStatus(VMJExchange vmjExchange) {
		Map<String, Object> requestBody = vmjExchange.getPayload();
		return paymentServiceImpl.checkPaymentStatus(requestBody);
	}

	@Route(url = "call/payment/callback")
	public int callback(VMJExchange vmjExchange) {
		paymentServiceImpl.callback(vmjExchange);
		return 200;
	}

	@Route(url = "call/payment/list", method = RequestMethod.GET)
	public List<HashMap<String, Object>> getAllPayment(VMJExchange vmjExchange) {
		return paymentServiceImpl.getAllPayment();
	}

	@Route(url = "call/payment/detail", method = RequestMethod.GET)
	public HashMap<String, Object> getPayment(VMJExchange vmjExchange) {
		Map<String, String> queryParamsMap = vmjExchange.queryToMap();
		String id = queryParamsMap.get("id");
		return paymentServiceImpl.getPayment(id);
	}

	@Route(url = "call/payment/delete", method = RequestMethod.DELETE)
	public List<HashMap<String, Object>> deletePayment(VMJExchange vmjExchange) {
		Map<String, Object> requestBody = vmjExchange.getPayload();
		System.out.println(requestBody);
		return paymentServiceImpl.deletePayment(requestBody);
	}

	@Route(url = "call/payment/update", method = RequestMethod.PUT)
	public HashMap<String, Object> updatePayment(VMJExchange vmjExchange) {
		Map<String, Object> requestBody = vmjExchange.getPayload();
		return paymentServiceImpl.updatePayment(requestBody);
	}

}
