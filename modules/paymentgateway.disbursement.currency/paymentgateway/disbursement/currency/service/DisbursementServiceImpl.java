package paymentgateway.disbursement.agent;

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
import paymentgateway.disbursement.currency.CurrencyImpl;

public class DisbursementServiceImpl extends DisbursementServiceDecorator {
    private static final Logger LOGGER = Logger.getLogger(DisbursementServiceImpl.class.getName());

    public DisbursementServiceImpl(DisbursementServiceComponent record) {
        super(record);
    }
    
    public Disbursement createDisbursement(Map<String, Object> requestBody) throws BadRequestException {
        Map<String, Object> enhancedRequestBody = addCurrencyInfo(requestBody);
        Map<String, Object> response = sendTransaction(enhancedRequestBody);
        return createDisbursement(enhancedRequestBody, response);
    }

    private Map<String, Object> addCurrencyInfo(Map<String, Object> requestBody) throws BadRequestException {
        Map<String, Object> enhancedRequestBody = new HashMap<>(requestBody);
        
        if (requestBody.containsKey("currency")) {
            String currency = (String) requestBody.get("currency");
            enhancedRequestBody.put("sender_currency_code", currency);
            enhancedRequestBody.put("destination_currency_code", currency);
        } else if (requestBody.containsKey("sender_currency") && requestBody.containsKey("destination_currency")) {
            enhancedRequestBody.put("sender_currency_code", requestBody.get("sender_currency"));
            enhancedRequestBody.put("destination_currency_code", requestBody.get("destination_currency"));
        } else {
            throw new BadRequestException("Missing currency information in the request body");
        }
        
        return enhancedRequestBody;
    }

    public Disbursement createDisbursement(Map<String, Object> requestBody, Map<String, Object> response) {
        int agent_id = (int) response.get("agent_id");
        String direction = (String) response.get("direction");
        Disbursement agentTransaction = DisbursementFactory.createDisbursement(
            "paymentgateway.disbursement.agent.AgentImpl",
            record.createDisbursement(requestBody, response),
            agent_id,
            direction
        );
        
        String senderCurrencyCode = (String) requestBody.get("sender_currency_code");
        String destinationCurrencyCode = (String) requestBody.get("destination_currency_code");
        Disbursement currencyTransaction = new CurrencyImpl(agentTransaction, senderCurrencyCode, destinationCurrencyCode);
        
        Repository.saveObject(currencyTransaction);
        return currencyTransaction;
    }

    public Map<String, Object> sendTransaction(Map<String, Object> requestBody) {
        String vendorName = (String) requestBody.get("vendor_name");
        Config config = ConfigFactory.createConfig(vendorName,
                ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
        String configUrl = config.getProductEnv("AgentDisbursement");
        HashMap<String, String> headerParams = config.getHeaderParams();
        LOGGER.info("Header: " + headerParams);
        LOGGER.info("Config URL: " + configUrl);
        String requestString = config.getRequestString(requestBody);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = (config.getBuilder(HttpRequest.newBuilder(), headerParams))
                .uri(URI.create(configUrl))
                .POST(HttpRequest.BodyPublishers.ofString(requestString))
                .build();
        Map<String, Object> responseMap = new HashMap<>();
        
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String rawResponse = response.body();
            LOGGER.info("Raw Response: " + rawResponse);
            responseMap = config.getAgentDisbursementResponse(rawResponse);
        } catch (Exception e) {
            LOGGER.severe("Error in sendTransaction: " + e.getMessage());
            e.printStackTrace();
        }
        return responseMap;
    }
}

class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }
}