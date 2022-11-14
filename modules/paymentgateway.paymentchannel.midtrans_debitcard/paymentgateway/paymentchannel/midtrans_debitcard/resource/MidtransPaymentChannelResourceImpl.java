package paymentgateway.paymentchannel.midtrans_debitcard;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.util.*;

import com.google.gson.Gson;

import paymentgateway.paymentchannel.core.PaymentChannel;
import paymentgateway.paymentchannel.core.PaymentChannelResourceComponent;
import paymentgateway.paymentchannel.debitcard.PaymentChannelResourceImpl;

public class MidtransPaymentChannelResourceImpl extends PaymentChannelResourceImpl {
	protected String apiKey;
	protected String apiEndpoint;
	public MidtransPaymentChannelResourceImpl(PaymentChannelResourceComponent record) {
		super(record);
		this.apiKey = "SB-Mid-server-NVYFqUidEQUTaozWjW77fFWW";
		this.apiEndpoint = "https://api.sandbox.midtrans.com/v2/charge";
	}
	
	@Override
	protected String sendChannelDebitCard(HashMap<String,Object> pgExchange) {
		int amount = (int) pgExchange.get("amount");
		String idTransaction = (String) pgExchange.get("idTransaction");
		String bankCode = (String) pgExchange.get("bankCode");
		
		Gson gson = new Gson();
		Map<String,Object> requestMap = new HashMap<String,Object>();
		requestMap.put("payment_type", bankCode);
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
		String redirect_url = "";
		
		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			MidtransDebitCardResponse responseObj = gson.fromJson(rawResponse, MidtransDebitCardResponse.class);
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
	
	@Route(url="test/call/midtrans/debitcard")
	public HashMap<String,Object> midtransPaymentLink(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) return null;
		HashMap<String,Object> request = new HashMap<String,Object>();
		request.put("amount", 12000);
		int randomId = Math.abs((new Random()).nextInt());
		request.put("idTransaction", "test-midtrans-debitcard-" + randomId);
		request.put("bankCode", "bri_epay");
		PaymentChannel result = this.createChannel(request);
		return result.toHashMap();
	}
}