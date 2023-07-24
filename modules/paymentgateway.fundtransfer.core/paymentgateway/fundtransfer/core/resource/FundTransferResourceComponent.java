package paymentgateway.fundtransfer.core;
import java.util.*;

import vmj.hibernate.integrator.RepositoryUtil;
import vmj.routing.route.VMJExchange;
//add other required packages

public abstract class FundTransferResourceComponent implements FundTransferResource{
	protected RepositoryUtil<FundTransfer> FundTransferRepository;

    public FundTransferResourceComponent(){
        this.FundTransferRepository = new RepositoryUtil<FundTransfer>(paymentgateway.fundtransfer.core.FundTransferComponent.class);
    }
    
    public abstract List<HashMap<String,Object>> saveFundTransfer(VMJExchange vmjExchange);
    public abstract FundTransfer createFundTransfer(VMJExchange vmjExchange);
    public abstract FundTransfer createFundTransfer(VMJExchange vmjExchange, int id);
    public abstract HashMap<String, Object> updateFundTransfer(VMJExchange vmjExchange);
    public abstract HashMap<String, Object> getFundTransfer(VMJExchange vmjExchange);
    public abstract List<HashMap<String,Object>> getAllFundTransfer(VMJExchange vmjExchange);
    public abstract List<HashMap<String,Object>> transformFundTransferListToHashMap(List<FundTransfer> FundTransferList);
    public abstract List<HashMap<String,Object>> deleteFundTransfer(VMJExchange vmjExchange);
}
