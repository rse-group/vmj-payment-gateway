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
import paymentgateway.client.brankas.BrankasImpl;
import com.google.gson.Gson;

public class WithUserAccountResourceImpl extends FundTransferResourceDecorator {
	protected APIKeyImpl APIKey;
	protected BrankasImpl paymentGateway;
	
    public WithUserAccountResourceImpl (FundTransferResourceComponent record) {
        super(record);
    	this.APIKey = new APIKeyImpl();
    	this.APIKey.setAPIKey("<>");
    	this.paymentGateway = new BrankasImpl(this.APIKey);
    }

    @Route(url="call/withuseraccount/save")
    public List<HashMap<String,Object>> saveFundTransfer(VMJExchange vmjExchange){
    	try {
    		FundTransfer fundTransfer = createFundTransfer(vmjExchange);
    		FundTransferRepository.saveObject(fundTransfer);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		return getAllFundTransfer(vmjExchange);
	}

    public FundTransfer createFundTransfer(VMJExchange vmjExchange){
		String bankAccountId = (String) vmjExchange.getRequestBodyForm("bankAccountId");
		
		FundTransfer fundtransferimpl = record.createFundTransfer(vmjExchange);
		FundTransfer fundTransferWithUserAccount = FundTransferFactory.createFundTransfer(
				"paymentgateway.fundtransfer.withuseraccount.WithUserAccountImpl",
				fundtransferimpl,
				bankAccountId
				);
		return fundTransferWithUserAccount;
	}
    
    public FundTransfer createFundTransfer(VMJExchange vmjExchange, int id){
		String bankAccountId = (String) vmjExchange.getRequestBodyForm("bankAccountId");
		
		FundTransfer fundtransferimpl = record.createFundTransfer(vmjExchange, id);
		FundTransfer fundTransferWithUserAccount = FundTransferFactory.createFundTransfer(
				"paymentgateway.fundtransfer.withuseraccount.WithUserAccountImpl",
				fundtransferimpl,
				bankAccountId
				);
		return fundTransferWithUserAccount;
	}
    
    @Route(url="call/withuseraccount/update")
    public HashMap<String, Object> updateFundTransfer(VMJExchange vmjExchange) {
    	String idStr = (String) vmjExchange.getRequestBodyForm("id");
		int id = Integer.parseInt(idStr);
		
		FundTransfer fundTransfer = FundTransferRepository.getObject(id);
		
		fundTransfer = this.createFundTransfer(vmjExchange, id);
		
    	FundTransferRepository.updateObject(fundTransfer);
    	return fundTransfer.toHashMap();
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
	
	@Route(url="call/withuseraccount/sendtransfer")
	public List<HashMap<String,Object>> sendTransfer(VMJExchange vmjexchange) {
    	if (vmjexchange.getHttpMethod().equals("OPTIONS")) return null;
    	
    	this.paymentGateway.sendTransfer(vmjexchange);
    	
    	List<HashMap<String,Object>> result = this.saveFundTransfer(vmjexchange);
		
		return result;
	}
}
