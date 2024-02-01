package paymentgateway.payment.debitcard;

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
	
	protected String apiKey;
	protected String apiEndpoint;
    public PaymentResourceImpl (PaymentResourceComponent record) {
    	super(record);
		this.apiKey = "SB-Mid-server-NVYFqUidEQUTaozWjW77fFWW";
		this.apiEndpoint = "https://api.sandbox.midtrans.com/v2/charge";
    }

	public Payment createPayment(HashMap<String,Object> vmjExchange) {
		String bankCode = (String) vmjExchange.get("bankCode");
		
		Payment transaction = record.createPayment(vmjExchange);
		String directDebitUrl = sendTransaction(vmjExchange);
		Payment directDebitTransaction = 
				PaymentFactory.createPayment(
						"paymentgateway.payment.debitcard.PaymentImpl",
						transaction,
						bankCode,
						directDebitUrl
						);
		
		return directDebitTransaction;
	}
	
	protected String sendTransaction(HashMap<String,Object> vmjExchange) {
		String idTransaction = (String) vmjExchange.get("idTransaction");
		int amount = (int) vmjExchange.get("amount");
		String bankCode = (String) vmjExchange.get("bankCode");
		
		Gson gson = new Gson();
		Map<String,Object> requestMap = new HashMap<String,Object>();
		requestMap.put("payment_type", bankCode);
		Map<String,Object> transaction_details = new HashMap<String,Object>();
		transaction_details.put("order_id", idTransaction);
		transaction_details.put("gross_amount", amount);
		requestMap.put("transaction_details", transaction_details);
		//Map<String,Object> bank_transfer = new HashMap<String,Object>();
		//bank_transfer.put("bank", bankCode);
		//requestMap.put("bank_transfer", bank_transfer);
		
		String requestString = gson.toJson(requestMap);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.header("Authorization", getBasicAuthenticationHeader(apiKey, ""))
				.header("Content-Type", "application/json")
				.header("Accept", "application/json")
				.uri(URI.create(apiEndpoint))
				.POST(HttpRequest.BodyPublishers.ofString(requestString))
				.build();
		String redirect_url = "";
		
		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			DebitCardResponse responseObj = gson.fromJson(rawResponse, DebitCardResponse.class);
			redirect_url = redirect_url + responseObj.getRedirect_url();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return redirect_url;
	}
	
	private static final String getBasicAuthenticationHeader(String username, String password) {
		String valueToEncode = username + ":" + password;
		return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
	}
	
	@Route(url="test/call/debitcard")
	public HashMap<String,Object> testDebitCard(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) return null;
		
		int amount = ((Double) vmjExchange.getRequestBodyForm("amount")).intValue();
		String idTransaction = (String) vmjExchange.getRequestBodyForm("idTransaction");
		String paymentMethods = (String) vmjExchange.getRequestBodyForm("paymentMethods");
		String bankCode = (String) vmjExchange.getRequestBodyForm("bankCode");
		
		HashMap<String,Object> request = new HashMap<String,Object>();
		request.put("amount", amount);
		request.put("idTransaction", idTransaction);
		request.put("bankCode", bankCode);
		Payment result = this.createPayment(request);
		return result.toHashMap();
	}
}

