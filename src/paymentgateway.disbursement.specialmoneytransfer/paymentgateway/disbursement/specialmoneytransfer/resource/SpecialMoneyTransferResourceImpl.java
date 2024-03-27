package paymentgateway.disbursement.specialmoneytransfer;

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


import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import paymentgateway.disbursement.DisbursementFactory;
import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementResourceDecorator;
import paymentgateway.disbursement.core.DisbursementImpl;
import paymentgateway.disbursement.core.DisbursementResourceComponent;
import paymentgateway.disbursement.DisbursementFactory;
import paymentgateway.disbursement.core.MoneyTransferResponse;
import paymentgateway.disbursement.special.SpecialResourceImpl;

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

public class SpecialMoneyTransferResourceImpl extends SpecialResourceImpl {
	private static final Logger LOGGER = Logger.getLogger(SpecialMoneyTransferResourceImpl.class.getName());

	public SpecialMoneyTransferResourceImpl(DisbursementResourceComponent record) {
		super(record);
	}

<<<<<<< HEAD
	public Disbursement createDisbursement(VMJExchange vmjExchange, String productName, String serviceName) {
		MoneyTransferResponse response = super.sendTransaction(vmjExchange,productName, serviceName);
		String status = response.getStatus();
		
		Gson gson = new Gson();
		String jsonResponse = gson.toJson(response);
		
		System.out.println("----- Response ----- Special -----");
		System.out.println(jsonResponse);
		
		int id = response.getId();
		int userId = response.getUser_id();
		int sender_country = response.getSender().getSender_country();
		String sender_name = response.getSender().getSender_name();
		String sender_address = response.getSender().getSender_address();
		String sender_job = response.getSender().getSender_job();
		String direction = response.getDirection();
=======
	public Disbursement createDisbursement(VMJExchange vmjExchange) {
		Map<String, Object> response = sendTransaction(vmjExchange);

		int id = (int) response.get("id");
		int userId = (int) response.get("user_id");
		String status = (String) response.get("status");

		int sender_country = (int) response.get("country");
		String sender_name = (String) response.get("name");
		String sender_address = (String) response.get("address");
		String sender_job = (String) response.get("job");
		String direction = (String) response.get("direction");
>>>>>>> serima/dev

		Disbursement transaction = record.createDisbursement(vmjExchange,id,userId);
		Disbursement moneyTransferTransaction = DisbursementFactory.createDisbursement(
				"paymentgateway.disbursement.moneytransfer.MoneyTransferImpl",
				transaction, status);
		Repository.saveObject(moneyTransferTransaction);
		Disbursement approvalTransaction = DisbursementFactory.createDisbursement(
				"paymentgateway.disbursement.special.SpecialImpl",
				moneyTransferTransaction,
				sender_country,
				sender_name,
				sender_address,
				sender_job,
				direction);
		Repository.saveObject(approvalTransaction);
		return approvalTransaction;
	}

	public Map<String, Object> sendTransaction(VMJExchange vmjExchange) {

		Config config = ConfigFactory.createConfig(ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
		Map<String, Object> body = vmjExchange.getPayload();
		String configUrl = config.getProductEnv("SpecialMoneyTransfer");
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
			requestMap = config.getSpecialMoneyTransferResponse(rawResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return requestMap;
	}

	@Route(url = "call/special-money-transfer")
	public HashMap<String, Object> specialMoneyTransfer(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")){
			Disbursement result = this.createDisbursement(vmjExchange);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
		
	}
}


