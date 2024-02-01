package paymentgateway.payment.virtualaccount;

import com.google.gson.Gson;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import paymentgateway.payment.PaymentFactory;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentResourceDecorator;
import paymentgateway.payment.core.PaymentImpl;
import paymentgateway.payment.core.PaymentResourceComponent;

public class PaymentResourceImpl extends PaymentResourceDecorator {
	// implement this to work with authorization module
	protected String apiKey;
	protected String apiEndpoint;
    public PaymentResourceImpl (PaymentResourceComponent record) {
        super(record);
        this.apiKey = "SB-Mid-server-NVYFqUidEQUTaozWjW77fFWW";
		this.apiEndpoint = "https://api.sandbox.midtrans.com/v2/charge";
    }
    
	public Payment createPayment(HashMap<String,Object> vmjExchange) {
		String bankCode = (String) vmjExchange.get("bankCode");
		boolean isOpenVA = (boolean) vmjExchange.get("isOpenVA");
		
		Payment transaction = record.createPayment(vmjExchange);
		String vaAccountNumber = sendTransaction(vmjExchange);
		Payment virtualAccountTransaction = PaymentFactory.createPayment(
				"paymentgateway.payment.virtualaccount.PaymentImpl", transaction, bankCode, isOpenVA, vaAccountNumber);
		return virtualAccountTransaction;
	}
	
	protected String sendTransaction(HashMap<String,Object> vmjExchange) {
		String idTransaction = (String) vmjExchange.get("idTransaction");
		int amount = (int) vmjExchange.get("amount");
		String bankCode = (String) vmjExchange.get("bankCode");
		
		Gson gson = new Gson();
		Map<String,Object> requestMap = new HashMap<String,Object>();
		requestMap.put("payment_type", "bank_transfer");
		Map<String,Object> transaction_details = new HashMap<String,Object>();
		transaction_details.put("order_id", idTransaction);
		transaction_details.put("gross_amount", amount);
		requestMap.put("transaction_details", transaction_details);
		Map<String,Object> bank_transfer = new HashMap<String,Object>();
		bank_transfer.put("bank", bankCode);
		requestMap.put("bank_transfer", bank_transfer);
		
		String requestString = gson.toJson(requestMap);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.header("Authorization", getBasicAuthenticationHeader(apiKey, ""))
				.header("Content-Type", "application/json")
				.header("Accept", "application/json")
				.uri(URI.create(apiEndpoint))
				.POST(HttpRequest.BodyPublishers.ofString(requestString))
				.build();
		String vaAccountNumber = "";
		
		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			VirtualAccountResponse responseObj = gson.fromJson(rawResponse, VirtualAccountResponse.class);
			vaAccountNumber = vaAccountNumber + ((responseObj.getVa_numbers()).get(0)).getVa_number();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return vaAccountNumber;
	}
	
	private static final String getBasicAuthenticationHeader(String username, String password) {
		String valueToEncode = username + ":" + password;
		return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
	}
	
	@Route(url="test/call/virtualaccount")
	public HashMap<String,Object> testVirtualAccount(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) return null;
		
		int amount = ((Double) vmjExchange.getRequestBodyForm("amount")).intValue();
		String idTransaction = (String) vmjExchange.getRequestBodyForm("idTransaction");
		String bankCode = (String) vmjExchange.getRequestBodyForm("bankCode");
		boolean isOpenVA = (boolean) vmjExchange.getRequestBodyForm("isOpenVA");
		
		HashMap<String,Object> pgExchange = new HashMap<String,Object>();
		pgExchange.put("idTransaction", idTransaction);
		pgExchange.put("amount", amount);
		pgExchange.put("bankCode", bankCode);
		pgExchange.put("isOpenVA", isOpenVA);
		Payment result = this.createPayment(pgExchange);
		return result.toHashMap();
	}
}

