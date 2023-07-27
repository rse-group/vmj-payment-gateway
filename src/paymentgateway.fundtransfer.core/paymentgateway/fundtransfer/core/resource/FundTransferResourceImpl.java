package paymentgateway.fundtransfer.core;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import paymentgateway.fundtransfer.core.FundTransfer;
import paymentgateway.apiauth.apikey.APIKeyImpl;
import java.util.*;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import paymentgateway.fundtransfer.FundTransferFactory;
import prices.auth.vmj.annotations.Restricted;
import paymentgateway.client.oy.OyImpl;

//add other required packages

public class FundTransferResourceImpl extends FundTransferResourceComponent{
	protected APIKeyImpl APIKey;
	protected OyImpl paymentGateway;
	
	public FundTransferResourceImpl () {
    	this.APIKey = new APIKeyImpl();
    	this.APIKey.setAPIKey("c017a92d-286f-4665-ad34-fee920174aaa");
    	this.APIKey.setAPIPassword("joanlamrack");
    	this.paymentGateway = new OyImpl(this.APIKey);
	}
	
    @Route(url="call/fundtransfer/save")
    public List<HashMap<String,Object>> saveFundTransfer(VMJExchange vmjExchange){
		FundTransfer fundTransferReport = createFundTransfer(vmjExchange);
		FundTransferRepository.saveObject(fundTransferReport);
		return getAllFundTransfer(vmjExchange);
	}
    
    public FundTransfer createFundTransfer(VMJExchange vmjExchange){
    	
		String status = (String) vmjExchange.getRequestBodyForm("status");
		String statusDescription = (String) vmjExchange.getRequestBodyForm("statusDescription");
		String reference = (String) vmjExchange.getRequestBodyForm("reference");
		String description = (String) vmjExchange.getRequestBodyForm("description");
		String amount = (String) vmjExchange.getRequestBodyForm("amount");
		String destinationCode = (String) vmjExchange.getRequestBodyForm("destinationCode");
		String destinationHolderName = (String) vmjExchange.getRequestBodyForm("destinationHolderName");
		String email = (String) vmjExchange.getRequestBodyForm("email");
		String senderId = (String) vmjExchange.getRequestBodyForm("senderId");
		String created = (String) vmjExchange.getRequestBodyForm("created");
		String destinationAccountNumber = (String) vmjExchange.getRequestBodyForm("destinationAccountNumber");
    	
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
		return transaction;
	}
    
    public FundTransfer createFundTransfer(VMJExchange vmjExchange, int id){
		String status = (String) vmjExchange.getRequestBodyForm("status");
		String statusDescription = (String) vmjExchange.getRequestBodyForm("statusDescription");
		String reference = (String) vmjExchange.getRequestBodyForm("reference");
		String description = (String) vmjExchange.getRequestBodyForm("description");
		String amount = (String) vmjExchange.getRequestBodyForm("amount");
		String destinationCode = (String) vmjExchange.getRequestBodyForm("destinationCode");
		String destinationHolderName = (String) vmjExchange.getRequestBodyForm("destinationHolderName");
		String email = (String) vmjExchange.getRequestBodyForm("email");
		String senderId = (String) vmjExchange.getRequestBodyForm("senderId");
		String created = (String) vmjExchange.getRequestBodyForm("created");
		String destinationAccountNumber = (String) vmjExchange.getRequestBodyForm("destinationAccountNumber");
    	
		FundTransfer transaction = FundTransferFactory.createFundTransfer(
				"paymentgateway.fundtransfer.core.FundTransferImpl", 
				id,
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
		return transaction;
	}
    
    @Route(url="call/fundtransfer/update")
    public HashMap<String, Object> updateFundTransfer(VMJExchange vmjExchange) {
		String idStr = (String) vmjExchange.getRequestBodyForm("id");
		int id = Integer.parseInt(idStr);
		String status = (String) vmjExchange.getRequestBodyForm("status");
		String statusDescription = (String) vmjExchange.getRequestBodyForm("statusDescription");
		String reference = (String) vmjExchange.getRequestBodyForm("reference");
		String description = (String) vmjExchange.getRequestBodyForm("description");
		String amount = (String) vmjExchange.getRequestBodyForm("amount");
		String destinationCode = (String) vmjExchange.getRequestBodyForm("destinationCode");
		String destinationHolderName = (String) vmjExchange.getRequestBodyForm("destinationHolderName");
		String email = (String) vmjExchange.getRequestBodyForm("email");
		String senderId = (String) vmjExchange.getRequestBodyForm("senderId");
		String created = (String) vmjExchange.getRequestBodyForm("created");
		String destinationAccountNumber = (String) vmjExchange.getRequestBodyForm("destinationAccountNumber");
		
		FundTransfer fundTransfer = FundTransferRepository.getObject(id);
		fundTransfer.setStatus(status);
		fundTransfer.setStatusDescription(statusDescription);
		fundTransfer.setReference(reference);
		fundTransfer.setDescription(description);
		fundTransfer.setAmount(Integer.parseInt(amount));
		fundTransfer.setDestinationCode(destinationCode);
		fundTransfer.setEmail(email);
		fundTransfer.setSenderId(senderId);
		fundTransfer.setCreated(created);
		fundTransfer.setDestinationAccountNumber(destinationAccountNumber);
		
		FundTransferRepository.updateObject(fundTransfer);
		return fundTransfer.toHashMap();
    }
    
    @Route(url="call/fundtransfer/list")
    public List<HashMap<String,Object>> getAllFundTransfer(VMJExchange vmjExchange){
		List<FundTransfer> FundTransferList = FundTransferRepository.getAllObject("fundtransfer_impl");
		return transformFundTransferListToHashMap(FundTransferList);
	}
    
    @Route(url="call/fundtransfer/detail")
    public HashMap<String, Object> getFundTransfer(VMJExchange vmjExchange) {
        String idStr = vmjExchange.getGETParam("id");
        int id = Integer.parseInt(idStr);
        FundTransfer fundTransfer = FundTransferRepository.getObject(id);
        return fundTransfer.toHashMap();
    }
    
    @Route(url="call/fundtransfer/list")
    public List<HashMap<String,Object>> transformFundTransferListToHashMap(List<FundTransfer> FundTransferList){
		List<HashMap<String,Object>> resultList = new ArrayList<HashMap<String,Object>>();
        for(int i = 0; i < FundTransferList.size(); i++) {
            resultList.add(FundTransferList.get(i).toHashMap());
        }

        return resultList;
	}
    
    @Route(url="call/fundtransfer/delete")
    public List<HashMap<String,Object>> deleteFundTransfer(VMJExchange vmjExchange) {
        String idStr = (String) vmjExchange.getRequestBodyForm("id");
        int id = Integer.parseInt(idStr);
        FundTransferRepository.deleteObject(id);
        return getAllFundTransfer(vmjExchange);
    }
    
    @Route(url="call/fundtransfer/sendtransfer")
    public List<HashMap<String,Object>> sendTransfer(VMJExchange vmjexchange) {
    	if (vmjexchange.getHttpMethod().equals("OPTIONS")) return null;
    	
    	this.paymentGateway.sendTransfer(vmjexchange);
    	
    	List<HashMap<String,Object>> result = this.saveFundTransfer(vmjexchange);
		
		return result;
    }
}
