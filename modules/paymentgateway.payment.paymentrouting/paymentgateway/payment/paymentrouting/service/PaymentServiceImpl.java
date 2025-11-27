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

import java.io.IOException;
import java.lang.reflect.Type;

import vmj.hibernate.integrator.RepositoryUtil;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentServiceDecorator;
import paymentgateway.payment.core.PaymentServiceComponent;
import paymentgateway.payment.PaymentFactory;

public class PaymentServiceImpl extends PaymentServiceDecorator {
	RepositoryUtil<PaymentImpl> paymentRoutingRepository;
	
	public PaymentServiceImpl (PaymentServiceComponent record) {
        super(record);
		this.paymentRoutingRepository = new RepositoryUtil<PaymentImpl>(paymentgateway.payment.paymentrouting.PaymentImpl.class);
    }

	public Payment createPayment(Map<String, Object> requestBody) {
		record.validateVendorName((String) requestBody.get("vendor_name"));
		double amount = record.validateAmount(requestBody.get("amount"));
		requestBody.put("amount", amount);

		Map<String, Object> response = sendTransaction(requestBody);

		String id = (String) response.get("id");
		String paymentCheckoutUrl = (String) response.get("payment_checkout_url");
		String status = (String) response.get("status");
		String vendorGeneratedId = (String) response.get("vendor_generated_id");

		Payment transaction = record.createPayment(requestBody, id, status, vendorGeneratedId);
		Payment paymentRoutingTransaction = PaymentFactory.createPayment(
				"paymentgateway.payment.paymentrouting.PaymentImpl",
				transaction,
				paymentCheckoutUrl
				);

		PaymentRepository.saveObject(paymentRoutingTransaction);
		return paymentRoutingTransaction;
	}
	
	public Map<String, Object> sendTransaction(Map<String, Object> requestBody) {
		String vendorName = (String) requestBody.get("vendor_name");

		Config config = ConfigFactory.createConfig(vendorName, ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
		
		Gson gson = new Gson();
		Map<String, Object> requestMap = config.getPaymentRoutingRequestBody(requestBody);
		String id = (String) requestMap.get("id");
		requestMap.remove("id");
		String requestString = config.getRequestString(requestMap);
		String configUrl = config.getProductEnv("PaymentRouting");
		HashMap<String, String> headerParams = config.getHeaderParams();
		System.out.println("configUrl: " + configUrl);
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
			responseMap = config.getPaymentRoutingResponse(rawResponse, id);
		} catch (IOException | InterruptedException e) {
			System.out.println(e);
		}

		return responseMap;
	}

	public List<PaymentImpl> getByVendorName(Map<String, String> queryParams) {
		String vendorName = (String) queryParams.get("vendor_name");
		record.validateVendorName(vendorName);
		List<PaymentImpl> result = new ArrayList<>();
		List<PaymentImpl> paymentRoutings = paymentRoutingRepository.getAllObject("paymentrouting_impl");
		for(PaymentImpl paymentRouting : paymentRoutings){
			if (paymentRouting.getVendorName().equals(vendorName)){
				result.add(paymentRouting);
			}
		}
		return result;
	}

	public HashMap<String, Object> getById(Map<String, String> queryParams) {
		String id = (String) queryParams.get("id");
		String validatedId = record.validateId(id);
		List<PaymentImpl> paymentRoutings = paymentRoutingRepository.getAllObject("paymentrouting_impl");
		for(PaymentImpl paymentRouting : paymentRoutings){
			if (paymentRouting.getIdTransaction().toString().equals(validatedId)){
				return paymentRouting.toHashMap();
			}
		}
		throw new BadRequestException("Payment routing dengan ID " + validatedId + " tidak ditemukan");
	}
}

