package paymentgateway.disbursement.internationalmoneytransfer;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.DisbursementFactory;
import paymentgateway.disbursement.core.DisbursementImpl;
import paymentgateway.disbursement.core.DisbursementResourceComponent;
import paymentgateway.disbursement.international.InternationalResourceImpl;

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

public class InternationalMoneyTransferResourceImpl extends InternationalResourceImpl {
	private static final Logger LOGGER = Logger.getLogger(InternationalMoneyTransferResourceImpl.class.getName());

	public InternationalMoneyTransferResourceImpl(DisbursementResourceComponent record) {
		super(record);
	}

	public Disbursement createDisbursement(VMJExchange vmjExchange) {
		Map<String, Object> response = sendTransaction(vmjExchange);
		
		int id = (int) response.get("id");
		int user_id = (int) response.get("user_id");
		String status = (String) response.get("status");

		double exachange_rate = (double) response.get("exchange_rate");
		double fee = (double) response.get("fee");
		String source_country = (String) response.get("source_country");
		String destination_country = (String) response.get("destination_country");
		double amount_in_sender_currency = (double) response.get("amount");
		String beneficiary_currency_code =  (String) response.get("beneficiary_currency_code");;

		Disbursement transaction = record.createDisbursement(vmjExchange,id,user_id);
		Disbursement moneyTransferTransaction = DisbursementFactory.createDisbursement(
				"paymentgateway.disbursement.moneytransfer.MoneyTransferImpl",
				transaction, status);
		Repository.saveObject(moneyTransferTransaction);
		Disbursement internationalMoneyTransferTransaction = DisbursementFactory.createDisbursement(
				"paymentgateway.disbursement.international.InternationalImpl",
				moneyTransferTransaction,
				exachange_rate,
				fee,
				source_country,
				destination_country,
				amount_in_sender_currency,
				beneficiary_currency_code);
		Repository.saveObject(internationalMoneyTransferTransaction);
		return internationalMoneyTransferTransaction;
	}

	public Map<String, Object> sendTransaction(VMJExchange vmjExchange) {

		Config config = ConfigFactory.createConfig(ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
		Map<String, Object> body = vmjExchange.getPayload();
		String configUrl = config.getProductEnv("InternationalMoneyTransfer");
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
			requestMap = config.getInternationalMoneyTransferResponse(rawResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return requestMap;
	}

	@Route(url = "call/international-money-transfer")
	public HashMap<String, Object> moneyTransfer(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")){
			Disbursement result = this.createDisbursement(vmjExchange);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
	}
}

