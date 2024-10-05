package paymentgateway.disbursement.specifiedrecipient;

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
import paymentgateway.disbursement.DisbursementFactory;
import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementServiceDecorator;
import paymentgateway.disbursement.core.DisbursementImpl;
import paymentgateway.disbursement.core.DisbursementServiceComponent;
import paymentgateway.disbursement.specifiedrecipient.SpecifiedRecipientImpl;

public class DisbursementServiceImpl extends DisbursementServiceDecorator {
	private static final Logger LOGGER = Logger.getLogger(DisbursementServiceImpl.class.getName());

    public DisbursementServiceImpl(DisbursementServiceComponent record) {
        super(record);
    }
    
    public Disbursement createDisbursement(Map<String, Object> requestBody) {
        Map<String, Object> validatedRequestBody = validateRequestBody(requestBody);
        Map<String, Object> response = sendTransaction(validatedRequestBody);
        return createDisbursement(requestBody, response);
    }

	public Disbursement createDisbursement(Map<String, Object> requestBody, Map<String, Object> response) 	{
		String currency = (String) response.get("currency");
		String account_holder_name = (String) response.get("account_holder_name");

		Disbursement specifiedRecipientTransaction = DisbursementFactory.createDisbursement(
			"paymentgateway.disbursement.specifiedrecipient.SpecifiedRecipientImpl",
			record.createDisbursement(requestBody, response),
			currency,
			account_holder_name
		);

		Repository.saveObject(specifiedRecipientTransaction);

		return specifiedRecipientTransaction;
	}
	
	private Map<String, Object> validateRequestBody(Map<String, Object> requestBody) {
		String vendorName = (String) requestBody.get("vendor_name");
		Config config = ConfigFactory.createConfig(vendorName,
				ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
        return config.getDisbursementRequestBody(requestBody);
	}
}

class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }
}