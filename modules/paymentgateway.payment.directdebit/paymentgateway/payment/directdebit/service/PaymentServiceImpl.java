package paymentgateway.payment.directdebit;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentServiceDecorator;
import paymentgateway.payment.core.PaymentServiceComponent;
import paymentgateway.payment.PaymentFactory;

public class PaymentServiceImpl extends PaymentServiceDecorator {

	public PaymentServiceImpl(PaymentServiceComponent record) {
		super(record);
	}

	public Payment createPayment(Map<String, Object> requestBody) {
		record.validateVendorName((String) requestBody.get("vendor_name"));
		double amount = record.validateAmount(requestBody.get("amount"));
		requestBody.put("amount", amount);

		Map<String, Object> response = sendTransaction(requestBody);

		String status = (String) response.get("status");
		String directDebitUrl = (String) response.get("direct_debit_url");
		String id = (String) response.get("id");
		String vendorGeneratedId = (String) response.get("vendor_generated_id");

		Payment transaction = record.createPayment(requestBody, id, status, vendorGeneratedId);
		Payment directDebitTransaction = PaymentFactory.createPayment(
				"paymentgateway.payment.directdebit.PaymentImpl", transaction, directDebitUrl);
		PaymentRepository.saveObject(directDebitTransaction);
		return directDebitTransaction;
	}

	public Map<String, Object> sendTransaction(Map<String, Object> requestBody) {
		String vendorName = (String) requestBody.get("vendor_name");

		Config config = ConfigFactory.createConfig(vendorName, ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
		
		Map<String, Object> requestMap = config.getDirectDebitRequestBody(requestBody);
		String id = (String) requestMap.get("id");
		requestMap.remove("id");
		String configUrl = config.getProductEnv("DirectDebit");
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
			System.out.println(request.toString());
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			System.out.println("rawResponse " + rawResponse);
			responseMap = config.getDirectDebitResponse(rawResponse, id);
			
		} catch (IOException | InterruptedException e) {
			System.out.println(e);
		}
		
		return responseMap;
	}
}
