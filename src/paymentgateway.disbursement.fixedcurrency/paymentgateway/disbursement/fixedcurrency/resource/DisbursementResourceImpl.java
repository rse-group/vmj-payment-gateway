package paymentgateway.disbursement.fixedcurrency;

import java.util.*;
import java.util.logging.Logger;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;
import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementResource;
import paymentgateway.disbursement.core.DisbursementResourceDecorator;
import paymentgateway.disbursement.core.DisbursementResourceComponent;
import paymentgateway.disbursement.core.DisbursementServiceComponent;

public class DisbursementResourceImpl extends DisbursementResourceDecorator{
	private static final Logger LOGGER = Logger.getLogger(DisbursementResourceImpl.class.getName());
	
	private DisbursementServiceImpl disbursementServiceImpl;

    public DisbursementResourceImpl(DisbursementResourceComponent recordController, DisbursementServiceComponent recordService) {
        super(recordController);
        this.disbursementServiceImpl = new DisbursementServiceImpl(recordService);
    }
	
	@Route(url = "call/fixedcurrency/callback")
	public int callback(VMJExchange vmjExchange) {
		Map<String, Object> requestBody = vmjExchange.getPayload(); 
		return disbursementServiceImpl.callback(requestBody);
	}

	@Route(url="call/fixedcurrency/detail")
	public HashMap<String, Object> getDisbursement(VMJExchange vmjExchange){
		Map<String, Object> requestBody = vmjExchange.getPayload(); 
		return disbursementServiceImpl.getDisbursement(requestBody);
	}

	@Route(url="call/fixedcurrency/list")
	public List<HashMap<String, Object>> getAllDisbursement(VMJExchange vmjExchange){
		Map<String, Object> requestBody = vmjExchange.getPayload(); 
		return disbursementServiceImpl.getAllDisbursement(requestBody);
	}

	@Route(url="call/fixedcurrency/delete")
	public List<HashMap<String, Object>> deleteDisbursement(VMJExchange vmjExchange){
		Map<String, Object> requestBody = vmjExchange.getPayload(); 
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) {
			return null;
		}

		return disbursementServiceImpl.deleteDisbursement(requestBody);
	}

	@Route(url="call/fixedcurrency/update")
	public HashMap<String, Object> updateDisbursement(VMJExchange vmjExchange){
		Map<String, Object> requestBody = vmjExchange.getPayload(); 
		if (vmjExchange.getHttpMethod().equals("OPTIONS")){
			return null;
		}

		return disbursementServiceImpl.updateDisbursement(requestBody);
	}

	@Route(url = "call/fixedcurrency")
	public HashMap<String, Object> disbursement(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")) {
			Map<String, Object> requestBody = vmjExchange.getPayload(); 
			Disbursement result = disbursementServiceImpl.createDisbursement(requestBody);
			return result.toHashMap();
            
		}
		throw new NotFoundException("Route tidak ditemukan");
	}
}
