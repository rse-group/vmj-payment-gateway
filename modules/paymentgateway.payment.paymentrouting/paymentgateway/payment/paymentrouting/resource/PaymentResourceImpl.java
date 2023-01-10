package paymentgateway.payment.paymentrouting;

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
import paymentgateway.payment.core.PaymentImpl;
import paymentgateway.payment.core.PaymentResourceComponent;

public class PaymentResourceImpl extends PaymentResourceDecorator {
	
	protected String apiKey;
	protected String apiUsername;
	protected String apiEndpoint;
    public PaymentResourceImpl (PaymentResourceComponent record) {
        super(record);
		this.apiKey = "60c97bc8-ac93-4ae2-806b-76f361235535";
		this.apiUsername = "tsaqif31";
		this.apiEndpoint = "https://api-stg.oyindonesia.com/api/payment-routing/create-transaction";
    }

	public Payment createPayment(HashMap<String,Object> vmjExchange) {
		String paymentMethods = (String) vmjExchange.get("paymentMethods");
		String sourceOfFunds = (String) vmjExchange.get("sourceOfFunds");
		List<PaymentRoutingRecipient> routings = (List<PaymentRoutingRecipient>) vmjExchange.get("routings");
		
		Payment transaction = record.createPayment(vmjExchange);
		String paymentCheckoutUrl = sendTransaction(vmjExchange);
		Payment paymentRoutingTransaction = PaymentFactory.createPayment(
				"paymentgateway.payment.paymentrouting.PaymentImpl",
				transaction,
				paymentMethods,
				sourceOfFunds,
				routings,
				paymentCheckoutUrl
				);
		return paymentRoutingTransaction;
	}
	
	protected String sendTransaction(HashMap<String,Object> vmjExchange) {
		String idTransaction = (String) vmjExchange.get("idTransaction");
		int amount = (int) vmjExchange.get("amount");
		String paymentMethods = (String) vmjExchange.get("paymentMethods");
		String sourceOfFunds = (String) vmjExchange.get("sourceOfFunds");
		List<PaymentRoutingRecipient> routings = (List<PaymentRoutingRecipient>) vmjExchange.get("routings");
		
		Gson gson = new Gson();
		Map<String,Object> requestMap = new HashMap<String,Object>();
		requestMap.put("partner_trx_id", idTransaction);
		requestMap.put("need_frontend", true);
		requestMap.put("receive_amount", amount);
		requestMap.put("list_enable_payment_method", paymentMethods);
		requestMap.put("list_enable_sof",sourceOfFunds);
		List<Map<String,Object>> payment_routing = new ArrayList();
		for(PaymentRoutingRecipient x : routings) {
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
			PaymentRoutingResponse responseObj = gson.fromJson(rawResponse, PaymentRoutingResponse.class);
			payment_checkout_url = payment_checkout_url + (responseObj.getPayment_info()).getPayment_checkout_url();
		} catch (Exception e) {
			System.out.println(e);
		}
		return payment_checkout_url;
	}
	
	@Route(url="test/call/paymentrouting")
	public HashMap<String,Object> paymentLinkEndpoint(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) return null;
		int amount = ((Double) vmjExchange.getRequestBodyForm("amount")).intValue();
		String idTransaction = (String) vmjExchange.getRequestBodyForm("idTransaction");
		String paymentMethods = (String) vmjExchange.getRequestBodyForm("paymentMethods");
		String sourceOfFunds = (String) vmjExchange.getRequestBodyForm("sourceOfFunds");
		List<PaymentRoutingRecipient> routings = new ArrayList<PaymentRoutingRecipient>();
		List<Map<String,Object>> recipient = (List<Map<String,Object>>) vmjExchange.getRequestBodyForm("routings");
		for(Map<String,Object> x : recipient) {
			PaymentRoutingRecipient temp = new PaymentRoutingRecipient();
			temp.setRecipient_account((String) x.get("recipient_account"));
			temp.setRecipient_bank((String) x.get("recipient_bank"));
			temp.setRecipient_amount(((Double) x.get("recipient_amount")).intValue());
			temp.setRecipient_email((String) x.get("recipient_email"));
			temp.setRecipient_note((String) x.get("recipient_note"));
			routings.add(temp);
		}
		
		HashMap<String,Object> testVMJExchange = new HashMap<String,Object>();
		testVMJExchange.put("idTransaction", idTransaction);
		testVMJExchange.put("amount", amount);
		testVMJExchange.put("paymentMethods", paymentMethods);
		testVMJExchange.put("sourceOfFunds", sourceOfFunds);
		testVMJExchange.put("routings", routings);
		Payment result = this.createPayment(testVMJExchange);
		return result.toHashMap();
	}
	
	private HashMap<String,Object> createTestVMJExchange() {
		HashMap<String,Object> test = new HashMap<>();
		test.put("idTransaction", "id-transaction-" + Math.abs((new Random()).nextInt()));
		test.put("amount", 58000);
		test.put("paymentMethods", "VA");
		test.put("sourceOfFunds", "002");
		PaymentRoutingRecipient recipient = new PaymentRoutingRecipient();
		recipient.setRecipient_account("1234567890");
		recipient.setRecipient_bank("014");
		recipient.setRecipient_amount(10000);
		recipient.setRecipient_email("recipient_bca@gmail.com");
		recipient.setRecipient_note("test payment routing");
		List<PaymentRoutingRecipient> routings = new ArrayList<>();
		routings.add(recipient);
		test.put("routings", routings);
		return test;
	}
}

