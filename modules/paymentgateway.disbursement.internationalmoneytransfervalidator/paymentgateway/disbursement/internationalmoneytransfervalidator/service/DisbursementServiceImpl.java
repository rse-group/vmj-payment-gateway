package paymentgateway.disbursement.internationalmoneytransfervalidator;

import vmj.routing.route.VMJExchange;

import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementServiceDecorator;
import paymentgateway.disbursement.core.DisbursementServiceComponent;

import paymentgateway.config.core.Config;

import java.util.Map;

import paymentgateway.config.ConfigFactory;

public class DisbursementServiceImpl extends DisbursementServiceDecorator {
    public DisbursementServiceImpl(DisbursementServiceComponent record) {
		super(record);
	}

    public Disbursement createDisbursement(Map<String, Object> requestBody) {
		String vendorName = (String) requestBody.get("vendor_name");
		Config config = ConfigFactory.createConfig(vendorName,
				ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
        config.getInternationalMoneyTransferRequestBody(requestBody);
		return record.createDisbursement(requestBody);
	}
}
