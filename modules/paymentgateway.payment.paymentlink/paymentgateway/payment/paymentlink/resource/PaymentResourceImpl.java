package paymentgateway.payment.paymentlink;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import paymentgateway.payment.PaymentFactory;
import paymentgateway.payment.PaymentResourceFactory;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentResourceDecorator;
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


	@Route(url = "call/payment/paymentlink")
	public HashMap<String, Object> payment(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")){
			Map<String, Object> requestBody = vmjExchange.getPayload(); 
			Payment result = paymentServiceImpl.createPayment(requestBody);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
	}


	@Route(url = "call/paymentlink/vendorname")
	public List<PaymentLinkImpl> getByVendorName(VMJExchange vmjExchange) {
		Map<String, Object> requestBody = vmjExchange.getPayload(); 
		return paymentServiceImpl.getByVendorName(requestBody);
	}

	@Route(url = "call/paymentlink/detail")
	public HashMap<String, Object> getById(VMJExchange vmjExchange) {
		Map<String, Object> requestBody = vmjExchange.getPayload(); 
		return paymentServiceImpl.getById(requestBody);
	}


	@Route(url = "call/paymentlink/delete")
	public String deletePaymentLinkById(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS"))
			return null;
		
		Map<String, Object> requestBody = vmjExchange.getPayload(); 
		return paymentServiceImpl.deletePaymentLinkById(requestBody);
	}

	@Route(url = "call/paymentlink/deleted")
	public List<HashMap<String, Object>> deletePaymentLinkByIdTransaction(VMJExchange vmjExchange) {
		Map<String, Object> requestBody = vmjExchange.getPayload(); 
		return paymentServiceImpl.deletePaymentLinkByIdTransaction(requestBody);
	}
}
