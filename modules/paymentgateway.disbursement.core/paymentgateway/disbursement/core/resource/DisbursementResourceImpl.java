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

import paymentgateway.disbursement.core.DisbursementResourceComponent;
import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

public class DisbursementResourceImpl {
	private static final Logger LOGGER = Logger.getLogger(DisbursementResourceController.class.getName());
	
	private final DisbursementResourceService disbursementResourceService = new DisbursementResourceService();
	
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

				String hostAddress = disbursementResourceService.getEnvVariableHostAddress("AMANAH_HOST_BE");
        		int portNum = disbursementResourceService.getEnvVariablePortNumber("AMANAH_PORT_BE");
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

	@Route(url="call/disbursement/detail")
	public HashMap<String, Object> getDisbursement(VMJExchange vmjExchange){
		return disbursementResourceService.getDisbursement(vmjExchange);
	}

	@Route(url="call/disbursement/list")
	public List<HashMap<String, Object>> getAllDisbursement(VMJExchange vmjExchange){
		return disbursementResourceService.getAllDisbursement(vmjExchange);
	}

	@Route(url="call/disbursement/delete")
	public List<HashMap<String, Object>> deleteDisbursement(VMJExchange vmjExchange){
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) {
			return null;
		}

		return disbursementResourceService.deleteDisbursement(vmjExchange);
	}

	@Route(url="call/disbursement/update")
	public HashMap<String, Object> updateDisbursement(VMJExchange vmjExchange){
		if (vmjExchange.getHttpMethod().equals("OPTIONS")){
			return null;
		}

		return disbursementResourceService.updateDisbursement(vmjExchange);
	}

	@Route(url = "call/disbursement")
	public HashMap<String, Object> moneyTransfer(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")) {
			Disbursement result = disbursementResourceService.createDisbursement(vmjExchange);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
	}
}
