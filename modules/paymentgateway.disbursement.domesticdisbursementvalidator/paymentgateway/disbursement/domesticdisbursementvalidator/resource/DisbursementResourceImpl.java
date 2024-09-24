package paymentgateway.disbursement.domesticdisbursementvalidator;

import com.google.gson.Gson;

import java.net.URI;
import java.net.URLEncoder;
import java.util.*;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

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
}
