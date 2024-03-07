package paymentgateway.disbursement.core;

import com.google.gson.Gson;
import java.util.*;
import java.util.logging.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import paymentgateway.disbursement.core.MoneyTransferResponse;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import paymentgateway.disbursement.DisbursementFactory;
import paymentgateway.disbursement.DisbursementConfiguration;
import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

public class DisbursementResourceImpl extends DisbursementResourceComponent {
	protected DisbursementResourceComponent record;
	private static final Logger LOGGER = Logger.getLogger(DisbursementResourceImpl.class.getName());

	public Disbursement createDisbursement(VMJExchange vmjExchange,int id, int userId) {
		String bank_code = "";
		String account_number = "";
		try{
			bank_code = (String) vmjExchange.getRequestBodyForm("bank_code");
			account_number = (String) vmjExchange.getRequestBodyForm("account_number");
		}catch (Exception e){
			bank_code = (String) vmjExchange.getRequestBodyForm("beneficiary_bank_name");
			account_number = (String) vmjExchange.getRequestBodyForm("beneficiary_account_number");
		}
		//for FE
		double amount = Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount"));
		//for BE test
//		double amount = (Double) vmjExchange.getRequestBodyForm("amount");

		Disbursement disbursement = DisbursementFactory.createDisbursement(
				"paymentgateway.disbursement.core.DisbursementImpl",
				id,
				userId,
				account_number,
				amount,
				bank_code);
		Repository.saveObject(disbursement);
		return disbursement;
	}



