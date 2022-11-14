package paymentgateway.paymentchannel.oy_retailoutlet;

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
import paymentgateway.paymentchannel.retailoutlet.PaymentChannelResourceImpl;

public class OyPaymentChannelResourceImpl extends PaymentChannelResourceImpl {
	protected String apiKey;
	protected String apiUsername;
	protected String apiEndpoint;
	public OyPaymentChannelResourceImpl(PaymentChannelResourceComponent record) {
		super(record);
		this.apiKey = "60c97bc8-ac93-4ae2-806b-76f361235535";
		this.apiUsername = "tsaqif31";
		this.apiEndpoint = "https://api-stg.oyindonesia.com/api/offline-create";
	}
	
	@Override
	protected String sendChannelRetailOutlet(HashMap<String,Object> pgExchange) {
		int amount = (int) pgExchange.get("amount");
		String idTransaction = (String) pgExchange.get("idTransaction");
		String retailOutlet = (String) pgExchange.get("retailOutlet");
		String idCustomer = (String) pgExchange.get("idCustomer");
		String transaction_type = (String) pgExchange.get("transaction_type");
		
		Gson gson = new Gson();
		Map<String,Object> requestMap = new HashMap<String,Object>();
		requestMap.put("partner_trx_id", idTransaction);
		requestMap.put("customer_id", idCustomer);
		requestMap.put("amount", amount);
		requestMap.put("transaction_type", transaction_type);
		requestMap.put("offline_channel", retailOutlet);
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
		String retailPaymentCode = "";
		
		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			System.out.println(rawResponse);
			OyRetailOutletResponse responseObj = gson.fromJson(rawResponse, OyRetailOutletResponse.class);
			retailPaymentCode = retailPaymentCode + responseObj.getCode();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return retailPaymentCode;
	}
	
	@Route(url="test/call/oy/retailoutlet")
	public HashMap<String,Object> oyEwalletLink(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) return null;
		HashMap<String,Object> request = new HashMap<String,Object>();
		request.put("amount", 36000);
		int randomId = Math.abs((new Random()).nextInt());
		request.put("idTransaction", "test-oy-retailoutlet-" + randomId);
		request.put("retailOutlet", "INDOMARET");
		request.put("idCustomer", "anya");
		request.put("transaction_type", "CASH_IN");
		PaymentChannel result = this.createChannel(request);
		return result.toHashMap();
	}
}