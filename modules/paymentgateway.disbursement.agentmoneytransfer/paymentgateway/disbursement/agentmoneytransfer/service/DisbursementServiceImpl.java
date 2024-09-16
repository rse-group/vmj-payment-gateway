package paymentgateway.disbursement.agentmoneytransfer;

import vmj.routing.route.VMJExchange;

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.DisbursementServiceFactory;
import paymentgateway.disbursement.core.DisbursementServiceComponent;
import paymentgateway.disbursement.core.DisbursementServiceDecorator;
import paymentgateway.disbursement.core.DisbursementService;

public class DisbursementServiceImpl extends DisbursementServiceDecorator {
	private static final Logger LOGGER = Logger.getLogger(DisbursementServiceImpl.class.getName());
	private static DisbursementService RESOURCE;

    public DisbursementServiceImpl(DisbursementServiceComponent record) {
   		super(record);
		RESOURCE = DisbursementServiceFactory
					.createDisbursementService(
						"paymentgateway.disbursement.domesticmoneytransfervalidator.DisbursementServiceImpl",
							DisbursementServiceFactory.createDisbursementService(
								"paymentgateway.disbursement.agent.DisbursementServiceImpl",
									DisbursementServiceFactory.createDisbursementService(
										"paymentgateway.disbursement.core.DisbursementServiceImpl")));
	}

	public Disbursement createDisbursement(VMJExchange vmjExchange) {
		return RESOURCE.createDisbursement(vmjExchange);
	}
}