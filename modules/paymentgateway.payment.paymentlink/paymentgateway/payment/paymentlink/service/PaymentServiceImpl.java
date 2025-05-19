package paymentgateway.payment.paymentlink;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


import vmj.hibernate.integrator.RepositoryUtil;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import paymentgateway.config.ConfigFactory;
import paymentgateway.config.core.Config;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentServiceDecorator;
import paymentgateway.payment.core.PaymentServiceComponent;
import paymentgateway.payment.PaymentFactory;

public class PaymentServiceImpl extends PaymentServiceDecorator {
	RepositoryUtil<PaymentLinkImpl> paymentLinkRepository;

	public PaymentServiceImpl(PaymentServiceComponent record) {
		super(record);
		this.paymentLinkRepository = new RepositoryUtil<PaymentLinkImpl>(paymentgateway.payment.paymentlink.PaymentLinkImpl.class);
	}

	public Payment createPayment(Map<String, Object> requestBody) {
		record.validateVendorName((String) requestBody.get("vendor_name"));
		double amount = record.validateAmount(requestBody.get("amount"));
		requestBody.put("amount", amount);

		Map<String, Object> response = sendTransaction(requestBody);
		String paymentLink = (String) response.get("url");
		String id = (String) response.get("id");
		String status = (String) response.get("status");
		String vendorGeneratedId = (String) response.get("vendor_generated_id");

		Payment transaction = record.createPayment(requestBody, id, status, vendorGeneratedId);
		Payment paymentLinkTransaction =
			PaymentFactory.createPayment("paymentgateway.payment.paymentlink.PaymentLinkImpl",
			transaction, UUID.fromString(id), paymentLink);
		PaymentRepository.saveObject(paymentLinkTransaction);
		return paymentLinkTransaction;
	}

	public Map<String, Object> sendTransaction(Map<String, Object> requestBody) {
		String vendorName = (String) requestBody.get("vendor_name");

		Config config = ConfigFactory.createConfig(vendorName, ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));

		Gson gson = new Gson();
		Map<String, Object> requestMap = config.getPaymentLinkRequestBody(requestBody);
		String id = (String) requestMap.get("id");
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
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		return responseMap;
	}
	
	public List<PaymentLinkImpl> getByVendorName(Map<String, String> queryParams) {
		String vendorName = (String) queryParams.get("vendor_name");
		record.validateVendorName(vendorName);
		List<PaymentLinkImpl> result = new ArrayList<>();
		List<PaymentLinkImpl> paymentLink = paymentLinkRepository.getAllObject("paymentlink_impl");
		for(PaymentLinkImpl payment : paymentLink){
			if (payment.getVendorName().equals(vendorName)){
				result.add(payment);
			}
		}
		return result;
	}
	
	public HashMap<String, Object> getById(Map<String, String> queryParams) {
		String id = (String) queryParams.get("id");
		String validatedId = record.validateId(id);
		List<PaymentLinkImpl> paymentLink = paymentLinkRepository.getAllObject("paymentlink_impl");
		for(PaymentLinkImpl payment : paymentLink){
			if (payment.getIdTransaction().toString().equals(validatedId)){
				return payment.toHashMap();
			}
		}
		throw new BadRequestException("Payment link dengan ID " + validatedId + " tidak ditemukan");
	}
	
	public String deletePaymentLinkById(Map<String, Object> requestBody) {
		String id = (String) requestBody.get("id");
		String validatedId = record.validateId(id);
		List<PaymentLinkImpl> paymentLinks = paymentLinkRepository.getAllObject("paymentlink_impl");
		for(PaymentLinkImpl payment : paymentLinks){
			if(payment.getId().toString().equals(validatedId)){
				System.out.println(payment.getId());
				paymentLinkRepository.deleteObject(payment.getId());
				return "SUCCESS";
			}
		}

		throw new BadRequestException("Payment link dengan ID " + validatedId + " tidak ditemukan");
	}
}
