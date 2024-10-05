package paymentgateway.disbursement.specifiedrecipient;

import vmj.routing.route.VMJExchange;
import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;
import java.util.Map;
import java.util.logging.Logger;

import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementService;
import paymentgateway.disbursement.core.DisbursementServiceDecorator;
import paymentgateway.disbursement.core.DisbursementServiceComponent;
import paymentgateway.disbursement.DisbursementFactory;
import paymentgateway.disbursement.DisbursementServiceFactory;

public class DisbursementServiceImpl extends DisbursementServiceDecorator {
    private static final Logger LOGGER = Logger.getLogger(DisbursementServiceImpl.class.getName());
    private static DisbursementService RESOURCE;

    public DisbursementServiceImpl(DisbursementServiceComponent record) {
        super(record);
        RESOURCE = DisbursementServiceFactory.createDisbursementService(
            "paymentgateway.disbursement.core.DisbursementServiceImpl"
        );
    }

    @Override
    public Disbursement createDisbursement(Map<String, Object> requestBody) {
        Map<String, Object> validatedRequestBody = validateRequestBody(requestBody);
        LOGGER.info("Request Body: " + validatedRequestBody);
        
        Map<String, Object> response = sendTransaction(validatedRequestBody);
        LOGGER.info("Transaction Response: " + response);
        
        Disbursement coreDisbursement = RESOURCE.createDisbursement(requestBody);
        LOGGER.info("Core Disbursement - Account Number: " + coreDisbursement.getAccountNumber());
        LOGGER.info("Core Disbursement - Bank Code: " + coreDisbursement.getBankCode());

        return createDisbursement(requestBody, response, coreDisbursement);
    }

    private Disbursement createDisbursement(Map<String, Object> requestBody, Map<String, Object> response, Disbursement coreDisbursement) {
        String currency = (String) response.getOrDefault("currency", requestBody.get("currency"));
        String accountHolderName = (String) response.getOrDefault("account_holder_name", requestBody.get("account_holder_name"));
        LOGGER.info("Creating SpecifiedRecipientImpl with currency: " + currency + ", accountHolderName: " + accountHolderName);

        Disbursement specifiedRecipientTransaction = DisbursementFactory.createDisbursement(
            "paymentgateway.disbursement.specifiedrecipient.SpecifiedRecipientImpl",
            coreDisbursement,
            currency,
            accountHolderName
        );
        LOGGER.info("SpecifiedRecipientImpl created. Account Number: " + specifiedRecipientTransaction.getAccountNumber() + ", Bank Code: " + specifiedRecipientTransaction.getBankCode());

        Repository.saveObject(specifiedRecipientTransaction);

        return specifiedRecipientTransaction;
    }

    private Map<String, Object> validateRequestBody(Map<String, Object> requestBody) {
        String vendorName = (String) requestBody.get("vendor_name");
        Config config = ConfigFactory.createConfig(vendorName,
            ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
        Map<String, Object> validatedBody = config.getDisbursementRequestBody(requestBody);

        if (validatedBody.get("account_number") == null || validatedBody.get("bank_code") == null) {
            LOGGER.warning("account_number or bank_code is missing in validated request body");
        }

        return validatedBody;
    }

    @Override
    public Map<String, Object> sendTransaction(Map<String, Object> validatedRequestBody) {
        return RESOURCE.sendTransaction(validatedRequestBody);
    }
}