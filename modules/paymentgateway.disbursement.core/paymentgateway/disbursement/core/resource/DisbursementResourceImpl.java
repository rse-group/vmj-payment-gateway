package paymentgateway.disbursement.core;

import java.util.*;
import java.util.logging.Logger;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import paymentgateway.disbursement.core.DisbursementResourceComponent;

public class DisbursementResourceImpl extends DisbursementResourceComponent{
	private static final Logger LOGGER = Logger.getLogger(DisbursementResourceImpl.class.getName());
	
	private DisbursementServiceImpl disbursementServiceImpl = new DisbursementServiceImpl();
	
	@Route(url = "call/disbursement/callback")
	public int callback(VMJExchange vmjExchange) {
		Map<String, Object> requestBody = vmjExchange.getPayload(); 
		return disbursementServiceImpl.callback(requestBody);
	}

	@Route(url="call/disbursement/detail")
	public HashMap<String, Object> getDisbursement(VMJExchange vmjExchange){
		Map<String, Object> requestBody = vmjExchange.getPayload(); 
		return disbursementServiceImpl.getDisbursement(requestBody);
	}

	@Route(url="call/disbursement/list")
	public List<HashMap<String, Object>> getAllDisbursement(VMJExchange vmjExchange){
		Map<String, Object> requestBody = vmjExchange.getPayload(); 
		return disbursementServiceImpl.getAllDisbursement(requestBody);
	}

	@Route(url="call/disbursement/delete")
	public List<HashMap<String, Object>> deleteDisbursement(VMJExchange vmjExchange){
		Map<String, Object> requestBody = vmjExchange.getPayload(); 
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) {
			return null;
		}

		return disbursementServiceImpl.deleteDisbursement(requestBody);
	}

	@Route(url="call/disbursement/update")
	public HashMap<String, Object> updateDisbursement(VMJExchange vmjExchange){
		Map<String, Object> requestBody = vmjExchange.getPayload(); 
		if (vmjExchange.getHttpMethod().equals("OPTIONS")){
			return null;
		}

		return disbursementServiceImpl.updateDisbursement(requestBody);
	}

	@Route(url = "call/disbursement")
	public HashMap<String, Object> disbursement(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")) {
            Map<String, Object> requestBody = vmjExchange.getPayload(); 
			Disbursement result = disbursementServiceImpl.createDisbursement(requestBody);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
	}
}
