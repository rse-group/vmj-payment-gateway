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
import paymentgateway.disbursement.special.SpecialResourceImpl;

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

public class SpecialMoneyTransferResourceImpl extends SpecialResourceImpl {
	private static final Logger LOGGER = Logger.getLogger(SpecialMoneyTransferResourceImpl.class.getName());

	public SpecialMoneyTransferResourceImpl(DisbursementResourceComponent record) {
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

	public Map<String, Object> sendTransaction(VMJExchange vmjExchange) {
		String vendorName = (String) vmjExchange.getRequestBodyForm("vendor_name");

		Config config = ConfigFactory.createConfig(vendorName, ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
		
		Map<String, Object> requestMap = vmjExchange.getPayload();
		String configUrl = config.getProductEnv("SpecialMoneyTransfer");
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
			LOGGER.info("rawResponse:" + rawResponse);
			responseMap = config.getSpecialMoneyTransferResponse(rawResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseMap;
	}

	@Route(url = "call/special-money-transfer")
	public HashMap<String, Object> SpecialMoneyTransfer(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")){
			Disbursement result = this.createDisbursement(vmjExchange);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
		
	}
}


