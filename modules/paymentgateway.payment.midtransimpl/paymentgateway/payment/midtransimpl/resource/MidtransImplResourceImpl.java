package paymentgateway.payment.midtransimpl;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import paymentgateway.payment.PaymentFactory;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentResourceDecorator;
import paymentgateway.payment.core.PaymentImpl;
import paymentgateway.payment.core.PaymentResourceComponent;

public class PaymentLinkResourceImpl extends PaymentResourceDecorator {
	// implement this to work with authorization module
	protected String apiKey;
	protected String apiEndpoint;

	public PaymentLinkResourceImpl(PaymentResourceComponent record) {
		super(record);
		this.apiKey = "SB-Mid-server-NVYFqUidEQUTaozWjW77fFWW";
		this.apiEndpoint = "https://api.sandbox.midtrans.com/v1/payment-links";
	}

	public Payment createPayment(HashMap<String, Object> vmjExchange) {
		Payment transaction = record.createPayment(vmjExchange);
		System.out.println("this is IdTransaction: " + transaction.getIdTransaction());
		String paymentLink = sendTransaction(vmjExchange);
		System.out.println("masuk paymentImpl");
		Payment paymentLinkTransaction = PaymentFactory.createPayment("paymentgateway.payment.paymentlink.PaymentImpl",
				transaction, paymentLink);
		System.out.println("masuk paymentLinkImpl");
		Payment midtransPaymentLinkTransaction = PaymentFactory.createPayment(
				"paymentgateway.payment.midtransimpl.PaymentLinkImpl",
				paymentLinkTransaction, "test");
		return midtransPaymentLinkTransaction;

	}

	protected String sendTransaction(HashMap<String, Object> vmjExchange) {
		String idTransaction = (String) vmjExchange.get("idTransaction");
		int amount = (int) vmjExchange.get("amount");

		Gson gson = new Gson();
		Map<String, Object> transaction_details = new HashMap<String, Object>();
		transaction_details.put("order_id", idTransaction);
		transaction_details.put("gross_amount", amount);
		Map<String, Object> requestMap = new HashMap<String, Object>();
		requestMap.put("transaction_details", transaction_details);

		String requestString = gson.toJson(requestMap);
		System.out.println("this is request String: " + requestString);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.header("Authorization", getBasicAuthenticationHeader(apiKey, ""))
				.header("Content-Type", "application/json")
				.header("Accept", "application/json")
				.uri(URI.create(apiEndpoint))
				.POST(HttpRequest.BodyPublishers.ofString(requestString))
				.build();
		String paymentLink = "";

		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			System.out.println(rawResponse);
			PaymentLinkResponse responseObj = gson.fromJson(rawResponse, PaymentLinkResponse.class);
			paymentLink = paymentLink + responseObj.getPayment_url();
			System.out.println("this is paymentlink: " + paymentLink);
		} catch (Exception e) {
			System.out.println(e);
		}

		return paymentLink;
	}

	private static final String getBasicAuthenticationHeader(String username, String password) {
		String valueToEncode = username + ":" + password;
		return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
	}

	@Route(url = "test/call/paymentlink")
	public HashMap<String, Object> paymentLink(VMJExchange vmjExchange) {

		if (vmjExchange.getHttpMethod().equals("OPTIONS"))
			return null;

		int amount = ((Double) vmjExchange.getRequestBodyForm("amount")).intValue();
		String idTransaction = (String) vmjExchange.getRequestBodyForm("idTransaction");

		HashMap<String, Object> testExchange = new HashMap<>();
		testExchange.put("idTransaction", idTransaction);
		testExchange.put("amount", amount);
		Payment result = this.createPayment(testExchange);
		return result.toHashMap();
	}

	// @Route(url = "test/call/debitcard")
	// public HashMap<String, Object> testDebitCard(VMJExchange vmjExchange) {
	// if (vmjExchange.getHttpMethod().equals("OPTIONS"))
	// return null;

	// int amount = ((Double) vmjExchange.getRequestBodyForm("amount")).intValue();
	// String idTransaction = (String)
	// vmjExchange.getRequestBodyForm("idTransaction");
	// String paymentMethods = (String)
	// vmjExchange.getRequestBodyForm("paymentMethods");
	// String bankCode = (String) vmjExchange.getRequestBodyForm("bankCode");

	// HashMap<String, Object> request = new HashMap<String, Object>();
	// request.put("amount", amount);
	// request.put("idTransaction", idTransaction);
	// request.put("bankCode", bankCode);
	// Payment result = this.createPayment(request);
	// return result.toHashMap();
	// }
}
