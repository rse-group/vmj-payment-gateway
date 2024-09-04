package paymentgateway.disbursement.moneytransfer.international;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.util.*;
import java.util.logging.Logger;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

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
		double exachange_rate = (double) response.get("exchange_rate");
		double fee = (double) response.get("fee");
		String source_country = (String) response.get("source_country");
		String destination_country = (String) response.get("destination_country");
		double amount_in_sender_currency = (double) response.get("amount");
		String beneficiary_currency_code = (String) response.get("beneficiary_currency_code");

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

		Disbursement internationalTransaction = DisbursementFactory.createDisbursement(
			"paymentgateway.disbursement.moneytransfer.international.InternationalImpl",
			disbursementBase,
			userId,
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

	public Map<String, Object> sendTransaction(VMJExchange vmjExchange) {
		String vendorName = (String) vmjExchange.getRequestBodyForm("vendor_name");
		Config config = ConfigFactory.createConfig(vendorName,
				ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
		Map<String, Object> requestMap = vmjExchange.getPayload();

		String beneficiaryBankName = "";
		if (requestMap.containsKey("beneficiary_bank_name")){
			beneficiaryBankName = (String) requestMap.get("beneficiary_bank_name");
		} else if (requestMap.containsKey("bank_code")) {
			beneficiaryBankName = (String) requestMap.get("bank_code");
		}
		requestMap.put("beneficiary_bank_name", beneficiaryBankName);

		String beneficiaryAccountNumber = "";
		if (requestMap.containsKey("beneficiary_account_number")){
			beneficiaryAccountNumber = (String) requestMap.get("beneficiary_account_number");
		} else if (requestMap.containsKey("bank_code")) {
			beneficiaryAccountNumber = (String) requestMap.get("account_number");
		}
		requestMap.put("beneficiary_account_number", beneficiaryAccountNumber);

		String configUrl = config.getProductEnv("InternationalMoneyTransfer");
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
			responseMap = config.getInternationalMoneyTransferResponse(rawResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseMap;
	}

	@Route(url = "call/international")
	public HashMap<String, Object> moneyTransfer(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")) {
			Disbursement result = this.createDisbursement(vmjExchange);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
	}
}
