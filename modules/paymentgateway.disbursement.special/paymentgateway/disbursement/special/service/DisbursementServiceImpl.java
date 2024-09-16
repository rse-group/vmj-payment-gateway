package paymentgateway.disbursement.special;

import vmj.routing.route.VMJExchange;

import paymentgateway.disbursement.DisbursementFactory;
import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementServiceDecorator;
import paymentgateway.disbursement.core.DisbursementImpl;
import paymentgateway.disbursement.core.DisbursementServiceComponent;


import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

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
		int sender_country = (int) response.get("country");
		String sender_name = (String) response.get("name");
		String sender_address = (String) response.get("address");
		String sender_job = (String) response.get("job");
		String direction = (String) response.get("direction");

		Disbursement approvalTransaction = DisbursementFactory.createDisbursement(
			"paymentgateway.disbursement.special.SpecialImpl",
			record.createDisbursement(requestBody, response),
			sender_country,
			sender_name,
			sender_address,
			sender_job,
			direction
		);

		Repository.saveObject(approvalTransaction);
		
		return approvalTransaction;
	}

	public Map<String, Object> sendTransaction(Map<String, Object> requestBody) {
		String vendorName = (String) requestBody.get("vendor_name");
		Config config = ConfigFactory.createConfig(vendorName,
				ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));

		String configUrl = config.getProductEnv("SpecialMoneyTransfer");
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
			responseMap = config.getSpecialMoneyTransferResponse(rawResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseMap;
	}
}