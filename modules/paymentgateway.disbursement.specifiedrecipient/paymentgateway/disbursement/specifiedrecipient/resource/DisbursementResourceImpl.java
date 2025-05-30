package paymentgateway.disbursement.specifiedrecipient;

import java.util.*;

import vmj.routing.route.RequestMethod;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementResourceComponent;
import paymentgateway.disbursement.core.DisbursementServiceComponent;
import paymentgateway.disbursement.core.DisbursementResourceDecorator;
import paymentgateway.disbursement.core.DisbursementResource;

public class DisbursementResourceImpl extends DisbursementResourceDecorator {
	private DisbursementServiceImpl disbursementServiceImpl;

	public DisbursementResourceImpl(DisbursementResourceComponent recordController, DisbursementServiceComponent recordService) {
		super(recordController);
		this.disbursementServiceImpl = new DisbursementServiceImpl(recordService);
	}

	@Route(url = "call/specified-recipient", method = RequestMethod.POST)
	public HashMap<String, Object> disbursement(VMJExchange vmjExchange) {
		Map<String, Object> requestBody = vmjExchange.getPayload(); 
		Disbursement result = disbursementServiceImpl.createDisbursement(requestBody);
		return result.toHashMap();
	}

	@Route(url = "call/specified-recipient/list", method = RequestMethod.GET)
	public List<HashMap<String, Object>> getAllDisbursement(VMJExchange vmjExchange){
		return disbursementServiceImpl.getAllDisbursement();
	}

	@Route(url = "call/specified-recipient/detail", method = RequestMethod.GET)
	public HashMap<String, Object> getDisbursement(VMJExchange vmjExchange){
		Map<String, String> queryParamsMap = vmjExchange.queryToMap();
		String id = queryParamsMap.get("id");
		return disbursementServiceImpl.getDisbursement(id);
	}
}
