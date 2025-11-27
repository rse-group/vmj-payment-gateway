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
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentServiceDecorator;
import vmj.hibernate.integrator.RepositoryUtil;
import paymentgateway.payment.core.PaymentServiceComponent;

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

public class PaymentServiceImpl extends PaymentServiceDecorator {
	RepositoryUtil<PaymentLinkImpl> paymentLinkRepository;

	public PaymentServiceImpl(PaymentServiceComponent record) {
		super(record);
		this.paymentLinkRepository = new RepositoryUtil<PaymentLinkImpl>(paymentgateway.payment.paymentlink.PaymentLinkImpl.class);
	}

	public Payment createPayment(Map<String, Object> requestBody) {
		Map<String, Object> response = sendTransaction(requestBody);
		String paymentLink = (String) response.get("url");
		int id = (int) response.get("id");
		Payment transaction = record.createPayment(requestBody, id);
		Payment paymentLinkTransaction =
			PaymentFactory.createPayment("paymentgateway.payment.paymentlink.PaymentLinkImpl",
			transaction,id, paymentLink);
		PaymentRepository.saveObject(paymentLinkTransaction);
		return paymentLinkTransaction;
	}

	public Map<String, Object> sendTransaction(Map<String, Object> requestBody) {
		String vendorName = (String) requestBody.get("vendor_name");

		Config config = ConfigFactory.createConfig(vendorName, ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));

		Gson gson = new Gson();
		Map<String, Object> requestMap = config.getPaymentLinkRequestBody(requestBody);
		int id = ((Integer) requestMap.get("id")).intValue();
		requestMap.remove("id");
		String configUrl = config.getProductEnv("PaymentLink");
		HashMap<String, String> headerParams = config.getHeaderParams();
		System.out.println("configUrl: " + configUrl);
		String requestString = config.getRequestString(requestMap);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = (config.getBuilder(HttpRequest.newBuilder(),headerParams))
				.uri(URI.create(configUrl))
				.POST(HttpRequest.BodyPublishers.ofString(requestString))
				.build();
		Map<String, Object> responseMap = new HashMap<>();
		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			System.out.println("rawResponse " + rawResponse);
			responseMap = config.getPaymentLinkResponse(rawResponse, id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseMap;
	}
	
	public List<PaymentLinkImpl> getByVendorName(Map<String, Object> requestBody) {
		String vendorName = (String) requestBody.get("vendor_name");
		List<PaymentLinkImpl> result = new ArrayList<>();
		List<PaymentLinkImpl> paymentLink = paymentLinkRepository.getAllObject("paymentlink_impl");
		for(PaymentLinkImpl payment : paymentLink){
			if (payment.getVendorName().equals(vendorName)){
				result.add(payment);
			}
		}
		return result;
	}
	
	public HashMap<String, Object> getById(Map<String, Object> requestBody) {
		int id = ((Double) requestBody.get("id")).intValue();
		List<PaymentLinkImpl> paymentLink = paymentLinkRepository.getAllObject("paymentlink_impl");
		for(PaymentLinkImpl payment : paymentLink){
			if (payment.getIdTransaction() == id){
				return payment.toHashMap();
			}
		}
		return null;
	}
	
	public String deletePaymentLinkById(Map<String, Object> requestBody) {
		int id = ((Double) requestBody.get("id")).intValue();
		List<PaymentLinkImpl> paymentLinks = paymentLinkRepository.getAllObject("paymentlink_impl");
		for(PaymentLinkImpl payment : paymentLinks){
			if(payment.getIdTransaction() == id){
				HashMap<String, Object> paymentMap = payment.toHashMap();
				int intId = ((Integer) paymentMap.get("idTransaction")).intValue();
				System.out.println(intId);
				paymentLinkRepository.deleteObject(intId);
				return "SUCCESS";
			}
		}

		return "There is no paymentlink with id: " + id;
	}

	public List<HashMap<String, Object>> deletePaymentLinkByIdTransaction(Map<String, Object> requestBody) {
		return record.deletePayment(requestBody);
	}
}
