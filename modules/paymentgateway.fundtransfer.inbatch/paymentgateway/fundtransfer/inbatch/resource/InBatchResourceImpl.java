package paymentgateway.fundtransfer.inbatch;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.*;

import paymentgateway.fundtransfer.core.FundTransfer;
import paymentgateway.fundtransfer.core.FundTransferResourceDecorator;
import paymentgateway.fundtransfer.core.FundTransferImpl;
import paymentgateway.fundtransfer.core.FundTransferResourceComponent;
import paymentgateway.apiauth.basicauth.basicAuthImpl;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import prices.auth.vmj.annotations.Restricted;
import paymentgateway.fundtransfer.FundTransferFactory;

public class InBatchResourceImpl extends FundTransferResourceDecorator {
	protected basicAuthImpl basicAuth;
	protected FundTransferResourceComponent record;
	
    public InBatchResourceImpl(FundTransferResourceComponent record) {
        super(record);
        this.basicAuth = new basicAuthImpl();
        this.basicAuth.setUsername("<>");
        this.basicAuth.setPassword("");
    }
    
    @Route(url="call/fundtransfer/save")
    public List<HashMap<String,Object>> saveFundTransfer(VMJExchange vmjExchange){
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) {
			return null;
		}
		System.out.println("saveFundTransfer");
		FundTransfer fundtransfer = createFundTransfer(vmjExchange);
		FundTransferRepository.saveObject(fundtransfer);
		return getAllFundTransfer(vmjExchange);
	}
    
    public List<HashMap<String,Object>> saveFundTransfers(VMJExchange vmjExchange){
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) {
			return null;
		}
		return createFundTransfersAndSave(vmjExchange);
	}
    
    @Route(url="call/fundtransfer/list")
    public List<HashMap<String,Object>> getAllFundTransfer(VMJExchange vmjExchange){
    	System.out.println("getAllFundTransfer");
		List<FundTransfer> FundTransferList = FundTransferRepository.getAllObject("fundtransfer_impl");
		return transformFundTransferListToHashMap(FundTransferList);
	}
    

    public List<HashMap<String,Object>> transformFundTransferListToHashMap(List<FundTransfer> FundTransferList){
		List<HashMap<String,Object>> resultList = new ArrayList<HashMap<String,Object>>();
        for(int i = 0; i < FundTransferList.size(); i++) {
            resultList.add(FundTransferList.get(i).toHashMap());
        }

        return resultList;
	}
    
    public FundTransfer transformHashMapToFundTransfer(Map<String, Object> transfer){
    	 String status = (String) transfer.get("status");
         String statusDescription = (String) transfer.get("statusDescription"); 
         String reference = (String) transfer.get("reference");
         String description = (String) transfer.get("statusDescription"); 
         String amount = (String) transfer.get("amount"); 
         String destinationCode = (String) transfer.get("destinationCode"); 
         String destinationHolderName = (String) transfer.get("destinationHolderName"); 
         String email = (String) transfer.get("email"); 
         String senderId = (String) transfer.get("senderId"); 
         String created = (String) transfer.get("created"); 
         String destinationAccountNumber = (String) transfer.get("destinationAccountNumber"); 
         		
         FundTransfer transaction = FundTransferFactory.createFundTransfer(
 				"paymentgateway.fundtransfer.core.FundTransferImpl",
 				status, 
 				statusDescription,
 				reference,
 				description,
 				Integer.parseInt(amount),
 				destinationCode,
 				destinationHolderName,
 				email,
 				senderId,
 				created,
 				destinationAccountNumber
 				);
         
         System.out.println(transaction);
         
         return transaction;
    }
    
    public Map<String, Object> transformTransferToPaymentGatewayFormat(Map<String, Object> transfer) {
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
    
    public List<Map<String,Object>> transfersListTransferToPaymentGatewayFormat(List<Map<String,Object>> transfers) {
    	List<Map<String,Object>> output = new ArrayList<>();
    	
        for (int i = 0; i < transfers.size(); i++) {
            Map<String, Object> convertedTransfer = transformTransferToPaymentGatewayFormat(transfers.get(i));
    		
            output.add(convertedTransfer);
        }
        
        return output;
    }

    public List<HashMap<String,Object>> createFundTransfersAndSave(VMJExchange vmjExchange){
    	List<HashMap<String,Object>> transfers = (List<HashMap<String,Object>>) vmjExchange.getRequestBodyForm("transfers");
		
        for (int i = 0; i < transfers.size(); i++) {
            FundTransfer convertedTransfer = transformHashMapToFundTransfer(transfers.get(i));
    		
            FundTransferRepository.saveObject(convertedTransfer);
        }
        
		return getAllFundTransfer(vmjExchange);
	}
    
	public void sendTransfer(VMJExchange vmjExchange) {
		String URL = "https://api.xendit.co/batch_disbursements";
		
		List<Map<String, Object>> transferInput = (List<Map<String, Object>>) vmjExchange.getRequestBodyForm("transfers");
		
		Map<String, Object> requestMap = new HashMap<String, Object>();
		
		Random r = new Random();
		String batchId = Integer.toString(Math.abs(r.nextInt()));
		
		requestMap.put("reference", batchId);
		
		List<Map<String,Object>> batchDisbursement = transfersListTransferToPaymentGatewayFormat(transferInput);
		
		requestMap.put("disbursements", batchDisbursement);
		
		Gson gson = new Gson();
    	String requestString = gson.toJson(requestMap);
    	
    	System.out.println(requestString);
    	
    	HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
        		.uri(URI.create(URL))
                .headers(
                    "Authorization", "Basic " + this.basicAuth.generateCredential(), 
                    "Content-type", "application/json",
                    "Accept", "application/json"
                )
                .POST(BodyPublishers.ofString(requestString))
                .build();
    	
        System.out.println(request);
        
		try {
			System.out.println("SendTransfer");
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			System.out.println(rawResponse);
			FundTransferResponse responseObj = gson.fromJson(rawResponse, FundTransferResponse.class);
			System.out.println(responseObj);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
    @Route(url="fundtransfer/call/inbatch")
    public List<HashMap<String,Object>> batchController(VMJExchange vmjExchange){
    	
    	sendTransfer(vmjExchange);
		return createFundTransfersAndSave(vmjExchange);
	}
}
