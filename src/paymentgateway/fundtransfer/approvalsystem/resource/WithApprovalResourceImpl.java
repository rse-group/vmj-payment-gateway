package paymentgateway.fundtransfer.withapproval;

import paymentgateway.fundtransfer.core.WithApprovalResourceDecorator;
import paymentgateway.fundtransfer.core.WithApprovalImpl;
import paymentgateway.fundtransfer.core.WithApprovalResourceComponent;

public class WithApprovalResourceImpl extends FundTransferResourceDecorator {
    public WithApprovalResourceImpl (WithApprovalResourceComponent record) {
        // to do implement the method
    }

    // @Restriced(permission = "")
    @Route(url="call/withapproval/save")
    public List<HashMap<String,Object>> saveFundTransfer(VMJExchange vmjExchange){
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) {
			return null;
		}
		  = create(vmjExchange);
		Repository.saveObject();
		return getAll(vmjExchange);
	}

    public FundTransfer createFundTransfer(VMJExchange vmjExchange){
		String approverId = (String) vmjExchange.getRequestBodyForm("approverId);
		FundTransferImpl fundtransferimpl = (FundTransferImpl) vmjExchange.getRequestBodyForm("fundtransferimpl);
		
		  = record.create(vmjExchange);
		 deco = Factory.create("paymentgateway.withapproval.core.WithApprovalImpl", approverId, fundtransferimpl);
			return deco;
	}

    // @Restriced(permission = "")
    @Route(url="call/withapproval/update")
    public HashMap<String, Object> updateFundTransfer(VMJExchange vmjExchange){
		// to do implement the method
	}

	// @Restriced(permission = "")
    @Route(url="call/withapproval/detail")
    public HashMap<String, Object> getFundTransfer(VMJExchange vmjExchange){
		return record.getFundTransfer(vmjExchange);
	}

	// @Restriced(permission = "")
    @Route(url="call/withapproval/list")
    public List<HashMap<String,Object>> getAllFundTransfer(VMJExchange vmjExchange){
		List<> List = Repository.getAllObject("_impl");
		return transformListToHashMap(List);
	}

    public List<HashMap<String,Object>> transformFundTransferListToHashMap(List<FundTransfer> FundTransferList){
		List<HashMap<String,Object>> resultList = new ArrayList<HashMap<String,Object>>();
        for(int i = 0; i < FundTransferList.size(); i++) {
            resultList.add(FundTransferList.get(i).toHashMap());
        }

        return resultList;
	}

	// @Restriced(permission = "")
    @Route(url="call/withapproval/delete")
    public List<HashMap<String,Object>> deleteFundTransfer(VMJExchange vmjExchange){
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) {
			return null;
		}
		
		String idStr = (String) vmjExchange.getRequestBodyForm("approverId");
		int id = Integer.parseInt(idStr);
		Repository.deleteObject(id);
		return getAll(vmjExchange);
	}

	public void approveTransfer(String transactionId) {
		// TODO: implement this method
	}

	public void rejectTransfer(String transactionId) {
		// TODO: implement this method
	}
}
