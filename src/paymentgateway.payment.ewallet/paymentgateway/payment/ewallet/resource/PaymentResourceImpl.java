package paymentgateway.payment.ewallet;

import com.google.gson.Gson;

import java.lang.reflect.*;

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
import paymentgateway.payment.PaymentConfiguration;

public class PaymentResourceImpl extends PaymentResourceDecorator {
	// implement this with author

	protected String apiKey;
	protected String apiEndpoint;
    public PaymentResourceImpl (PaymentResourceComponent record) {
        super(record);
//        this.apiKey = "SB-Mid-server-NVYFqUidEQUTaozWjW77fFWW";
//		this.apiEndpoint = "https://api.sandbox.midtrans.com/v2/charge";
    }

	
	public Payment createPayment(HashMap<String,Object> vmjExchange) {
		String eWalletType = (String) vmjExchange.get("eWalletType");
		Payment transaction = record.createPayment(vmjExchange);
		String eWalletUrl = sendTransaction(vmjExchange);
		Payment ewalletTransaction =
			PaymentFactory.createPayment(
					"paymentgateway.payment.ewallet.PaymentImpl",
					transaction,
					eWalletType,
					eWalletUrl
					);
		return ewalletTransaction;
	}

	protected String sendTransaction(HashMap<String,Object> vmjExchange) {
		HashMap<String,Object> prodEnv = PaymentConfiguration.getProductEnv();
		String apiKey = prodEnv.get("MidtransApiKey").toString();
		String apiEndpoint = prodEnv.get("MidtransApiEndpoint").toString();
		
		System.out.println("apikey: " + apiKey);
		System.out.println("apiEndpoint: " + apiEndpoint);
		String idTransaction = (String) vmjExchange.get("idTransaction");
		int amount = (int) vmjExchange.get("amount");
		String eWalletType = (String) vmjExchange.get("eWalletType");
		
		if (vmjExchange.get("idTransaction") instanceof String) {
			System.out.println("this is string");
		}
		
		if (vmjExchange.get("idTransaction") instanceof Integer) {
			System.out.println("this is int id");
		}
		
		if (vmjExchange.get("amount") instanceof Integer) {
			System.out.println("this is int");
		}
		
		Gson gson = new Gson();
		Map<String,Object> requestMap = new HashMap<String,Object>();
		requestMap.put("payment_type", eWalletType);
		Map<String,Object> transaction_details = new HashMap<String,Object>();
		transaction_details.put("order_id", idTransaction);
		transaction_details.put("gross_amount", amount);
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
		String eWalletUrl = "";
		
		try {
			System.out.println("a");
			System.out.println(request.toString());
			System.out.println("aa");
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(response.toString());
			System.out.println("bb");
			System.out.println("b");
			String rawResponse = response.body().toString();
			System.out.println("raw: " + rawResponse);
			System.out.println("c");
			EWalletResponse responseObj = gson.fromJson(rawResponse, EWalletResponse.class);
			System.out.println("e");
			for(EWalletAction x : responseObj.getActions()) {
				if("generate-qr-code".equals(x.getName())) {
					eWalletUrl = eWalletUrl + x.getUrl();
				}
			}
			System.out.println("f");
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return eWalletUrl;
	}
	
	private static final String getBasicAuthenticationHeader(String username, String password) {
		String valueToEncode = username + ":" + password;
		return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
	}
	
	@Route(url="test/call/ewallet")
	public HashMap<String,Object> testEwallet(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) return null;
		
		int amount = ((Double) vmjExchange.getRequestBodyForm("amount")).intValue();
		String idTransaction = (String) vmjExchange.getRequestBodyForm("idTransaction");
		String eWalletType = (String) vmjExchange.getRequestBodyForm("eWalletType");
		
		HashMap<String,Object> request = new HashMap<String,Object>();
		request.put("amount", amount);
		request.put("idTransaction", idTransaction);
		request.put("eWalletType", eWalletType);
		System.out.println("sebelum");
		Payment result = this.createPayment(request);
		System.out.println("setelah");
		return result.toHashMap();
	}
}

