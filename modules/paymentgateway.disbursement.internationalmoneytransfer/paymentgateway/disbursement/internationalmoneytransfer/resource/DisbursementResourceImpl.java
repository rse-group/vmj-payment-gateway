package paymentgateway.disbursement.internationalmoneytransfer;

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
	private final DisbursementResourceService disbursementResourceService;

	public DisbursementResourceImpl(DisbursementResourceComponent record) {
		super(record);
		this.disbursementResourceService = new DisbursementResourceService(record);
	}

	@Route(url = "call/disbursement/international-money-transfer")
	public HashMap<String, Object> moneyTransfer(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")) {
			Disbursement result = this.createDisbursement(vmjExchange);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
	}
}
