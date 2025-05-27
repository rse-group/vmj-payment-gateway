package paymentgateway.disbursement.core;

import java.util.*;
import java.util.logging.Logger;

import vmj.routing.route.RequestMethod;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import paymentgateway.disbursement.core.DisbursementResourceComponent;

public class DisbursementResourceImpl extends DisbursementResourceComponent{
	private static final Logger LOGGER = Logger.getLogger(DisbursementResourceImpl.class.getName());
	
	private DisbursementServiceImpl disbursementServiceImpl = new DisbursementServiceImpl();
	
	@Route(url = "call/disbursement/callback", method = RequestMethod.POST)
	public int callback(VMJExchange vmjExchange) {
		Map<String, Object> requestBody = vmjExchange.getPayload();
		// handles callback token from Xendit
		String callbackToken = vmjExchange.getHttpExchange().getRequestHeaders().getFirst("X-CALLBACK-TOKEN");
		if (callbackToken != null) {
			requestBody.put("X-CALLBACK-TOKEN", callbackToken);
		}
		return disbursementServiceImpl.callback(requestBody);
	}

	@Route(url="call/disbursement/detail", method = RequestMethod.GET)
	public HashMap<String, Object> getDisbursement(VMJExchange vmjExchange){
		Map<String, String> queryParamsMap = vmjExchange.queryToMap();
		String id = queryParamsMap.get("id");
		return disbursementServiceImpl.getDisbursement(id);
	}

	@Route(url="call/disbursement/list", method = RequestMethod.GET)
	public List<HashMap<String, Object>> getAllDisbursement(VMJExchange vmjExchange){
		Map<String, String> queryParams = vmjExchange.queryToMap(); 
		return disbursementServiceImpl.getAllDisbursement(queryParams);
	}

	@Route(url="call/disbursement/delete", method = RequestMethod.DELETE)
	public List<HashMap<String, Object>> deleteDisbursement(VMJExchange vmjExchange){
		Map<String, Object> requestBody = vmjExchange.getPayload(); 
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) {
			return null;
		}

		return disbursementServiceImpl.deleteDisbursement(requestBody);
	}

	@Route(url="call/disbursement/update", method = RequestMethod.PUT)
	public HashMap<String, Object> updateDisbursement(VMJExchange vmjExchange){
		Map<String, Object> requestBody = vmjExchange.getPayload(); 
		if (vmjExchange.getHttpMethod().equals("OPTIONS")){
			return null;
		}

		return disbursementServiceImpl.updateDisbursement(requestBody);
	}

	@Route(url = "call/disbursement", method = RequestMethod.POST)
	public HashMap<String, Object> disbursement(VMJExchange vmjExchange) {
		Map<String, Object> requestBody = vmjExchange.getPayload(); 
		Disbursement result = disbursementServiceImpl.createDisbursement(requestBody);
		return result.toHashMap();
	}
}
