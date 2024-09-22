package paymentgateway.disbursement.internationaldisbursement;

import java.util.*;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementResource;
import paymentgateway.disbursement.core.DisbursementResourceDecorator;
import paymentgateway.disbursement.core.DisbursementResourceComponent;
import paymentgateway.disbursement.DisbursementResourceFactory;

public class DisbursementServiceImpl extends DisbursementServiceDecorator {
	private static DisbursementResource RESOURCE;
	private static final String[] DELTA_MODULES = {
		"paymentgateway.disbursement.international.DisbursementResourceImpl",
		"paymentgateway.disbursement.internationaldisbursementvalidator.DisbursementResourceImpl"
	};

    public DisbursementServiceImpl(DisbursementServiceComponent record) {
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

    public Disbursement createDisbursement(Map<String, Object> requestBody) {
		return RESOURCE.createDisbursement(requestBody);
	}
}