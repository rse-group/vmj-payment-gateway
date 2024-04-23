package paymentgateway.disbursement.facilitatormoneytransfer;

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
import paymentgateway.disbursement.special.SpecialResourceImpl;

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

public class FacilitatorMoneyTransferResourceImpl extends SpecialResourceImpl {
	private static final Logger LOGGER = Logger.getLogger(FacilitatorMoneyTransferResourceImpl.class.getName());

	public FacilitatorMoneyTransferResourceImpl(DisbursementResourceComponent record) {
		super(record);
	}

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

	protected Map<String, Object> sendTransaction(VMJExchange vmjExchange) {
		Config config = ConfigFactory.createConfig(ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
		Gson gson = new Gson();
		Map<String, Object> requestMap = vmjExchange.getPayload();
		String configUrl = config.getProductEnv("FacilitatorMoneyTransfer");
		HashMap<String, String> headerParams = config.getHeaderParams();
		System.out.println("configUrl: " + configUrl);
		LOGGER.info("header: " + headerParams);
		LOGGER.info("configUrl: " + configUrl);
		String requestString = config.getRequestString(requestMap);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = (config.getBuilder(HttpRequest.newBuilder(),headerParams))
				.uri(URI.create(configUrl))
				.POST(HttpRequest.BodyPublishers.ofString(requestString))
				.build();
		Map<String, Object> responseMap = new HashMap<>();
		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			System.out.println("rawResponse " + rawResponse);
			responseMap = config.getFacilitatorMoneyTransferResponse(rawResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseMap;
	}

	// public Map<String, Object> sendTransaction(VMJExchange vmjExchange) {

	// 	Config config = ConfigFactory.createConfig(ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
	// 	Map<String, Object> body = vmjExchange.getPayload();
	// 	String configUrl = config.getProductEnv("FacilitatorMoneyTransfer");
	// 	HashMap<String, String> headerParams = config.getHeaderParams();
	// 	LOGGER.info("header: " + headerParams);
	// 	LOGGER.info("configUrl: " + configUrl);
	// 	Gson gson = new Gson();
	// 	HttpClient client = HttpClient.newHttpClient();
	// 	HttpRequest request = (config.getBuilder(HttpRequest.newBuilder(), headerParams))
	// 			.uri(URI.create(configUrl))
	// 			.POST(HttpRequest.BodyPublishers.ofString(record.getParamsUrlEncoded(body)))
	// 			.build();
				
	// 	Map<String, Object> requestMap = new HashMap<>();

	// 	try {
	// 		HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
	// 		String rawResponse = response.body().toString();
	// 		LOGGER.info("rawResponse:" + rawResponse);
	// 		requestMap = config.getFacilitatorMoneyTransferResponse(rawResponse);
	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 	}

	// 	return requestMap;
	// }

	@Route(url = "call/facilitator-money-transfer")
	public HashMap<String, Object> FacilitatorMoneyTransfer(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")){
			Disbursement result = this.createDisbursement(vmjExchange);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
		
	}
}


