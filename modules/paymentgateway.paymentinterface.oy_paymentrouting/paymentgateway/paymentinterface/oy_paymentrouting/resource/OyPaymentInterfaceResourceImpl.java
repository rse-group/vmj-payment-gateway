package paymentgateway.paymentinterface.oy_paymentrouting;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import java.util.*;
import java.net.URI;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;

import com.google.gson.Gson;

import paymentgateway.paymentinterface.core.PaymentInterface;
import paymentgateway.paymentinterface.core.PaymentInterfaceResourceComponent;
import paymentgateway.paymentinterface.paymentrouting.PaymentInterfaceResourceImpl;


import paymentgateway.paymentinterface.paymentrouting.PaymentRoutingRecipient;

public class OyPaymentInterfaceResourceImpl extends PaymentInterfaceResourceImpl {
	protected String apiKey;
	protected String apiUsername;
	protected String apiEndpoint;
	
	// Set up payment routings as needed before compiling
	public OyPaymentInterfaceResourceImpl(PaymentInterfaceResourceComponent record) {
		super(record);
		setPaymentMethods("VA");
		setSourceOfFunds("002");
		List<PaymentRoutingRecipient> paymentRoutings = new ArrayList<PaymentRoutingRecipient>();
		paymentRoutings.add(setUpRouting());
		setPaymentRoutings(paymentRoutings);
		this.apiKey = "60c97bc8-ac93-4ae2-806b-76f361235535";
		this.apiUsername = "tsaqif31";
		this.apiEndpoint = "https://api-stg.oyindonesia.com/api/payment-routing/create-transaction";
	}
	
	public OyPaymentInterfaceResourceImpl(
			PaymentInterfaceResourceComponent record, String paymentMethods,
			String sourceOfFunds, List<PaymentRoutingRecipient> paymentRoutings) {
        super(record, paymentMethods, sourceOfFunds, paymentRoutings);
		this.apiKey = "60c97bc8-ac93-4ae2-806b-76f361235535";
		this.apiUsername = "tsaqif31";
		this.apiEndpoint = "https://api-stg.oyindonesia.com/api/payment-routing/create-transaction";
	}
	
	private PaymentRoutingRecipient setUpRouting() {
		PaymentRoutingRecipient recipient = new PaymentRoutingRecipient();
		recipient.setRecipient_account("1234567890");
		recipient.setRecipient_bank("014");
		recipient.setRecipient_amount(10000);
		recipient.setRecipient_email("recipient_bca@gmail.com");
		recipient.setRecipient_note("test payment routing");
		return recipient;
	}
	
	@Override
	protected String sendTransactionPaymentRouting(int amount, String idTransaction) {
		Gson gson = new Gson();
		Map<String,Object> requestMap = new HashMap<String,Object>();
		requestMap.put("partner_trx_id", idTransaction);
		requestMap.put("need_frontend", true);
		requestMap.put("receive_amount", amount);
		requestMap.put("list_enable_payment_method", getPaymentMethods());
		requestMap.put("list_enable_sof", getSourceOfFunds());
		List<Map<String,Object>> payment_routing = new ArrayList();
		for(PaymentRoutingRecipient x : getPaymentRoutings()) {
			payment_routing.add(x.toHashMap());
		}
		requestMap.put("payment_routing", payment_routing);
		
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
		String payment_checkout_url = "";
		
		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			OyPaymentRoutingResponse responseObj = gson.fromJson(rawResponse, OyPaymentRoutingResponse.class);
			payment_checkout_url = payment_checkout_url + (responseObj.getPayment_info()).getPayment_checkout_url();
		} catch (Exception e) {
			System.out.println(e);
		}
		return payment_checkout_url;
	}
	
	@Route(url="test/call/oy/paymentrouting")
	public HashMap<String,Object> paymentLinkEndpoint(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) return null;
		int randomId = Math.abs((new Random()).nextInt());
		PaymentInterface result = this.createTransaction(58000, "id-transaction-" + randomId);
		return result.toHashMap();
	}
	
}