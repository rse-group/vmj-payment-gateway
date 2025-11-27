package paymentgateway.disbursement.international;

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

public class DisbursementServiceImpl extends DisbursementServiceDecorator {
	private static final Logger LOGGER = Logger.getLogger(DisbursementServiceImpl.class.getName());

    public DisbursementServiceImpl(DisbursementServiceComponent record) {
    	super(record);
    }

    public Disbursement createDisbursement(Map<String, Object> requestBody) {
    	Map<String, Object> response = sendTransaction(requestBody);
        return createDisbursement(requestBody, response);
	}

	public Disbursement createDisbursement(Map<String, Object> requestBody, Map<String, Object> response) {
		double exachange_rate = (double) response.get("exchange_rate");
		double fee = (double) response.get("fee");
		String source_country = (String) response.get("source_country");
		String destination_country = (String) response.get("destination_country");
		double amount_in_sender_currency = (double) response.get("amount");
		String beneficiary_currency_code = (String) response.get("beneficiary_currency_code");

		Disbursement internationalTransaction = DisbursementFactory.createDisbursement(
			"paymentgateway.disbursement.international.InternationalImpl",
			record.createDisbursement(requestBody, response),
			exachange_rate,
			fee,
			source_country,
			destination_country,
			amount_in_sender_currency,
			beneficiary_currency_code
		);

		Repository.saveObject(internationalTransaction);

		return internationalTransaction;
	}

	public Map<String, Object> sendTransaction(Map<String, Object> requestBody) {
        String vendorName = (String) requestBody.get("vendor_name");
		Config config = ConfigFactory.createConfig(vendorName,
				ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
		
		String configUrl = config.getProductEnv("InternationalDisbursement");
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
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			LOGGER.info("Raw Response: " + rawResponse);
			responseMap = config.getInternationalDisbursementResponse(rawResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseMap;
	}

}