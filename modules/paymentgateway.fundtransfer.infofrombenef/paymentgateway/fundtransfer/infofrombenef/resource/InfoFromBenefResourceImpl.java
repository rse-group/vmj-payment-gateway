package paymentgateway.fundtransfer.infofrombenef;

import paymentgateway.fundtransfer.core.InfoFromBenefResourceDecorator;
import paymentgateway.fundtransfer.core.InfoFromBenefImpl;
import paymentgateway.fundtransfer.core.InfoFromBenefResourceComponent;

public class InfoFromBenefResourceImpl extends InfoFromBenefResourceDecorator {
    public InfoFromBenefResourceImpl (InfoFromBenefResourceComponent record) {
        // to do implement the method
    }

    // @Restriced(permission = "")
    @Route(url="call/infofrombenef/save")
    public List<HashMap<String,Object>> saveFundTransfer(VMJExchange vmjExchange){
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) {
			return null;
		}
		  = create(vmjExchange);
		Repository.saveObject();
		return getAll(vmjExchange);
	}

    public FundTransfer createFundTransfer(VMJExchange vmjExchange){
		String url = (String) vmjExchange.getRequestBodyForm("url);
		FundTransferImpl fundtransferimpl = (FundTransferImpl) vmjExchange.getRequestBodyForm("fundtransferimpl);
		
		  = record.create(vmjExchange);
		 deco = Factory.create("paymentgateway.infofrombenef.core.InfoFromBenefImpl", url, fundtransferimpl);
			return deco;
	}

    // @Restriced(permission = "")
    @Route(url="call/infofrombenef/update")
    public HashMap<String, Object> updateFundTransfer(VMJExchange vmjExchange){
		// to do implement the method
	}

	// @Restriced(permission = "")
    @Route(url="call/infofrombenef/detail")
    public HashMap<String, Object> getFundTransfer(VMJExchange vmjExchange){
		return record.getFundTransfer(vmjExchange);
	}

	// @Restriced(permission = "")
    @Route(url="call/infofrombenef/list")
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
    @Route(url="call/infofrombenef/delete")
    public List<HashMap<String,Object>> deleteFundTransfer(VMJExchange vmjExchange){
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) {
			return null;
		}
		
		String idStr = (String) vmjExchange.getRequestBodyForm("");
		int id = Integer.parseInt(idStr);
		Repository.deleteObject(id);
		return getAll(vmjExchange);
	}

	public void cancelTransfer(String id) {
		// TODO: implement this method
	}

	public void sendTransfer(String id, String destinationAccountNumber, String destinationAccountHolderName, String destinationCode) {
		// TODO: implement this method
	}

	public void sendTransferLink(Real amount, String email, String reference, String description) {
		// TODO: implement this method
	}
}
