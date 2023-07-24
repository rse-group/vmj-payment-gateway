package paymentgateway.fundtransfer.withuseraccount;

import paymentgateway.fundtransfer.core.FundTransferResourceDecorator;

import java.util.HashMap;
import java.util.List;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import paymentgateway.apiauth.apikey.APIKeyImpl;
import java.util.*;

import paymentgateway.fundtransfer.core.FundTransfer;
import paymentgateway.fundtransfer.core.FundTransferDecorator;
import paymentgateway.fundtransfer.core.FundTransferImpl;
import paymentgateway.fundtransfer.core.FundTransferResourceComponent;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import paymentgateway.fundtransfer.FundTransferFactory;
import prices.auth.vmj.annotations.Restricted;

import com.google.gson.Gson;

public class WithUserAccountResourceImpl extends FundTransferResourceDecorator {
	protected APIKeyImpl APIKey;
	
    public WithUserAccountResourceImpl (FundTransferResourceComponent record) {
        super(record);
    	this.APIKey = new APIKeyImpl();
    	this.APIKey.setAPIKey("<>");
    }

    @Route(url="call/withuseraccount/save")
    public List<HashMap<String,Object>> saveFundTransfer(VMJExchange vmjExchange){
    	try {
    		FundTransfer fundTransfer = createFundTransfer(vmjExchange);
    		System.out.println("saving object");
    		FundTransferRepository.saveObject(fundTransfer);
    		System.out.println("Saved OBJECT");
    		System.out.println(fundTransfer);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		return getAllFundTransfer(vmjExchange);
	}

    public FundTransfer createFundTransfer(VMJExchange vmjExchange){
		System.out.println("Extract All Fields");
		    	
		String bankAccountId = (String) vmjExchange.getRequestBodyForm("bankAccountId");
		
		FundTransfer fundtransferimpl = record.createFundTransfer(vmjExchange);
		FundTransfer fundTransferWithUserAccount = FundTransferFactory.createFundTransfer(
				"paymentgateway.fundtransfer.withuseraccount.WithUserAccountImpl",
				fundtransferimpl,
				bankAccountId
				);

		System.out.println("CREATED WITHUSERFUNDTRANSFER RESOURCE");
		return fundTransferWithUserAccount;
	}
    

    @Route(url="call/withuseraccount/list")
    public List<HashMap<String,Object>> getAllFundTransfer(VMJExchange vmjExchange){
		List<FundTransfer> List = FundTransferRepository.getAllObject("fundtransfer_withuseraccount");
		return transformFundTransferListToHashMap(List);
	}

    public List<HashMap<String,Object>> transformFundTransferListToHashMap(List<FundTransfer> FundTransferList){
		List<HashMap<String,Object>> resultList = new ArrayList<HashMap<String,Object>>();
        for(int i = 0; i < FundTransferList.size(); i++) {
            resultList.add(FundTransferList.get(i).toHashMap());
        }

        return resultList;
	}
    
	public void sendTransfer(VMJExchange vmjExchange) {
		String URL = "https://disburse.sandbox.bnk.to/v2/disbursements";
		
		String bankAccountId = (String) vmjExchange.getRequestBodyForm("bankAccountId");
		String reference = (String) vmjExchange.getRequestBodyForm("reference");
		String description = (String) vmjExchange.getRequestBodyForm("description");
		String amount = (String) vmjExchange.getRequestBodyForm("amount");
		String destinationCode = (String) vmjExchange.getRequestBodyForm("destinationCode");
		String email = (String) vmjExchange.getRequestBodyForm("email");
		String destinationAccountNumber = (String) vmjExchange.getRequestBodyForm("destinationAccountNumber");
		String destinationHolder = (String) vmjExchange.getRequestBodyForm("destinationHolderName");
		
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
    	
    	Gson gson = new Gson();
    	String requestString = gson.toJson(requestMap);
    	
    	HttpClient client = HttpClient.newHttpClient();
    	
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .headers(
                    "x-api-key",  this.APIKey.getAPIKey(),
                    "Content-type", "application/json", 
                    "Accept", "application/json")
                .POST(BodyPublishers.ofString(requestString))
                .build();
    	
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
	
	@Route(url="fundtransfer/call/withuseraccount")
	public List<HashMap<String,Object>> fundTransferController(VMJExchange vmjexchange) {
    	if (vmjexchange.getHttpMethod().equals("OPTIONS")) return null;
    	
    	this.sendTransfer(vmjexchange);
    	List<HashMap<String,Object>> result = this.saveFundTransfer(vmjexchange);
		
		return result;
	}
}
