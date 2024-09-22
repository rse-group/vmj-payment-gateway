package paymentgateway.disbursement.agentdisbursement;

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
    private static DisbursementResource RESOURCE;
	private static final String[] DELTA_MODULES = {
		"paymentgateway.disbursement.agent.DisbursementResourceImpl",
		"paymentgateway.disbursement.domesticdisbursementvalidator.DisbursementResourceImpl"
	};

    public DisbursementResourceImpl(DisbursementResourceComponent record) {
		super(record);

		RESOURCE = DisbursementResourceFactory.createDisbursementResource(
			"paymentgateway.disbursement.core.DisbursementResourceImpl");

		for (int i = 0; i < DELTA_MODULES.length; i++) {
			RESOURCE = DisbursementResourceFactory.createDisbursementResource(
				DELTA_MODULES[i],
				RESOURCE
			);
		}
	}

	public Disbursement createDisbursement(VMJExchange vmjExchange) {
		return RESOURCE.createDisbursement(vmjExchange);
	}

	@Route(url = "call/disbursement/agent")
	public HashMap<String, Object> disbursement(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")) {
			Disbursement result = this.createDisbursement(vmjExchange);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
	}
}
