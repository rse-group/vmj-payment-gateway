package paymentgateway.payment.oyimpl;

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

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import paymentgateway.payment.PaymentFactory;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentResourceDecorator;
import paymentgateway.payment.paymentlink.PaymentLinkResponse;
import paymentgateway.payment.core.PaymentImpl;
import paymentgateway.payment.core.PaymentResourceComponent;

public class PaymentLinkResourceImpl extends PaymentResourceDecorator {

	protected String apiKey;
	protected String apiUsername;
	protected String apiEndpoint;

	public PaymentLinkResourceImpl(PaymentResourceComponent record) {
		super(record);
		this.apiKey = "60c97bc8-ac93-4ae2-806b-76f361235535";
		this.apiUsername = "tsaqif31";
		this.apiEndpoint = "https://api-stg.oyindonesia.com/api/payment-checkout/create-v2";
	}

	public Payment createPayment(HashMap<String, Object> vmjExchange) {
		Payment transaction = record.createPayment(vmjExchange);
		String paymentMethods = (String) vmjExchange.get("paymentMethods");
		String sourceOfFunds = (String) vmjExchange.get("sourceOfFunds");
		String paymentLink = sendTransaction(vmjExchange);
		Payment paymentLinkTransaction = PaymentFactory.createPayment(
				"paymentgateway.payment.paymentlink.PaymentLinkImpl",
				paymentMethods,
				sourceOfFunds,
				transaction,
				paymentLink);
		return paymentLinkTransaction;
	}

	protected String sendTransaction(HashMap<String, Object> vmjExchange) {
		String idTransaction = (String) vmjExchange.get("idTransaction");
		int amount = (int) vmjExchange.get("amount");
		String paymentMethods = (String) vmjExchange.get("paymentMethods");
		String sourceOfFunds = (String) vmjExchange.get("sourceOfFunds");

		Gson gson = new Gson();
		Map<String, Object> requestMap = new HashMap<String, Object>();
		requestMap.put("partner_tx_id", idTransaction);
		requestMap.put("sender_name", "bagus");
		requestMap.put("amount", amount);
		requestMap.put("is_open", false);
		requestMap.put("include_admin_fee", true);
		requestMap.put("list_enable_payment_method", paymentMethods);
		requestMap.put("list_enable_sof", sourceOfFunds);

		String requestString = gson.toJson(requestMap);
		System.out.println("this is request String: " + requestString);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.header("X-Api-Key", apiKey)
				.header("X-Oy-Username", apiUsername)
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

	@Route(url = "test/call/oypaymentlink")
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
}
