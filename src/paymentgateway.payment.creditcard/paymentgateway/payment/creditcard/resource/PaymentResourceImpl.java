package paymentgateway.payment.creditcard;

import com.google.gson.Gson;

import paymentgateway.payment.PaymentConfiguration;
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
import paymentgateway.payment.core.PaymentResourceComponent;

public class PaymentResourceImpl extends PaymentResourceDecorator {
	// implement this to work with authorization module
	protected String apiKey;
	protected String apiEndpoint;

	public PaymentResourceImpl(PaymentResourceComponent record) {
		super(record);
	}

	public Payment createPayment(VMJExchange vmjExchange, String productName, String serviceName) {
		CreditCardResponse response = sendTransaction(vmjExchange, productName, serviceName);
		String idToken = (String) vmjExchange.getRequestBodyForm("token_id");
		String creditCardUrl = response.getRedirect_url();
		int id = response.getId();

		Payment transaction = record.createPayment(vmjExchange, id, productName);
		Payment creditCardTransaction = PaymentFactory.createPayment(
				"paymentgateway.payment.creditcard.PaymentImpl", transaction, idToken, creditCardUrl);
		PaymentRepository.saveObject(creditCardTransaction);
		return creditCardTransaction;
	}

	protected CreditCardResponse sendTransaction(VMJExchange vmjExchange, String productName, String serviceName) {
//		String idTransaction = (String) vmjExchange.getRequestBodyForm("idTransaction");
//		int amount = (int) vmjExchange.getRequestBodyForm("amount");
//		String idToken = (String) vmjExchange.getRequestBodyForm("idToken");
//
//		Gson gson = new Gson();
//		Map<String, Object> requestMap = new HashMap<String, Object>();
//		requestMap.put("payment_type", "credit_card");
//		Map<String, Object> transaction_details = new HashMap<String, Object>();
//		transaction_details.put("order_id", idTransaction);
//		transaction_details.put("gross_amount", amount);
//		requestMap.put("transaction_details", transaction_details);
//		Map<String, Object> credit_card = new HashMap<String, Object>();
//		credit_card.put("token_id", idToken);
//		credit_card.put("authentication", true);
//		requestMap.put("credit_card", credit_card);
//
//		String configUrl = PaymentConfiguration.getProductEnv(productName, serviceName);
//
//		String requestString = gson.toJson(requestMap);
//		HttpClient client = HttpClient.newHttpClient();
//		HttpRequest request = HttpRequest.newBuilder()
//				.header("Authorization", "Basic U0ItTWlkLXNlcnZlci1PRkRNbmtLbzhBRG1nLXZSdmxzSnJhZ2c=")
//				.header("Content-Type", "application/json")
//				.header("Accept", "application/json")
//				.uri(URI.create(configUrl))
//				.POST(HttpRequest.BodyPublishers.ofString(requestString))
//				.build();
//		String creditCardUrl = "";

		Gson gson = new Gson();
		Map<String, Object> requestMap = PaymentConfiguration.processRequestMap(vmjExchange,productName,serviceName);
		int id = ((Integer) requestMap.get("id")).intValue();
		requestMap.remove("id");
		String requestString = gson.toJson(requestMap);
		String configUrl = PaymentConfiguration.getProductEnv(productName, serviceName);
		HashMap<String, String> headerParams = PaymentConfiguration.getHeaderParams(productName);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = (PaymentConfiguration.getBuilder(HttpRequest.newBuilder(),headerParams))
				.uri(URI.create(configUrl))
				.POST(HttpRequest.BodyPublishers.ofString(requestString))
				.build();

		CreditCardResponse responseObj = null;
		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			System.out.println(rawResponse);
			responseObj = gson.fromJson(rawResponse, CreditCardResponse.class);
		} catch (Exception e) {
			System.out.println(e);
		}
		responseObj.setId(id);
		return responseObj;
	}


	@Route(url = "test/call/creditcard")
	public HashMap<String, Object> testDebitCard(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS"))
			return null;

		String productName = (String) vmjExchange.getRequestBodyForm("product_name");
		Payment result = this.createPayment(vmjExchange, productName, "CreditCard");
		return result.toHashMap();
	}
}
