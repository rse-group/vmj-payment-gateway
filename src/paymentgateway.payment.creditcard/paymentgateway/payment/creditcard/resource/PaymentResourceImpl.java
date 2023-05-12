package paymentgateway.payment.creditcard;

import com.google.gson.Gson;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import paymentgateway.payment.PaymentFactory;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentResourceDecorator;
import paymentgateway.payment.core.PaymentImpl;
import paymentgateway.payment.core.PaymentResourceComponent;
import paymentgateway.payment.PropertiesReader;

public class PaymentResourceImpl extends PaymentResourceDecorator {
	// implement this to work with authorization module
	protected String apiKey;
	protected String apiEndpoint;

	public PaymentResourceImpl(PaymentResourceComponent record) {
		super(record);
	}

	public Payment createPayment(HashMap<String, Object> vmjExchange) {
		String idToken = (String) vmjExchange.get("idToken");

		Payment transaction = record.createPayment(vmjExchange);
		String creditCardUrl = sendTransaction(vmjExchange);
		Payment creditCardTransaction = PaymentFactory.createPayment(
				"paymentgateway.payment.creditcard.PaymentImpl", transaction, idToken, creditCardUrl);
		return creditCardTransaction;
	}

	protected String sendTransaction(HashMap<String, Object> vmjExchange) {
		String idTransaction = (String) vmjExchange.get("idTransaction");
		int amount = (int) vmjExchange.get("amount");
		String idToken = (String) vmjExchange.get("idToken");

		Gson gson = new Gson();
		Map<String, Object> requestMap = new HashMap<String, Object>();
		requestMap.put("payment_type", "credit_card");
		Map<String, Object> transaction_details = new HashMap<String, Object>();
		transaction_details.put("order_id", idTransaction);
		transaction_details.put("gross_amount", amount);
		requestMap.put("transaction_details", transaction_details);
		Map<String, Object> credit_card = new HashMap<String, Object>();
		credit_card.put("token_id", idToken);
		credit_card.put("authentication", true);
		requestMap.put("credit_card", credit_card);

		String requestString = gson.toJson(requestMap);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.header("Authorization", getBasicAuthenticationHeader(apiKey, ""))
				.header("Content-Type", "application/json")
				.header("Accept", "application/json")
				.uri(URI.create(apiEndpoint))
				.POST(HttpRequest.BodyPublishers.ofString(requestString))
				.build();
		String creditCardUrl = "";

		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			CreditCardResponse responseObj = gson.fromJson(rawResponse, CreditCardResponse.class);
			creditCardUrl = creditCardUrl + responseObj.getRedirect_url();
		} catch (Exception e) {
			System.out.println(e);
		}

		return creditCardUrl;
	}

	private static final String getBasicAuthenticationHeader(String username, String password) {
		String valueToEncode = username + ":" + password;
		return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
	}

	@Route(url = "test/call/creditcard")
	public HashMap<String, Object> testDebitCard(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS"))
			return null;

		int amount = ((Double) vmjExchange.getRequestBodyForm("amount")).intValue();
		String idTransaction = (String) vmjExchange.getRequestBodyForm("idTransaction");
		String idToken = (String) vmjExchange.getRequestBodyForm("idToken");

		HashMap<String, Object> request = new HashMap<String, Object>();
		request.put("amount", amount);
		int randomId = Math.abs((new Random()).nextInt());
		request.put("idTransaction", idTransaction);
		request.put("idToken", idToken);
		Payment result = this.createPayment(request);
		return result.toHashMap();
	}
}
