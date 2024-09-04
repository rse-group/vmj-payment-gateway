package paymentgateway.disbursement.moneytransfer.agent;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.util.*;
import java.util.logging.Logger;

import java.nio.charset.StandardCharsets;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import java.text.SimpleDateFormat;

import paymentgateway.disbursement.DisbursementFactory;
import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementResourceDecorator;
import paymentgateway.disbursement.core.DisbursementImpl;
import paymentgateway.disbursement.core.DisbursementResourceComponent;

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

public class DisbursementResourceImpl extends DisbursementResourceDecorator {
	private static final Logger LOGGER = Logger.getLogger(DisbursementResourceImpl.class.getName());

	public DisbursementResourceImpl(DisbursementResourceComponent record) {
		super(record);
	}

	public Disbursement createDisbursement(VMJExchange vmjExchange) {
		Map<String, Object> response = sendTransaction(vmjExchange);
		return createDisbursement(vmjExchange, response);
	}

	public Disbursement createDisbursement(VMJExchange vmjExchange, Map<String, Object> response) {
		int userId = (int) response.get("user_id");
		int agent_id = (int) response.get("agent_id");
		String direction = (String) response.get("direction");

		String recordClassName = record.getClass().getName();
		Disbursement disbursementBase;
		if (
			recordClassName.equals(
				"paymentgateway.disbursement.core.DisbursementResourceImpl") ||
			recordClassName.equals(
				"paymentgateway.disbursement.moneytransfer.DisbursementResourceImpl")
		) {
			disbursementBase = record.createDisbursement(vmjExchange, response);
		} else {
			disbursementBase = record.createDisbursement(vmjExchange);
		}

		Disbursement agentTransaction = DisbursementFactory.createDisbursement(
			"paymentgateway.disbursement.moneytransfer.agent.AgentImpl",
			disbursementBase,
			userId,
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

		String bankCode = "";
		if (requestMap.containsKey("bank_code")) {
			bankCode = (String) requestMap.get("bank_code");
		} else if (requestMap.containsKey("beneficiary_bank_name")) {
			bankCode = (String) requestMap.get("beneficiary_bank_name");
		}
		requestMap.put("bank_code", bankCode);

		String accountNumber = "";
		if (requestMap.containsKey("account_number")) {
			accountNumber = (String) requestMap.get("account_number");
		} else if (requestMap.containsKey("beneficiary_account_number")) {
			accountNumber = (String) requestMap.get("beneficiary_account_number");
		}
		requestMap.put("account_number", accountNumber);

		String configUrl = config.getProductEnv("AgentMoneyTransfer");
		HashMap<String, String> headerParams = config.getHeaderParams();

		LOGGER.info("header: " + headerParams);
		LOGGER.info("configUrl: " + configUrl);

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
			LOGGER.info("rawResponse: " + rawResponse);
			responseMap = config.getAgentMoneyTransferResponse(rawResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseMap;
	}

	@Route(url = "call/agent")
	public HashMap<String, Object> moneyTransfer(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")) {
			Disbursement result = this.createDisbursement(vmjExchange);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
	}
}
