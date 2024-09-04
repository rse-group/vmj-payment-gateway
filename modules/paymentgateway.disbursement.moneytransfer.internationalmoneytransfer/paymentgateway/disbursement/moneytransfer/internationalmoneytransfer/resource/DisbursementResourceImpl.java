package paymentgateway.disbursement.moneytransfer.internationalmoneytransfer;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.DisbursementResourceFactory;
import paymentgateway.disbursement.core.DisbursementResourceComponent;
import paymentgateway.disbursement.core.DisbursementResourceDecorator;
import paymentgateway.disbursement.core.DisbursementResource;

public class DisbursementResourceImpl extends DisbursementResourceDecorator {
	private static DisbursementResource RESOURCE;

	public DisbursementResourceImpl(DisbursementResourceComponent record) {
		super(record);
		RESOURCE = DisbursementResourceFactory
					.createDisbursementResource(
						"paymentgateway.disbursement.moneytransfer.international.DisbursementResourceImpl",
							DisbursementResourceFactory.createDisbursementResource(
								"paymentgateway.disbursement.moneytransfer.DisbursementResourceImpl",
									DisbursementResourceFactory.createDisbursementResource(
										"paymentgateway.disbursement.core.DisbursementResourceImpl")));
	}

	public Disbursement createDisbursement(VMJExchange vmjExchange) {
		return RESOURCE.createDisbursement(vmjExchange);
	}

	@Route(url = "call/international-money-transfer")
	public HashMap<String, Object> moneyTransfer(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")) {
			Disbursement result = this.createDisbursement(vmjExchange);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
	}
}
