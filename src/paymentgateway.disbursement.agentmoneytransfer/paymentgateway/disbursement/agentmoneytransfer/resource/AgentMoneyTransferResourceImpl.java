package paymentgateway.disbursement.agentmoneytransfer;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.logging.Logger;

import java.nio.charset.StandardCharsets;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import paymentgateway.disbursement.DisbursementFactory;
import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementResourceDecorator;
import paymentgateway.disbursement.core.DisbursementImpl;
import paymentgateway.disbursement.core.DisbursementResourceComponent;
import paymentgateway.disbursement.agent.*;
import paymentgateway.disbursement.core.GetAllDisbursementResponse;
import paymentgateway.disbursement.core.MoneyTransferResponse;

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

public class AgentMoneyTransferResourceImpl extends AgentResourceImpl {
	private static final Logger LOGGER = Logger.getLogger(AgentMoneyTransferResourceImpl.class.getName());

	public AgentMoneyTransferResourceImpl(DisbursementResourceComponent record) {
		super(record);
	}
	public Disbursement createDisbursement(VMJExchange vmjExchange) {
		Map<String, Object> response = sendTransaction(vmjExchange);

		int id = (int) response.get("id");
		int user_id = (int) response.get("user_id");
		String status = (String) response.get("status");
		int agent_id = (int) response.get("agent_id");
		String direction = (String) response.get("direction");

		Disbursement transaction = record.createDisbursement(vmjExchange,id,user_id);

		Disbursement moneyTransferTransaction = DisbursementFactory.createDisbursement(
				"paymentgateway.disbursement.moneytransfer.MoneyTransferImpl",
				transaction, status);
		Repository.saveObject(moneyTransferTransaction);
		System.out.println("11b");
		Disbursement agentTransaction = DisbursementFactory.createDisbursement(
				"paymentgateway.disbursement.agent.AgentImpl",
				moneyTransferTransaction,
				agent_id,
				direction);
		Repository.saveObject(agentTransaction);
		return agentTransaction;
	}

	public Map<String, Object> sendTransaction(VMJExchange vmjExchange) {

		Config config = ConfigFactory.createConfig(ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
		Map<String, Object> body = vmjExchange.getPayload();
		String configUrl = config.getProductEnv("AgentMoneyTransfer");
		HashMap<String, String> headerParams = config.getHeaderParams();
		LOGGER.info("header: " + headerParams);
		LOGGER.info("configUrl: " + configUrl);
		Gson gson = new Gson();
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = (config.getBuilder(HttpRequest.newBuilder(), headerParams))
				.uri(URI.create(configUrl))
				.POST(HttpRequest.BodyPublishers.ofString(record.getParamsUrlEncoded(body)))
				.build();
				
		Map<String, Object> requestMap = new HashMap<>();

		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			LOGGER.info("rawResponse:" + rawResponse);
			requestMap = config.getAgentMoneyTransferResponse(rawResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return requestMap;
	}

	@Route(url = "call/agent-money-transfer")
	public HashMap<String, Object> moneyTransfer(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")){
			Disbursement result = this.createDisbursement(vmjExchange);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
	}

}


