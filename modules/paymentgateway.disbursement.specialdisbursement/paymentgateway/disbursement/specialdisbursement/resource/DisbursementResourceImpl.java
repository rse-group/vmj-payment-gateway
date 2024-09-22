package paymentgateway.disbursement.specialdisbursement;

import java.util.*;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementResource;
import paymentgateway.disbursement.core.DisbursementResourceDecorator;
import paymentgateway.disbursement.core.DisbursementResourceComponent;
import paymentgateway.disbursement.DisbursementResourceFactory;

public class DisbursementResourceImpl extends DisbursementResourceDecorator {
	private final DisbursementServiceImpl disbursementServiceImpl;

	public DisbursementResourceImpl(DisbursementResourceComponent recordController, DisbursementServiceComponent recordService) {
		super(recordController);
		this.disbursementServiceImpl = new DisbursementServiceImpl(recordService);
	}

	@Route(url = "call/disbursement/special")
	public HashMap<String, Object> moneyTransfer(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")) {
            Map<String, Object> requestBody = vmjExchange.getPayload(); 
			Disbursement result = disbursementServiceImpl.createDisbursement(requestBody);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
	}
}
