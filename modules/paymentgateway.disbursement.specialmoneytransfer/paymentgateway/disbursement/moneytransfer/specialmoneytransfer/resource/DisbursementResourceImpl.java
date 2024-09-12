package paymentgateway.disbursement.specialmoneytransfer;

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
import paymentgateway.disbursement.core.DisbursementResourceComponent;
import paymentgateway.disbursement.core.DisbursementServiceComponent;
import paymentgateway.disbursement.core.DisbursementResourceDecorator;
import paymentgateway.disbursement.core.DisbursementResource;

public class DisbursementResourceImpl extends DisbursementResourceDecorator {
	private final DisbursementServiceImpl disbursementServiceImpl;

	public DisbursementResourceImpl(DisbursementResourceComponent recordController, DisbursementServiceComponent recordService) {
		super(recordController);
		this.disbursementServiceImpl = new DisbursementServiceImpl(recordService);
	}
	

	@Route(url = "call/special-money-transfer")
	public HashMap<String, Object> moneyTransfer(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")) {
			System.out.println("Here");
			Disbursement result = disbursementServiceImpl.createDisbursement(vmjExchange);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
	}
}
