package paymentgateway.payment.card;

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
		String idToken = (String) response.get("token_id");

		String status = (String) response.get("status");
		String id = (String) response.get("id");
		String vendorGeneratedId = (String) response.get("vendor_generated_id");

		Payment transaction = record.createPayment(requestBody, id, status, vendorGeneratedId);
		Payment cardTransaction = PaymentFactory.createPayment(
				"paymentgateway.payment.card.PaymentImpl", transaction, idToken);
		PaymentRepository.saveObject(cardTransaction);
		return cardTransaction;
	}

	public Map<String, Object> sendTransaction(Map<String, Object> requestBody) {
		String vendorName = (String) requestBody.get("vendor_name");

		Config config = ConfigFactory.createConfig(vendorName,
				ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
		Gson gson = new Gson();

		// Step 1: Get card token
		String tokenUrl = config.constructUrlParam("CardToken", requestBody);

		HashMap<String, String> headerParams = config.getHeaderParams();
		HttpRequest tokenRequest = (config.getBuilder(HttpRequest.newBuilder(), headerParams))
				.uri(URI.create(tokenUrl))
				.GET()
				.build();

		String tokenId = null;
		try {
			HttpResponse<String> tokenResponse = HttpClient.newHttpClient().send(tokenRequest,
					HttpResponse.BodyHandlers.ofString());
			System.out.println(tokenResponse.body());
			Map<String, Object> tokenResponseMap = gson.fromJson(tokenResponse.body(), Map.class);
			tokenId = (String) tokenResponseMap.get("token_id");
			System.out.println(tokenId);

			String tokenResponseStatusCode = (String) tokenResponseMap.get("status_code");

			if (tokenResponseStatusCode.equals("400")) {
				List<String> errorMessages = (List<String>) tokenResponseMap.get("validation_messages");
				String errorMessageString = String.join(", ", errorMessages);
				throw new BadRequestException(errorMessageString);
			}

		} catch (IOException | InterruptedException e) {
			System.out.println("Failed to get token: " + e.getMessage());
			return Map.of("error", "Token retrieval failed");
		}

		// Step 2: Send transaction request
		Map<String, Object> requestMap = config.getCardRequestBody(requestBody);
		String id = (String) requestMap.get("id");
		requestMap.remove("id");
		requestMap.put("credit_card", Map.of("token_id", tokenId, "authentication", false));
		requestMap.put("payment_type", "credit_card");

		String requestString = gson.toJson(requestMap);
		String configUrl = config.getProductEnv("Card");
		System.out.println(configUrl);

		HttpRequest transactionRequest = (config.getBuilder(HttpRequest.newBuilder(), headerParams))
				.uri(URI.create(configUrl))
				.POST(HttpRequest.BodyPublishers.ofString(requestString))
				.build();

		Map<String, Object> responseMap = new HashMap<>();
		try {
			HttpResponse<String> response = HttpClient.newHttpClient().send(transactionRequest,
					HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body();
			System.out.println("Transaction Response: " + rawResponse);
			responseMap = config.getCardResponse(rawResponse, id);
		} catch (IOException | InterruptedException e) {
			System.out.println("Transaction failed: " + e.getMessage());
		}

		responseMap.put("token_id", tokenId);

		return responseMap;
	}

}
