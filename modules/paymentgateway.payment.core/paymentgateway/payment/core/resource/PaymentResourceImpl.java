package paymentgateway.payment.core;

import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import paymentgateway.payment.PaymentFactory;
import vmj.auth.annotations.Restricted;
import vmj.routing.route.exceptions.*;

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

public class PaymentResourceImpl extends PaymentResourceComponent {
	protected PaymentResourceComponent record;

	public Payment createPayment(VMJExchange vmjExchange, int id) {
		String vendorName = (String) vmjExchange.getRequestBodyForm("vendor_name");
		double amount = Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount"));
		Payment transaction = PaymentFactory.createPayment("paymentgateway.payment.core.PaymentImpl",
				id,
				vendorName,
				amount,
				"PENDING");
		sendTransaction();
		PaymentRepository.saveObject(transaction);
		return transaction;
	}

	private void sendTransaction() {
		// to do implement this in deltas
	}
	
	protected Map<String, Object> checkPaymentStatus(VMJExchange vmjExchange) {
		String vendorName = (String) vmjExchange.getRequestBodyForm("vendor_name");
		String Id = (String) vmjExchange.getRequestBodyForm("id");

		Config config = ConfigFactory.createConfig(vendorName, ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
		HttpClient client = HttpClient.newHttpClient();
        String configUrl = config.getProductEnv("PaymentDetail");
        configUrl = config.getPaymentDetailEndpoint(configUrl, Id);
        HttpRequest request = (config.getBuilder(HttpRequest.newBuilder(),config.getHeaderParams()))
				.uri(URI.create(configUrl))
				.GET()
				.build();
        Map<String, Object> responseMap = new HashMap<>();
		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			System.out.println("rawResponse " + rawResponse);
            responseMap = config.getPaymentStatusResponse(rawResponse, Id);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return responseMap;
	}

	@Route(url = "call/paymentstatus")
	public Map<String, Object> paymentStatus(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")){
			return this.checkPaymentStatus(vmjExchange);
		}
		throw new NotFoundException("Route tidak ditemukan");
	}
	
	@Route(url = "call/callback")
	public int callback(VMJExchange vmjExchange) {
		String[] vendors = {"Midtrans","Oy", "Flip"};
		int statusCode = 200;
		for (String vendor : vendors) {
		    try {
		         Config config = ConfigFactory.createConfig(vendor, ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
		         Map<String, Object> requestMap = config.getCallbackRequestBody(vmjExchange);
	
		         String idStr = (String) requestMap.get("id");
		         String status = (String) requestMap.get("status");

				 HttpClient client = HttpClient.newHttpClient();
				 String configUrl = "http://localhost:443/call/receivecallback";
				 String requestString = config.getRequestString(requestMap);
				 HttpRequest request = (config.getBuilder(HttpRequest.newBuilder(),config.getHeaderParams()))
				 					.uri(URI.create(configUrl))
				 					.POST(HttpRequest.BodyPublishers.ofString(requestString))
				 					.build();

				 Map<String, Object> responseMap = new HashMap<>();
				 try {
				 	HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
				 	String rawResponse = response.body().toString();
				 	System.out.println("rawResponse " + rawResponse);
				 } catch (Exception e) {
					statusCode = 500;
				 	e.printStackTrace();
				 }
		    } catch (Exception e) {
				statusCode = 500;
		     	e.printStackTrace();
		    }
		}
		return statusCode;
	}

	@Route(url = "call/payment/list")
	public List<HashMap<String,Object>> getAll(VMJExchange vmjExchange) {
		String name = (String) vmjExchange.getRequestBodyForm("table_name");
		List<Payment> paymentVariation = PaymentRepository.getAllObject(name);
		return transformListToHashMap(paymentVariation);
	}

	public List<HashMap<String,Object>> getAll(String name) {
		List<Payment> paymentVariation = PaymentRepository.getAllObject(name);
		return transformListToHashMap(paymentVariation);
	}

	public List<HashMap<String,Object>> transformListToHashMap(List<Payment> List){
		List<HashMap<String,Object>> resultList = new ArrayList<HashMap<String,Object>>();
		for(int i = 0; i < List.size(); i++) {
			resultList.add(List.get(i).toHashMap());
		}
		return resultList;
	}

	public void deletePayment(VMJExchange vmjExchange){
		System.out.println("hello");
	}
}
