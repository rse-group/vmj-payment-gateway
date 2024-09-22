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

public class DisbursementResourceImpl extends DisbursementResourceComponent {
	private static final Logger LOGGER = Logger.getLogger(DisbursementResourceImpl.class.getName());

	public Disbursement createDisbursement(VMJExchange vmjExchange){
		Map<String, Object> response = sendTransaction(vmjExchange);
		return createDisbursement(vmjExchange, response);
	}
	
	public Disbursement createDisbursement(VMJExchange vmjExchange, Map<String, Object> response){
		String vendorName = (String) vmjExchange.getRequestBodyForm("vendor_name");
		Config config = ConfigFactory.createConfig(vendorName,
				ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
		Map<String, Object> requestBody = config.getDisbursementRequestBody(vmjExchange);

		String bank_code = (String) requestBody.get("bank_code");
		String account_number = (String) requestBody.get("account_number");
		double amount = Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount"));
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
	
	public String getEnvVariableHostAddress(String varname_host){
        String hostAddress = System.getenv(varname_host)  != null ? System.getenv(varname_host) : "localhost"; // Host
        return hostAddress;
	}

	public int getEnvVariablePortNumber(String varname_port){
        String portNum = System.getenv(varname_port)  != null? System.getenv(varname_port)  : "7776"; //PORT
        int portNumInt = Integer.parseInt(portNum);
        return portNumInt;
	}
	
	@Route(url = "call/disbursement/callback")
	public int callback(VMJExchange vmjExchange) {
		String workingDir = System.getProperty("user.dir");
		List<File> propertyFiles = new ArrayList<>();
		List<String> vendors = new ArrayList<>();
		String[] targetFiles = {"oy.properties", "flip.properties", "midtrans.properties"};

		// Iterate through target files
		for (String targetFile : targetFiles) {
			File file = new File(workingDir, targetFile);
			if (file.exists()) {
				String fileName = file.getName();
				String nameBeforeDot = fileName.substring(0, fileName.indexOf('.'));
				String capitalized = nameBeforeDot.substring(0, 1).toUpperCase() + nameBeforeDot.substring(1);
            	vendors.add(capitalized);
			}
		}
		
		for (String vendor : vendors) {
	        try {
	            Config config = ConfigFactory.createConfig(vendor, ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
	            Map<String, Object> requestMap = config.getCallbackDisbursementRequestBody(vmjExchange);

	            String idStr = (String) requestMap.get("id");
	            String status = (String) requestMap.get("status");

	            LOGGER.info("Processing Vendor: " + vendor);
	            LOGGER.info("ID: " + idStr);
	            LOGGER.info("Status: " + status);

				String hostAddress = getEnvVariableHostAddress("AMANAH_HOST_BE");
        		int portNum = getEnvVariablePortNumber("AMANAH_PORT_BE");
	            HttpClient client = HttpClient.newHttpClient();
				String configUrl = String.format("http://%s:%d/call/receivedisbursementcallback", hostAddress, portNum);
	            String requestString = config.getRequestString(requestMap);
	            HttpRequest request = config.getBuilder(HttpRequest.newBuilder(), config.getHeaderParams())
	                                       .uri(URI.create(configUrl))
	                                       .POST(HttpRequest.BodyPublishers.ofString(requestString))
	                                       .build();
				
	            try {
	                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
	                String rawResponse = response.body();
	                LOGGER.info("Raw Response: " + rawResponse);
	            } catch (Exception e) {
	                System.err.println("Failed to send request for vendor: " + vendor);
	                e.printStackTrace();
	            }
	        } catch (Exception e) {
	            System.err.println("Failed to process vendor: " + vendor);
	            e.printStackTrace();
	        }
	    }

	    return 200;
	}

	public Map<String, Object> sendTransaction(VMJExchange vmjExchange) {
		String vendorName = (String) vmjExchange.getRequestBodyForm("vendor_name");
		Config config = ConfigFactory.createConfig(vendorName,
				ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
		Map<String, Object> requestMap = vmjExchange.getPayload();
		String configUrl = config.getProductEnv("Disbursement");
		HashMap<String, String> headerParams = config.getHeaderParams();

		LOGGER.info("Header: " + headerParams);
		LOGGER.info("Config URL: " + configUrl);

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
			LOGGER.info("Raw Response: " + rawResponse);
			responseMap = config.getDisbursementResponse(rawResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseMap;
	}
	
	@Route(url="call/disbursement/detail")
	public HashMap<String, Object> getDisbursement(VMJExchange vmjExchange){
		int id = ((Double) vmjExchange.getRequestBodyForm("id")).intValue();
		Disbursement disbursementImpl = Repository.getObject(id);
		HashMap<String, Object> disbursementDataMap = disbursementImpl.toHashMap();
		return disbursementDataMap;
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

	@Route(url="call/disbursement/list")
	public List<HashMap<String, Object>> getAllDisbursement(VMJExchange vmjExchange){
		String table = (String) vmjExchange.getRequestBodyForm("table_name");
		List<Disbursement> List = Repository.getAllObject(table);
		return transformListToHashMap(List);
	}

	public List<HashMap<String, Object>> getAllDisbursement(String tableName){
		List<Disbursement> List = Repository.getAllObject(tableName);
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

	@Route(url="call/disbursement/delete")
	public List<HashMap<String, Object>> deleteDisbursement(VMJExchange vmjExchange){
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
		if (vmjExchange.getHttpMethod().equals("OPTIONS")){
			return null;
		}

		int id = ((Double) vmjExchange.getRequestBodyForm("id")).intValue();
		Disbursement disbursement = Repository.getObject(id);

		try {
			disbursement.setAmount((Double) vmjExchange.getRequestBodyForm("amount"));
			disbursement.setAccountNumber((String) vmjExchange.getRequestBodyForm("account_number"));
			disbursement.setBankCode((String) vmjExchange.getRequestBodyForm("bank_code"));
		} catch (Exception e){
			e.printStackTrace();
		}

		Repository.updateObject(disbursement);
		
		return disbursement.toHashMap();
	}

	@Route(url = "call/disbursement")
	public HashMap<String, Object> disbursement(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")) {
			Disbursement result = this.createDisbursement(vmjExchange);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
	}
}
