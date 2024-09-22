package paymentgateway.disbursement.domesticdisbursementvalidator;

import vmj.routing.route.VMJExchange;

import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementResourceDecorator;
import paymentgateway.disbursement.core.DisbursementResourceComponent;

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

public class DisbursementResourceImpl extends DisbursementResourceDecorator {
    public DisbursementResourceImpl(DisbursementResourceComponent record) {
		super(record);
	}

    public Disbursement createDisbursement(VMJExchange vmjExchange) {
		String vendorName = (String) vmjExchange.getRequestBodyForm("vendor_name");
		Config config = ConfigFactory.createConfig(vendorName,
				ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
        config.getDomesticDisbursementRequestBody(vmjExchange);
		return record.createDisbursement(vmjExchange);
	}
}
