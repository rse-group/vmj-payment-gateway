package paymentgateway.client.brankas;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.*;
import com.google.gson.Gson;

import vmj.routing.route.VMJExchange;
import paymentgateway.apiauth.apikey.APIKeyImpl;

import paymentgateway.client.core.ClientDecorator;

public class BrankasImpl extends ClientDecorator {
	private APIKeyImpl apiKeyAuth;
	private Gson gson;
	public BrankasImpl(APIKeyImpl apiKeyAuth) {
		this.apiKeyAuth = apiKeyAuth;
		this.gson = new Gson();
	}

	public void sendTransfer(VMJExchange request) {
		String URL = "https://disburse.sandbox.bnk.to/v2/disbursements";
		Map<String, Object> requestObject = this.buildPaymentGatewayRequest(request);
		
		String requestString = this.gson.toJson(requestObject);		
		HttpClient client = HttpClient.newHttpClient();
        HttpRequest requestHttpObject = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .headers(
                    "x-api-key",  this.apiKeyAuth.getAPIKey(),
                    "Content-type", "application/json", 
                    "Accept", "application/json")
                .POST(BodyPublishers.ofString(requestString))
                .build();
		try {
			HttpResponse response = client.send(requestHttpObject, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			FundTransferResponse responseObj = gson.fromJson(rawResponse, FundTransferResponse.class);
			System.out.println(responseObj);
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		
	}

	public Map<String,Object> buildPaymentGatewayRequest(VMJExchange request) {
		String bankAccountId = (String) request.getRequestBodyForm("bankAccountId");
		String reference = (String) request.getRequestBodyForm("reference");
		String description = (String) request.getRequestBodyForm("description");
		String amount = (String) request.getRequestBodyForm("amount");
		String destinationCode = (String) request.getRequestBodyForm("destinationCode");
		String destinationAccountNumber = (String) request.getRequestBodyForm("destinationAccountNumber");
		String destinationHolder = (String) request.getRequestBodyForm("destinationHolderName");
		
		Map<String,Object> destinationAmount  = new HashMap<String, Object>();
		destinationAmount.put("num", amount);
		destinationAmount.put("cur", "IDR");
		
		Map<String,Object> address = new HashMap<String,Object>();
		address.put("line1", "Address");
		address.put("line2", "Address");
		address.put("city", "City Name");
		address.put("province", "Province name");
		address.put("zip_code", "123456");
		address.put("country", "Indonesia");
		
		Map<String,Object> destinationAccount = new HashMap<String,Object>();
		destinationAccount.put("bank", destinationCode);
		destinationAccount.put("number", destinationAccountNumber);
		destinationAccount.put("holder_name", destinationHolder);
		destinationAccount.put("address", address);
		destinationAccount.put("type", "PERSONAL");
		
    	Map<String,Object> disbursementRequestMap = new HashMap<String,Object>();
    	disbursementRequestMap.put("merchant_txn_id", reference);
    	disbursementRequestMap.put("reference_id", reference);
    	disbursementRequestMap.put("type", "PAYMENT");
    	disbursementRequestMap.put("destination_account", destinationAccount);
    	disbursementRequestMap.put("destination_amount", destinationAmount);
		
    	ArrayList disbursementList = new ArrayList();
    	disbursementList.add(disbursementRequestMap);
    	
    	Map<String,Object> requestMap = new HashMap<String,Object>();
    	requestMap.put("source_account_id", bankAccountId);
    	requestMap.put("remark", description);
    	requestMap.put("disbursements", disbursementList);
    	
    	return requestMap;
	}

}
