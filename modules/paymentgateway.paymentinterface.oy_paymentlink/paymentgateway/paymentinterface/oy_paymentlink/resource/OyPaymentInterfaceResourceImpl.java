package paymentgateway.paymentinterface.oy_paymentlink;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.util.Base64;
import java.util.*;

import com.google.gson.Gson;

import paymentgateway.paymentinterface.PaymentInterfaceFactory;
import paymentgateway.paymentinterface.core.PaymentInterface;
import paymentgateway.paymentinterface.core.PaymentInterfaceResourceComponent;
import paymentgateway.paymentinterface.paymentlink.PaymentInterfaceResourceImpl;

public class OyPaymentInterfaceResourceImpl extends PaymentInterfaceResourceImpl {
	
	protected String apiKey;
	protected String apiUsername;
	protected String apiEndpoint;
	public OyPaymentInterfaceResourceImpl(PaymentInterfaceResourceComponent record) {
		super(record);
		this.apiKey = "60c97bc8-ac93-4ae2-806b-76f361235535";
		this.apiUsername = "tsaqif31";
		this.apiEndpoint = "https://api-stg.oyindonesia.com/api/payment-checkout/create-v2";
	}
	
	public PaymentInterface createTransaction(String apiKey, int amount, String idTransaction, String apiEndpoint) {
		return createTransaction(amount, idTransaction);
	}
	
	public PaymentInterface createTransaction(int amount, String idTransaction) {
		PaymentInterface transaction = record.createTransaction(apiKey, amount, idTransaction, apiEndpoint);
		String paymentLink = sendTransactionAPICall(amount, idTransaction);
		PaymentInterface paymentLinkTransaction = PaymentInterfaceFactory.createPaymentInterface("paymentgateway.paymentinterface.paymentlink.PaymentInterfaceImpl", transaction, paymentLink);
		return paymentLinkTransaction;
	}
	
	private String sendTransactionAPICall(int amount, String idTransaction) {
		Gson gson = new Gson();
		Map<String,Object> requestMap = new HashMap<String,Object>();
		requestMap.put("partner_tx_id", idTransaction);
		requestMap.put("amount", amount);
		
		String requestString = gson.toJson(requestMap);
		
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
			OyPaymentLinkResponse responseObj = gson.fromJson(rawResponse, OyPaymentLinkResponse.class);
			paymentLink = paymentLink + responseObj.getUrl();
		} catch (Exception e) {
			System.out.println(e);
		}
		return paymentLink;
	}
	
	@Route(url="test/call/oy/paymentlink")
	public HashMap<String,Object> oyPaymentLink(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) return null;
		PaymentInterface result = this.createTransaction("random", 36000, "test-paymentlink-1", "random");
		return result.toHashMap();
	}
}