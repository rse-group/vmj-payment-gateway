package paymentgateway.payment.directdebit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

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

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;
import paymentgateway.payment.core.CreatePaymentRequestBody;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentServiceDecorator;
import paymentgateway.payment.core.PaymentServiceComponent;
import paymentgateway.payment.PaymentFactory;

public class PaymentServiceImpl extends PaymentServiceDecorator {

	public PaymentServiceImpl(PaymentServiceComponent record) {
		super(record);
	}

	public Payment createPayment(CreatePaymentRequestBody requestBody) {
		Map<String, Object> response = sendTransaction(requestBody);

		String statusDirectDebitPayment = (String) response.get("status");
		String directDebitUrl = (String) response.get("direct_debit_url");
		int id = (int) response.get("id");

		Payment transaction = record.createPayment(requestBody, id);
		Payment cardTransaction = PaymentFactory.createPayment(
				"paymentgateway.payment.directdebit.PaymentImpl", transaction, statusDirectDebitPayment, directDebitUrl);
		PaymentRepository.saveObject(cardTransaction);
		return cardTransaction;
	}

	public Map<String, Object> sendTransaction(CreatePaymentRequestBody requestBody) {
		String vendorName = (String) requestBody.vendorName;

		Config config = ConfigFactory.createConfig(vendorName, ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
		
		Gson gson = new Gson();
		Map<String, Object> requestMap = config.getDirectDebitRequestBody(requestBody.toMap());
		int id = ((Integer) requestMap.get("id")).intValue();
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
