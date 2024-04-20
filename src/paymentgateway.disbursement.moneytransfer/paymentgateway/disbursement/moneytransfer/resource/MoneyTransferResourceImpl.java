package paymentgateway.disbursement.moneytransfer;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.util.*;
import java.util.logging.Logger;
import vmj.hibernate.integrator.RepositoryUtil;
import java.nio.charset.StandardCharsets;

import paymentgateway.disbursement.core.*;
import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementImpl;
import paymentgateway.disbursement.core.DisbursementResourceComponent;
import paymentgateway.disbursement.core.DisbursementResourceDecorator;

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;


import paymentgateway.disbursement.DisbursementFactory;

public class MoneyTransferResourceImpl extends DisbursementResourceDecorator {
	RepositoryUtil<MoneyTransferImpl> moneyTransferRepository;
	private static final Logger LOGGER = Logger.getLogger(MoneyTransferResourceImpl.class.getName());

	public MoneyTransferResourceImpl(DisbursementResourceComponent record) {
		super(record);
		this.moneyTransferRepository = new RepositoryUtil<MoneyTransferImpl>(paymentgateway.disbursement.moneytransfer.MoneyTransferImpl.class);
	}

	public Disbursement createDisbursement(VMJExchange vmjExchange) {
		Map<String, Object> response = sendTransaction(vmjExchange);

		int id = (int) response.get("id");
		int userId = (int) response.get("user_id");
		String status = (String) response.get("status");

		LOGGER.info("Response Status: " + status);
		Disbursement transaction = record.createDisbursement(vmjExchange,id,userId);
		Disbursement moneyTransferTransaction = DisbursementFactory.createDisbursement(
				"paymentgateway.disbursement.moneytransfer.MoneyTransferImpl",
				transaction, status);
		Repository.saveObject(moneyTransferTransaction);
		return moneyTransferTransaction;
	}

	public Map<String, Object> sendTransaction(VMJExchange vmjExchange) {

		Config config = ConfigFactory.createConfig(ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
		Map<String, Object> body = vmjExchange.getPayload();
		String configUrl = config.getProductEnv("MoneyTransfer");
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
			requestMap = config.getMoneyTransferResponse(rawResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return requestMap;
	}

	// @Route(url = "call/money-transfer/update-status")
	// public void updateStatus(VMJExchange vmjExchange){
	// 	GetAllDisbursementResponse dosmmesticData = record.getAllDataFromAPI("disbursement");
	// 	GetAllDisbursementResponse internationalData = record.getAllDataFromAPI("international-disbursement");
	// 	List<MoneyTransferResponse> dosmesticTransferData = dosmmesticData.getData();
	// 	List<MoneyTransferResponse> internationalTransferData = internationalData.getData();
	// 	List<MoneyTransferImpl> moneyTransfers = getPendingStatus();

	// 	if(moneyTransfers.size() != 0){
	// 		for(MoneyTransferResponse response :  dosmesticTransferData){
	// 			int id = response.getId();
	// 			try {
	// 				MoneyTransferImpl moneyTransfer = getPendingMoneyTransferById(moneyTransfers,id);
	// 				if(moneyTransfer == null){
	// 					continue;
	// 				}
	// 				moneyTransfer.setStatus(response.getStatus());
	// 				moneyTransferRepository.updateObject(moneyTransfer);
	// 			} catch (Exception e) {
	// 				e.printStackTrace();
	// 			}
	// 		}
	// 		for(MoneyTransferResponse response :  internationalTransferData){
	// 			int id = response.getId();
	// 			try {
	// 				MoneyTransferImpl moneyTransfer = getPendingMoneyTransferById(moneyTransfers,id);
	// 				if(moneyTransfer == null){
	// 					continue;
	// 				}
	// 				moneyTransfer.setStatus(response.getStatus());
	// 				moneyTransferRepository.updateObject(moneyTransfer);
	// 			} catch (Exception e) {
	// 				e.printStackTrace();
	// 			}
	// 		}

	// 	}
	// }

	// public MoneyTransferImpl getPendingMoneyTransferById(List<MoneyTransferImpl> moneyTransfers, int id){
	// 	for(MoneyTransferImpl moneyTransfer : moneyTransfers){
	// 		if(moneyTransfer.getId() == id){
	// 			return moneyTransfer;
	// 		}
	// 	}
	// 	return null;
	// }

	// public List<MoneyTransferImpl> getPendingStatus(){
	// 	List<MoneyTransferImpl> result = new ArrayList<>();
	// 	List<MoneyTransferImpl> moneyTransfers = moneyTransferRepository.getAllObject("moneytransfer_impl");
	// 	for(MoneyTransferImpl moneyTransfer : moneyTransfers){
	// 		if(moneyTransfer.getStatus().equals("PENDING")){
	// 			result.add(moneyTransfer);
	// 		}
	// 	}
	// 	return result;
	// }


	@Route(url = "call/money-transfer")
	public HashMap<String, Object> moneyTransfer(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")){
			Disbursement result = this.createDisbursement(vmjExchange);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
	}


}


