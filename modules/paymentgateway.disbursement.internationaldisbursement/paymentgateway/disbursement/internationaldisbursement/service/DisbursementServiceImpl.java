package paymentgateway.disbursement.internationaldisbursement;

import java.util.*;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementService;
import paymentgateway.disbursement.core.DisbursementServiceDecorator;
import paymentgateway.disbursement.core.DisbursementServiceComponent;
import paymentgateway.disbursement.DisbursementServiceFactory;

public class DisbursementServiceImpl extends DisbursementServiceDecorator {
	private static DisbursementService RESOURCE;
	private static final String[] DELTA_MODULES = {
		"paymentgateway.disbursement.international.DisbursementServiceImpl",
		"paymentgateway.disbursement.internationaldisbursementvalidator.DisbursementServiceImpl"
	};

    public DisbursementServiceImpl(DisbursementServiceComponent record) {
   		super(record);

		RESOURCE = DisbursementServiceFactory.createDisbursementService(
			"paymentgateway.disbursement.core.DisbursementServiceImpl");

		for (int i = 0; i < DELTA_MODULES.length; i++) {
			RESOURCE = DisbursementServiceFactory.createDisbursementService(
				DELTA_MODULES[i],
				RESOURCE
			);
		}
	}

    public Disbursement createDisbursement(Map<String, Object> requestBody) {
		return RESOURCE.createDisbursement(requestBody);
	}
}