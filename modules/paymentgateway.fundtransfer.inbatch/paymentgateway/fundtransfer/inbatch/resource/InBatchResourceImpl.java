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
import paymentgateway.client.xendit.XenditImpl;
import paymentgateway.apiauth.basicauth.basicAuthImpl;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import prices.auth.vmj.annotations.Restricted;
import paymentgateway.fundtransfer.FundTransferFactory;

public class InBatchResourceImpl extends FundTransferResourceDecorator {
	protected basicAuthImpl basicAuth;
	protected XenditImpl paymentGateway;
	protected FundTransferResourceComponent record;
	
    public InBatchResourceImpl(FundTransferResourceComponent record) {
        super(record);
        this.basicAuth = new basicAuthImpl();
        this.basicAuth.setUsername("<>");
        this.basicAuth.setPassword("");
        this.paymentGateway = new XenditImpl(this.basicAuth);
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
         return transaction;
    }
    
    
    public List<HashMap<String,Object>> createFundTransfersAndSave(VMJExchange vmjExchange){
    	List<HashMap<String,Object>> transfers = (List<HashMap<String,Object>>) vmjExchange.getRequestBodyForm("transfers");
		
        for (int i = 0; i < transfers.size(); i++) {
            FundTransfer convertedTransfer = transformHashMapToFundTransfer(transfers.get(i));
    		
            FundTransferRepository.saveObject(convertedTransfer);
        }
        
		return getAllFundTransfer(vmjExchange);
	}
	
    @Route(url="call/fundtransfer/batch")
    public List<HashMap<String,Object>> sendTransfer(VMJExchange vmjExchange){
    	this.paymentGateway.sendTransfer(vmjExchange);
		return createFundTransfersAndSave(vmjExchange);
	}
}
