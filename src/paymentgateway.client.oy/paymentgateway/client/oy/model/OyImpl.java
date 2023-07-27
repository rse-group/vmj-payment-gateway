package paymentgateway.client.oy;

import java.util.Map;
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

public class OyImpl extends ClientDecorator {
	private APIKeyImpl apiKeyAuth;
	private Gson gson;
	
	public OyImpl(APIKeyImpl apiKeyAuth) {
		this.apiKeyAuth = apiKeyAuth;
		this.gson = new Gson();
	}

	public void sendTransfer(VMJExchange request) {
		String URL = "https://api-stg.oyindonesia.com/api/remit";
        Map<String, Object> requestObject = this.buildPaymentGatewayRequest(request);

        String requestString = this.gson.toJson(requestObject);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest requestHttpObject = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .headers(
                        "X-OY-Username", this.apiKeyAuth.getAPIPassword(),
                        "X-Api-Key", this.apiKeyAuth.getAPIKey(),
                        "Content-type", "application/json",
                        "Accept", "application/json")
                .POST(BodyPublishers.ofString(requestString))
                .build();

        try {
            HttpResponse<String> response = client.send(
                    requestHttpObject,
                    HttpResponse.BodyHandlers.ofString());
            String rawResponse = response.body().toString();
            FundTransferResponse responseObj = this.gson.fromJson(rawResponse, FundTransferResponse.class);
            System.out.println(responseObj);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
	}

	public Map<String, Object> buildPaymentGatewayRequest(VMJExchange request) {
    	String reference = (String) request.getRequestBodyForm("reference");
		String description = (String) request.getRequestBodyForm("description");
		String amount = (String) request.getRequestBodyForm("amount");
		String destinationCode = (String) request.getRequestBodyForm("destinationCode");
		String email = (String) request.getRequestBodyForm("email");
		String destinationAccountNumber = (String) request.getRequestBodyForm("destinationAccountNumber");
    	
    	Map<String,Object> requestMap = new HashMap<String,Object>();
    	requestMap.put("recipient_bank", destinationCode);
    	requestMap.put("recipient_account", destinationAccountNumber);
    	requestMap.put("amount", amount);
    	requestMap.put("note", description);
    	requestMap.put("partner_trx_id", reference);
    	requestMap.put("email", email);
    	
        return requestMap;
	}
}
