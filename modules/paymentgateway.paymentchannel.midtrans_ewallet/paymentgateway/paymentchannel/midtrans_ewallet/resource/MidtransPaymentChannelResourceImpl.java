package paymentgateway.paymentchannel.midtrans_ewallet;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import java.net.URI;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.util.*;

import com.google.gson.Gson;

import paymentgateway.paymentchannel.core.PaymentChannel;
import paymentgateway.paymentchannel.core.PaymentChannelResourceComponent;
import paymentgateway.paymentchannel.ewallet.PaymentChannelResourceImpl;

public class MidtransPaymentChannelResourceImpl extends PaymentChannelResourceImpl {
	protected String apiKey;
	protected String apiEndpoint;
	public MidtransPaymentChannelResourceImpl(PaymentChannelResourceComponent record) {
		super(record);
		this.apiKey = "SB-Mid-server-NVYFqUidEQUTaozWjW77fFWW";
		this.apiEndpoint = "https://api.sandbox.midtrans.com/v2/charge";
	}
	
	@Override
	protected String sendChannelEWallet(HashMap<String,Object> pgExchange) {
		int amount = (int) pgExchange.get("amount");
		String idTransaction = (String) pgExchange.get("idTransaction");
		String eWalletType = (String) pgExchange.get("eWalletType");
		
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
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			MidtransEWalletResponse responseObj = gson.fromJson(rawResponse, MidtransEWalletResponse.class);
			for(MidtransAction x : responseObj.getActions()) {
				if("generate-qr-code".equals(x.getName())) {
					eWalletUrl = eWalletUrl + x.getUrl();
				}
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return eWalletUrl;
	}
	
	private static final String getBasicAuthenticationHeader(String username, String password) {
		String valueToEncode = username + ":" + password;
		return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
	}
	
	@Route(url="test/call/midtrans/ewallet")
	public HashMap<String,Object> midtransdPaymentLink(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) return null;
		HashMap<String,Object> request = new HashMap<String,Object>();
		request.put("amount", 12000);
		int randomId = Math.abs((new Random()).nextInt());
		request.put("idTransaction", "test-midtrans-ewallet-" + randomId);
		request.put("eWalletType", "gopay");
		PaymentChannel result = this.createChannel(request);
		return result.toHashMap();
	}
}