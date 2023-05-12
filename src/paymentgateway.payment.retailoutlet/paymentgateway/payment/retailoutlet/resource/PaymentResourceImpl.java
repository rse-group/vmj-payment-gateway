package paymentgateway.payment.retailoutlet;

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

public class PaymentResourceImpl extends PaymentResourceDecorator {
	// implement this with authorization module
	protected String apiKey;
	protected String apiEndpoint;
	protected final String payment_type = "cstore";
    public PaymentResourceImpl (PaymentResourceComponent record) {
    	super(record);
    	this.apiKey = "SB-Mid-server-NVYFqUidEQUTaozWjW77fFWW";
    	this.apiEndpoint = "https://api.sandbox.midtrans.com/v2/charge";
    }

	public Payment createPayment(HashMap<String,Object> vmjExchange) {
		String retailOutlet = (String) vmjExchange.get("retailOutlet");
		
		Payment transaction = record.createPayment(vmjExchange);
		String retailPaymentCode = sendTransaction(vmjExchange);
		Payment retailOutletChannel =
				PaymentFactory.createPayment(
						"paymentgateway.payment.retailoutlet.PaymentImpl",
						transaction,
						retailOutlet,
						retailPaymentCode
						);
		
		return retailOutletChannel;
	}
	
	protected String sendTransaction(HashMap<String,Object> vmjExchange) {
		String idTransaction = (String) vmjExchange.get("idTransaction");
		int amount = (int) vmjExchange.get("amount");
		String retailOutlet = (String) vmjExchange.get("retailOutlet");
		
		Gson gson = new Gson();
		Map<String,Object> requestMap = new HashMap<String,Object>();
		requestMap.put("payment_type", payment_type);
		Map<String,Object> transaction_details = new HashMap<String,Object>();
		transaction_details.put("order_id", idTransaction);
		transaction_details.put("gross_amount", amount);
		requestMap.put("transaction_details", transaction_details);
		Map<String,Object> cstore = new HashMap<String,Object>();
		cstore.put("store", retailOutlet);
		requestMap.put("cstore", cstore);
		
		String requestString = gson.toJson(requestMap);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.header("Authorization", getBasicAuthenticationHeader(apiKey, ""))
				.header("Content-Type", "application/json")
				.header("Accept", "application/json")
				.uri(URI.create(apiEndpoint))
				.POST(HttpRequest.BodyPublishers.ofString(requestString))
				.build();
		String retailPaymentCode = "";
		
		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			RetailOutletResponse responseObj = gson.fromJson(rawResponse, RetailOutletResponse.class);
			retailPaymentCode = retailPaymentCode + responseObj.getPayment_code();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return retailPaymentCode;
	}
	
	private static final String getBasicAuthenticationHeader(String username, String password) {
		String valueToEncode = username + ":" + password;
		return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
	}
	
	@Route(url="test/call/retailoutlet")
	public HashMap<String,Object> testRetailOutlet(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) return null;
		
		int amount = ((Double) vmjExchange.getRequestBodyForm("amount")).intValue();
		String idTransaction = (String) vmjExchange.getRequestBodyForm("idTransaction");
		String retailOutlet = (String) vmjExchange.getRequestBodyForm("retailOutlet");
		
		HashMap<String,Object> request = new HashMap<String,Object>();
		request.put("amount", amount);
		int randomId = Math.abs((new Random()).nextInt());
		request.put("idTransaction", idTransaction);
		request.put("retailOutlet", retailOutlet);
		Payment result = this.createPayment(request);
		return result.toHashMap();
	}
}

