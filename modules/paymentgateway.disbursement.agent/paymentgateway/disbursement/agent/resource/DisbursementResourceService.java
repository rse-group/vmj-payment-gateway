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

import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.DisbursementResourceFactory;
import paymentgateway.disbursement.core.DisbursementResourceComponent;
import paymentgateway.disbursement.core.DisbursementResourceDecorator;
import paymentgateway.disbursement.core.DisbursementResource;

public class DisbursementResourceService extends DisbursementResourceDecorator {
	private static final Logger LOGGER = Logger.getLogger(DisbursementResourceService.class.getName());
	
	public DisbursementResourceService(DisbursementResourceComponent record) {
    	super(record);
    }
	
	public Disbursement createDisbursement(VMJExchange vmjExchange) {
		Map<String, Object> response = sendTransaction(vmjExchange);
		return createDisbursement(vmjExchange, response);
	}

	public Disbursement createDisbursement(VMJExchange vmjExchange, Map<String, Object> response) {
		int agent_id = (int) response.get("agent_id");
		String direction = (String) response.get("direction");

		Disbursement agentTransaction = DisbursementFactory.createDisbursement(
			"paymentgateway.disbursement.agent.AgentImpl",
			record.createDisbursement(vmjExchange, response),
			agent_id,
			direction
		);

		Repository.saveObject(agentTransaction);

		return agentTransaction;
	}

	public Map<String, Object> sendTransaction(VMJExchange vmjExchange) {
		String vendorName = (String) vmjExchange.getRequestBodyForm("vendor_name");
		Config config = ConfigFactory.createConfig(vendorName,
				ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
		Map<String, Object> requestMap = vmjExchange.getPayload();
		String configUrl = config.getProductEnv("AgentMoneyTransfer");
		HashMap<String, String> headerParams = config.getHeaderParams();

		LOGGER.info("Header: " + headerParams);
		LOGGER.info("Config URL: " + configUrl);

		String requestString = config.getRequestString(requestMap);
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
			responseMap = config.getAgentMoneyTransferResponse(rawResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseMap;
	}
}