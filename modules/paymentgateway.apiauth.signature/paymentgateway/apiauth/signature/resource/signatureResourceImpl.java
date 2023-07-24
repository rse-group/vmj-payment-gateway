package paymentgateway.apiauth.signature;

import paymentgateway.apiauth.core.signatureResourceDecorator;
import paymentgateway.apiauth.core.signatureImpl;
import paymentgateway.apiauth.core.signatureResourceComponent;

public class signatureResourceImpl extends signatureResourceDecorator {
    public signatureResourceImpl (signatureResourceComponent record) {
        // to do implement the method
    }

    // @Restriced(permission = "")
    @Route(url="call/signature/save")
    public List<HashMap<String,Object>> saveAPIAuth(VMJExchange vmjExchange){
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) {
			return null;
		}
		  = create(vmjExchange);
		Repository.saveObject();
		return getAll(vmjExchange);
	}

    public APIAuth createAPIAuth(VMJExchange vmjExchange){
		String encryptionKey = (String) vmjExchange.getRequestBodyForm("encryptionKey);
		APIAuthImpl basicauthimpl = (APIAuthImpl) vmjExchange.getRequestBodyForm("basicauthimpl);
		String credential = (String) vmjExchange.getRequestBodyForm("credential);
		
		  = record.create(vmjExchange);
		 deco = Factory.create("paymentgateway.signature.core.signatureImpl", encryptionKey, basicauthimpl, credential);
			return deco;
	}

    // @Restriced(permission = "")
    @Route(url="call/signature/update")
    public HashMap<String, Object> updateAPIAuth(VMJExchange vmjExchange){
		// to do implement the method
	}

	// @Restriced(permission = "")
    @Route(url="call/signature/detail")
    public HashMap<String, Object> getAPIAuth(VMJExchange vmjExchange){
		return record.getAPIAuth(vmjExchange);
	}

	// @Restriced(permission = "")
    @Route(url="call/signature/list")
    public List<HashMap<String,Object>> getAllAPIAuth(VMJExchange vmjExchange){
		List<> List = Repository.getAllObject("_impl");
		return transformListToHashMap(List);
	}

    public List<HashMap<String,Object>> transformAPIAuthListToHashMap(List<APIAuth> APIAuthList){
		List<HashMap<String,Object>> resultList = new ArrayList<HashMap<String,Object>>();
        for(int i = 0; i < APIAuthList.size(); i++) {
            resultList.add(APIAuthList.get(i).toHashMap());
        }

        return resultList;
	}

	// @Restriced(permission = "")
    @Route(url="call/signature/delete")
    public List<HashMap<String,Object>> deleteAPIAuth(VMJExchange vmjExchange){
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) {
			return null;
		}
		
		String idStr = (String) vmjExchange.getRequestBodyForm("");
		int id = Integer.parseInt(idStr);
		Repository.deleteObject(id);
		return getAll(vmjExchange);
	}

	public void generateCredential() {
		// TODO: implement this method
	}
}
