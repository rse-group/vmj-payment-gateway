package paymentgateway.disbursement.core;

import com.google.gson.Gson;
import java.util.*;
import java.util.logging.Logger;
import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import paymentgateway.disbursement.DisbursementFactory;
import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

public class DisbursementServiceImpl extends DisbursementServiceComponent {
	private static final Logger LOGGER = Logger.getLogger(DisbursementServiceImpl.class.getName());
    
    public Disbursement createDisbursement(Map<String, Object> requestBody) {
        Map<String, Object> response = sendTransaction(validateRequestBody(requestBody));
        return createDisbursement(requestBody, response);
    }
	
	public Disbursement createDisbursement(Map<String, Object> requestBody, Map<String, Object> response){
		Map<String, Object> validatedRequestBody = validateRequestBody(requestBody);
		String bank_code = (String) validatedRequestBody.get("bank_code");
		String account_number = (String) validatedRequestBody.get("account_number");
		double amount = (Double) validatedRequestBody.get("amount");
		int id = (int) response.get("id");
		int userId = (int) response.get("user_id");
		String status = (String) response.get("status");
		
		Disbursement disbursement = DisbursementFactory.createDisbursement(
			"paymentgateway.disbursement.core.DisbursementImpl",
			id,
			userId,
			account_number,
			amount,
			bank_code,
			status
		);

		Repository.saveObject(disbursement);
		
		return disbursement;
	}
	
	public HashMap<String, Object> updateDisbursement(VMJExchange vmjExchange) {

		int id = ((Double) vmjExchange.getRequestBodyForm("id")).intValue();
		Disbursement disbursement = this.getObject(id);

		try {
			disbursement.setAmount((Double) vmjExchange.getRequestBodyForm("amount"));
			disbursement.setAccountNumber((String) vmjExchange.getRequestBodyForm("account_number"));
			disbursement.setBankCode((String) vmjExchange.getRequestBodyForm("bank_code"));
		} catch (Exception e){
			e.printStackTrace();
		}

		this.updateObject(disbursement);
		
		return disbursement.toHashMap();

    }
	
	public List<HashMap<String, Object>> deleteDisbursement(VMJExchange vmjExchange){
		int id = ((Double) vmjExchange.getRequestBodyForm("id")).intValue();
		Disbursement disbursement = this.getObject(id);
		this.deleteObject(id);

		return getAllDisbursement(vmjExchange);
	}
	
	public String getEnvVariableHostAddress(String varname_host){
        String hostAddress = System.getenv(varname_host)  != null ? System.getenv(varname_host) : "localhost"; // Host
        return hostAddress;
	}

	public int getEnvVariablePortNumber(String varname_port){
        String portNum = System.getenv(varname_port)  != null? System.getenv(varname_port)  : "7776"; //PORT
        int portNumInt = Integer.parseInt(portNum);
        return portNumInt;
	}

	public Map<String, Object> sendTransaction(Map<String, Object> requestBody) {
        String vendorName = (String) requestBody.get("vendor_name");
		Config config = ConfigFactory.createConfig(vendorName,
				ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));

		String configUrl = config.getProductEnv("Disbursement");
		HashMap<String, String> headerParams = config.getHeaderParams();

		LOGGER.info("Header: " + headerParams);
		LOGGER.info("Config URL: " + configUrl);

		String requestString = config.getRequestString(requestBody);
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
			responseMap = config.getDisbursementResponse(rawResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseMap;
	}
	
	public HashMap<String, Object> getDisbursementById(int id){
		List<HashMap<String, Object>> disbursementList = getAllDisbursement("disbursement_impl");
		for (HashMap<String, Object> disbursement : disbursementList){
			int record_id = ((Double) disbursement.get("record_id")).intValue();
			if (record_id == id){
				return disbursement;
			}
		}

		return null;
	}

	public List<HashMap<String, Object>> getAllDisbursement(String tableName){
		List<Disbursement> List = Repository.getAllObject(tableName);
		return transformListToHashMap(List);
	}
	
	public HashMap<String, Object> getDisbursement(VMJExchange vmjExchange){
		int id = ((Double) vmjExchange.getRequestBodyForm("id")).intValue();
		Disbursement disbursementImpl = this.getObject(id);
		HashMap<String, Object> disbursementDataMap = disbursementImpl.toHashMap();
		return disbursementDataMap;
	}
	
	public List<HashMap<String, Object>> getAllDisbursement(VMJExchange vmjExchange){
		String table = (String) vmjExchange.getRequestBodyForm("table_name");
		List<Disbursement> List = Repository.getAllObject(table);
		return transformListToHashMap(List);
	}

	public String getParamsUrlEncoded(VMJExchange vmjExchange){
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

	public List<HashMap<String, Object>> transformListToHashMap(List<Disbursement> List){
		List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();

		for (int i = 0; i < List.size(); i++){
			resultList.add(List.get(i).toHashMap());
		}
		
		return resultList;
	}
	
	
	
	public Disbursement getObject(int id) {
        return Repository.getObject(id);
    }

    public void deleteObject(int id) {
        Repository.deleteObject(id);
    }

    public void updateObject(Disbursement disbursement) {
        Repository.updateObject(disbursement);
    }

    public List<Disbursement> getAllObject(String tableName) {
        return Repository.getAllObject(tableName);
    }

	private Map<String, Object> validateRequestBody(Map<String, Object> requestBody) {
		String vendorName = (String) requestBody.get("vendor_name");
		Config config = ConfigFactory.createConfig(vendorName,
				ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
        return config.getDisbursementRequestBody(requestBody);
	}
}