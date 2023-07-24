package paymentgateway.fundtransfer.scheduled;

import paymentgateway.fundtransfer.core.ScheduledResourceDecorator;
import paymentgateway.fundtransfer.core.ScheduledImpl;
import paymentgateway.fundtransfer.core.ScheduledResourceComponent;

public class ScheduledResourceImpl extends ScheduledResourceDecorator {
    public ScheduledResourceImpl (ScheduledResourceComponent record) {
        // to do implement the method
    }

    // @Restriced(permission = "")
    @Route(url="call/scheduled/save")
    public List<HashMap<String,Object>> saveFundTransfer(VMJExchange vmjExchange){
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) {
			return null;
		}
		  = create(vmjExchange);
		Repository.saveObject();
		return getAll(vmjExchange);
	}

    public FundTransfer createFundTransfer(VMJExchange vmjExchange){
		String scheduleDate = (String) vmjExchange.getRequestBodyForm("scheduleDate);
		FundTransferImpl fundtransferimpl = (FundTransferImpl) vmjExchange.getRequestBodyForm("fundtransferimpl);
		
		  = record.create(vmjExchange);
		 deco = Factory.create("paymentgateway.scheduled.core.ScheduledImpl", scheduleDate, fundtransferimpl);
			return deco;
	}

    // @Restriced(permission = "")
    @Route(url="call/scheduled/update")
    public HashMap<String, Object> updateFundTransfer(VMJExchange vmjExchange){
		// to do implement the method
	}

	// @Restriced(permission = "")
    @Route(url="call/scheduled/detail")
    public HashMap<String, Object> getFundTransfer(VMJExchange vmjExchange){
		return record.getFundTransfer(vmjExchange);
	}

	// @Restriced(permission = "")
    @Route(url="call/scheduled/list")
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
    @Route(url="call/scheduled/delete")
    public List<HashMap<String,Object>> deleteFundTransfer(VMJExchange vmjExchange){
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) {
			return null;
		}
		
		String idStr = (String) vmjExchange.getRequestBodyForm("");
		int id = Integer.parseInt(idStr);
		Repository.deleteObject(id);
		return getAll(vmjExchange);
	}

	public void updateScheduledTransfer() {
		// TODO: implement this method
	}

	public void cancelScheduleTransfer() {
		// TODO: implement this method
	}

	public void retryScheduledTransfer() {
		// TODO: implement this method
	}
}
