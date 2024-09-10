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
import paymentgateway.disbursement.DisbursementResourceFactory;
import paymentgateway.disbursement.core.DisbursementResourceComponent;
import paymentgateway.disbursement.core.DisbursementResourceDecorator;
import paymentgateway.disbursement.core.DisbursementResource;

public class DisbursementResourceService extends DisbursementResourceDecorator {
	private static final Logger LOGGER = Logger.getLogger(DisbursementResourceService.class.getName());
	private static DisbursementResource RESOURCE;

	public DisbursementResourceService(DisbursementResourceComponent record) {
		super(record);
		RESOURCE = DisbursementResourceFactory
					.createDisbursementResource(
						"paymentgateway.disbursement.domesticmoneytransfervalidator.DisbursementResourceImpl",
							DisbursementResourceFactory.createDisbursementResource(
								"paymentgateway.disbursement.agent.DisbursementResourceService",
									DisbursementResourceFactory.createDisbursementResource(
										"paymentgateway.disbursement.core.DisbursementResourceService")));
	}

	public Disbursement createDisbursement(VMJExchange vmjExchange) {
		return RESOURCE.createDisbursement(vmjExchange);
	}
}