package paymentgateway.disbursement.specialmoneytransfer;

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

    public DisbursementResourceImpl(DisbursementResourceComponent record) {
		super(record);
		RESOURCE = DisbursementResourceFactory
					.createDisbursementResource(
                        "paymentgateway.disbursement.internationalmoneytransfervalidator.DisbursementResourceImpl",
                            DisbursementResourceFactory.createDisbursementResource(
                                "paymentgateway.disbursement.domesticmoneytransfervalidator.DisbursementResourceImpl",
                                    DisbursementResourceFactory.createDisbursementResource(
                                        "paymentgateway.disbursement.special.DisbursementResourceImpl",
                                            DisbursementResourceFactory.createDisbursementResource(
                                                "paymentgateway.disbursement.core.DisbursementResourceImpl"))));
	}

	public Disbursement createDisbursement(VMJExchange vmjExchange) {
		return RESOURCE.createDisbursement(vmjExchange);
	}

	@Route(url = "call/disbursement/special-money-transfer")
	public HashMap<String, Object> moneyTransfer(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")) {
			Disbursement result = this.createDisbursement(vmjExchange);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
	}
}
