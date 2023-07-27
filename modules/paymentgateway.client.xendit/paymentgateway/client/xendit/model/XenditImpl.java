package paymentgateway.client.xendit;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.*;
import com.google.gson.Gson;

import vmj.routing.route.VMJExchange;
import paymentgateway.apiauth.basicauth.basicAuthImpl;

import paymentgateway.client.core.ClientDecorator;

public class XenditImpl extends ClientDecorator {
	public basicAuthImpl basicAuth;
	private Gson gson;
	
	public XenditImpl(basicAuthImpl basicAuth) {
		this.basicAuth = basicAuth;
		this.gson = new Gson();
	}

	public void sendTransfer(VMJExchange request) {
		String URL = "https://api.xendit.co/batch_disbursements";
        Map<String, Object> requestObject = this.buildPaymentGatewayRequest(request);

        String requestString = this.gson.toJson(requestObject);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest requestHttpObject = HttpRequest.newBuilder()
        		.uri(URI.create(URL))
                .headers(
                    "Authorization", "Basic " + this.basicAuth.generateCredential(), 
                    "Content-type", "application/json",
                    "Accept", "application/json"
                )
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
		List<Map<String, Object>> transferInput = (List<Map<String, Object>>) request.getRequestBodyForm("transfers");
		Map<String, Object> requestMap = new HashMap<String, Object>();
		
		Random r = new Random();
		String batchId = Integer.toString(Math.abs(r.nextInt()));
		
		requestMap.put("reference", batchId);
		
		List<Map<String,Object>> batchDisbursement = transfersListTransferToPaymentGatewayFormat(transferInput);
		
		requestMap.put("disbursements", batchDisbursement);
    	
        return requestMap;
	}
	
    private List<Map<String,Object>> transfersListTransferToPaymentGatewayFormat(List<Map<String,Object>> transfers) {
    	List<Map<String,Object>> output = new ArrayList<>();
    	
        for (int i = 0; i < transfers.size(); i++) {
            Map<String, Object> convertedTransfer = transformTransferToPaymentGatewayFormat(transfers.get(i));
    		
            output.add(convertedTransfer);
        }
        
        return output;
    }
    
    private Map<String, Object> transformTransferToPaymentGatewayFormat(Map<String, Object> transfer) {
    	String status = (String) transfer.get("status");
        String statusDescription = (String) transfer.get("statusDescription"); 
        String reference = (String) transfer.get("reference");
        String description = (String) transfer.get("statusDescription"); 
        Integer amount = Integer.parseInt((String) transfer.get("amount")); 
        String destinationCode = (String) transfer.get("destinationCode"); 
        String destinationHolderName = (String) transfer.get("destinationHolderName"); 
        String email = (String) transfer.get("email"); 
        String senderId = (String) transfer.get("senderId"); 
        String created = (String) transfer.get("created"); 
        String destinationAccountNumber = (String) transfer.get("destinationAccountNumber"); 
        
        Map<String,Object> request  = new HashMap<String, Object>();
        
        request.put("external_id", reference);
        request.put("bank_code", destinationCode);
        request.put("bank_account_name", destinationHolderName);
        request.put("bank_account_number", destinationAccountNumber);
        request.put("description", destinationAccountNumber);
        request.put("amount", amount);
        
        return request;
    }

}
