package paymentgateway.apiauth.oauth;

import paymentgateway.apiauth.core.oAuthResourceDecorator;
import paymentgateway.apiauth.core.oAuthImpl;
import paymentgateway.apiauth.core.oAuthResourceComponent;

public class oAuthResourceImpl extends oAuthResourceDecorator {
    public oAuthResourceImpl (oAuthResourceComponent record) {
        // to do implement the method
    }

    // @Restriced(permission = "")
    @Route(url="call/oauth/save")
    public List<HashMap<String,Object>> saveAPIAuth(VMJExchange vmjExchange){
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) {
			return null;
		}
		  = create(vmjExchange);
		Repository.saveObject();
		return getAll(vmjExchange);
	}

    public APIAuth createAPIAuth(VMJExchange vmjExchange){
		APIAuthImpl basicauthimpl = (APIAuthImpl) vmjExchange.getRequestBodyForm("basicauthimpl);
		String clientId = (String) vmjExchange.getRequestBodyForm("clientId);
		String clientSecret = (String) vmjExchange.getRequestBodyForm("clientSecret);
		String credential = (String) vmjExchange.getRequestBodyForm("credential);
		
		  = record.create(vmjExchange);
		 deco = Factory.create("paymentgateway.oauth.core.oAuthImpl", basicauthimpl, clientId, clientSecret, credential);
			return deco;
	}

    // @Restriced(permission = "")
    @Route(url="call/oauth/update")
    public HashMap<String, Object> updateAPIAuth(VMJExchange vmjExchange){
		// to do implement the method
	}

	// @Restriced(permission = "")
    @Route(url="call/oauth/detail")
    public HashMap<String, Object> getAPIAuth(VMJExchange vmjExchange){
		return record.getAPIAuth(vmjExchange);
	}

	// @Restriced(permission = "")
    @Route(url="call/oauth/list")
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
    @Route(url="call/oauth/delete")
    public List<HashMap<String,Object>> deleteAPIAuth(VMJExchange vmjExchange){
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) {
			return null;
		}
		
		String idStr = (String) vmjExchange.getRequestBodyForm("clientId");
		int id = Integer.parseInt(idStr);
		Repository.deleteObject(id);
		return getAll(vmjExchange);
	}

	public void generateCredential() {
		// TODO: implement this method
	}
}
