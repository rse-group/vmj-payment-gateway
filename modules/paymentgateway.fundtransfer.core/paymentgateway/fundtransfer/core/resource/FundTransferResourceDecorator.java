package paymentgateway.fundtransfer.core;
import java.util.*;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

public abstract class FundTransferResourceDecorator extends FundTransferResourceComponent{
	protected FundTransferResourceComponent record;

    public FundTransferResourceDecorator(FundTransferResourceComponent record) {
        this.record = record;
    }

    public List<HashMap<String,Object>> saveFundTransfer(VMJExchange vmjExchange){
		return record.saveFundTransfer(vmjExchange);
	}
    
    public FundTransfer createFundTransfer(VMJExchange vmjExchange) {
    	return record.createFundTransfer(vmjExchange);
    }
    
    public FundTransfer createFundTransfer(VMJExchange vmjExchange, int id) {
    	return record.createFundTransfer(vmjExchange, id);
    }
    
    public HashMap<String, Object> updateFundTransfer(VMJExchange vmjExchange) {
        return record.updateFundTransfer(vmjExchange);
    }

    public HashMap<String, Object> getFundTransfer(VMJExchange vmjExchange) {
        return record.getFundTransfer(vmjExchange);
    }
    
    public List<HashMap<String,Object>> getAllFundTransfer(VMJExchange vmjExchange){
		return record.getAllFundTransfer(vmjExchange);
	}
    
    public List<HashMap<String,Object>> transformFundTransferListToHashMap(List<FundTransfer> fundtransferList){
		return record.transformFundTransferListToHashMap(fundtransferList);
	}
    
    public List<HashMap<String,Object>> deleteFundTransfer(VMJExchange vmjExchange) {
        return record.deleteFundTransfer(vmjExchange);
    }
}
