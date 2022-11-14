package paymentgateway.paymentchannel.oy_ewallet;

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
import paymentgateway.paymentchannel.ewallet.PaymentChannelResourceImpl;


public class OyPaymentChannelResourceImpl extends PaymentChannelResourceImpl {
	protected String apiKey;
	protected String apiUsername;
	protected String apiEndpoint;
	public OyPaymentChannelResourceImpl(PaymentChannelResourceComponent record) {
		super(record);
		this.apiKey = "60c97bc8-ac93-4ae2-806b-76f361235535";
		this.apiUsername = "tsaqif31";
		this.apiEndpoint = "https://api-stg.oyindonesia.com/api/e-wallet-aggregator/create-transaction";
	}
	
	@Override
	protected String sendChannelEWallet(HashMap<String,Object> pgExchange) {
		int amount = (int) pgExchange.get("amount");
		String idTransaction = (String) pgExchange.get("idTransaction");
		String eWalletType = (String) pgExchange.get("eWalletType");
		String idCustomer = (String) pgExchange.get("idCustomer");
		String successRedirectUrl = (String) pgExchange.get("successRedirectUrl");
		
		Gson gson = new Gson();
		Map<String,Object> requestMap = new HashMap<String,Object>();
		requestMap.put("customer_id", idCustomer);
		requestMap.put("partner_trx_id", idTransaction);
		requestMap.put("amount", amount);
		requestMap.put("ewallet_code", eWalletType);
		requestMap.put("success_redirect_url", successRedirectUrl);
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
		String eWalletUrl = "";
		
		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			OyEWalletResponse responseObj = gson.fromJson(rawResponse, OyEWalletResponse.class);
			eWalletUrl = eWalletUrl + responseObj.getEwallet_url();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return eWalletUrl;
	}
	
	@Route(url="test/call/oy/ewallet")
	public HashMap<String,Object> oyEwalletLink(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) return null;
		HashMap<String,Object> request = new HashMap<String,Object>();
		request.put("amount", 36000);
		int randomId = Math.abs((new Random()).nextInt());
		request.put("idTransaction", "test-oy-ewallet-" + randomId);
		request.put("eWalletType", "shopeepay_ewallet");
		request.put("idCustomer", "anya");
		request.put("successRedirectUrl", "https://myweb.com/user/redirect");
		PaymentChannel result = this.createChannel(request);
		return result.toHashMap();
	}
}