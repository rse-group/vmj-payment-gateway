package paymentgateway.paymentinterface.midtrans_paymentlink;

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

public class MidtransPaymentInterfaceResourceImpl extends PaymentInterfaceResourceImpl {
	
	protected String apiKey;
	protected String apiEndpoint;
	public MidtransPaymentInterfaceResourceImpl(PaymentInterfaceResourceComponent record) {
		super(record);
		this.apiKey = "SB-Mid-server-NVYFqUidEQUTaozWjW77fFWW";
		this.apiEndpoint = "https://api.sandbox.midtrans.com/v1/payment-links";
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
		Map<String,Object> transaction_details = new HashMap<String,Object>();
		transaction_details.put("order_id", idTransaction);
		transaction_details.put("gross_amount", amount);
		Map<String,Object> requestMap = new HashMap<String,Object>();
		requestMap.put("transaction_details", transaction_details);
		
		String requestString = gson.toJson(requestMap);
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
			MidtransPaymentLinkResponse responseObj = gson.fromJson(rawResponse, MidtransPaymentLinkResponse.class);
			paymentLink = paymentLink + responseObj.getPayment_url();
		} catch (Exception e) {
			System.out.println(e);
		}
		return paymentLink;
	}
	
	private static final String getBasicAuthenticationHeader(String username, String password) {
		String valueToEncode = username + ":" + password;
		return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
	}
	
	@Route(url="test/call/midtrans/paymentlink")
	public HashMap<String,Object> midtransPaymentLink(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) return null;
		PaymentInterface result = this.createTransaction(12000, "test-midtrans-paymentlink-64");
		return result.toHashMap();
	}
}