	public MoneyTransferResponse sendTransaction(VMJExchange vmjExchange, String serviceName) {

		Config config = ConfigFactory
				.createConfig(ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
		Map<String, Object> body = config.processRequestMap(vmjExchange,serviceName);
		String configUrl = config.getProductEnv(serviceName);
		HashMap<String, String> headerParams = config.getHeaderParams();
		LOGGER.info("header: " + headerParams);
		LOGGER.info("configUrl: " + configUrl);
		Gson gson = new Gson();
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = (config.getBuilder(HttpRequest.newBuilder(),headerParams))
				.uri(URI.create(configUrl))
				.POST(HttpRequest.BodyPublishers.ofString(getParamsUrlEncoded(body)))
				.build();
		MoneyTransferResponse responseObj = null;


		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			LOGGER.info("rawResponse:" + rawResponse);
			responseObj = gson.fromJson(rawResponse,
					MoneyTransferResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseObj;
	}

	public String getParamsUrlEncoded(Map<String, Object> vmjExchange) {
		ArrayList<String> paramList = new ArrayList<>();
		for (Map.Entry<String, Object> entry : vmjExchange.entrySet()) {
			String key = entry.getKey();
			Object val = entry.getValue();
			if (val instanceof String) {
				paramList.add(key + "=" + URLEncoder.encode(val.toString(), StandardCharsets.UTF_8));
			} else if (val instanceof Integer) {
				paramList.add(key + "=" + URLEncoder.encode(val.toString(), StandardCharsets.UTF_8));
			} else if (val instanceof Double) {
				int temp = ((Double) val).intValue();
				paramList.add(key + "=" + URLEncoder.encode(Integer.toString(temp), StandardCharsets.UTF_8));
			}

		}
		String encodedURL = String.join("&",paramList);
		return encodedURL;
	}
	@Route(url="call/disbursement/detail")
	public HashMap<String, Object> getDisbursement(VMJExchange vmjExchange){
		int id = ((Double) vmjExchange.getRequestBodyForm("id")).intValue();
		Disbursement disbursementImpl = Repository.getObject(id);
		HashMap<String,Object> disbursementDataMap = disbursementImpl.toHashMap();
		return disbursementDataMap;
	}

	public HashMap<String, Object> getDisbursementById(int id){
		List<HashMap<String,Object>> disbursementList = getAllDisbursement("moneytransfer_impl");
		for (HashMap<String,Object> disbursement : disbursementList){
			int record_id = ((Double) disbursement.get("record_id")).intValue();
			if(record_id == id){
				return disbursement;
			}

		}

		return null;
	}

	@Route(url="call/disbursement/list")
	public List<HashMap<String,Object>> getAllDisbursement(VMJExchange vmjExchange){
		String table = (String) vmjExchange.getRequestBodyForm("table_name");
		List<Disbursement> List = Repository.getAllObject(table);
		return transformListToHashMap(List);
	}

	public List<HashMap<String,Object>> getAllDisbursement(String tableName){
		List<Disbursement> List = Repository.getAllObject(tableName);
		return transformListToHashMap(List);
	}

	public GetAllDisbursementResponse getAllDataFromAPI(String name){
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
					.header("Content-Type", "application/x-www-form-urlencoded")
					.header("Authorization",
							"Basic SkRKNUpERXpKR3A0YlU5WVppNU9kRGRuU0VoU2JYbFBibXhEVVM1VVJGaHRTM0pEZFZwc2NWVTFMemgxUldwSVVqVldielpMYkhOMkwybDE=")
					.header("Cookie", "_csrf=I_hH_U80Wpc07Yx-pV_HBDI4KO64F3ES")
					.uri(URI.create("https://bigflip.id/big_sandbox_api/v2/" + name + "?pagination=1000&page=1"))
					.GET()
					.build();


		Gson gson = new Gson();
		GetAllDisbursementResponse responseObj = null;
		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			responseObj = gson.fromJson(rawResponse,
					GetAllDisbursementResponse.class);
			LOGGER.info("Raw Response: " + responseObj);
		} catch (Exception e) {
			System.out.println(e);
		}
		return responseObj;
	}

	public String getParamsUrlEncoded(VMJExchange vmjExchange) {
		ArrayList<String> paramList = new ArrayList<>();
		for (Map.Entry<String, Object> entry : vmjExchange.getPayload().entrySet()) {
			String key = entry.getKey();
			Object val = entry.getValue();
			if (val instanceof String) {
				paramList.add(key + "=" + URLEncoder.encode(val.toString(), StandardCharsets.UTF_8));
			} else if (val instanceof Integer) {
				paramList.add(key + "=" + URLEncoder.encode(val.toString(), StandardCharsets.UTF_8));
			} else if (val instanceof Double) {
				int temp = ((Double) val).intValue();
				paramList.add(key + "=" + URLEncoder.encode(Integer.toString(temp), StandardCharsets.UTF_8));
			}

		}
		String encodedURL = String.join("&",paramList);
		return encodedURL;
	}

	public List<HashMap<String,Object>> transformListToHashMap(List<Disbursement> List){
		List<HashMap<String,Object>> resultList = new ArrayList<HashMap<String,Object>>();
		for(int i = 0; i < List.size(); i++) {
			resultList.add(List.get(i).toHashMap());
		}
		return resultList;
	}

	@Route(url="call/disbursement/delete")
	public List<HashMap<String,Object>> deleteDisbursement(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) {
			return null;
		}
		int id = ((Double) vmjExchange.getRequestBodyForm("id")).intValue();
		Disbursement disbursement = Repository.getObject(id);
		Repository.deleteObject(id);
		return getAllDisbursement(vmjExchange);
	}

	@Route(url="call/disbursement/update")
	public HashMap<String, Object> updateDisbursement(VMJExchange vmjExchange){
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) {
			return null;
		}
		int id = ((Double) vmjExchange.getRequestBodyForm("id")).intValue();
		Disbursement disbursement = Repository.getObject(id);
		try{
			disbursement.setAmount((Double) vmjExchange.getRequestBodyForm("amount"));
			disbursement.setAccountNumber((String) vmjExchange.getRequestBodyForm("account_number"));
			disbursement.setBankCode((String) vmjExchange.getRequestBodyForm("bank_code"));

		}
		catch (Exception e){
			e.printStackTrace();
		}

		Repository.updateObject(disbursement);
		return disbursement.toHashMap();
	}

}
