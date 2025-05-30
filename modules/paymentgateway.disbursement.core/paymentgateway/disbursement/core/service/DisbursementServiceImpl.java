package paymentgateway.disbursement.core;

import com.google.gson.Gson;
import java.util.*;
import java.util.logging.Logger;
import java.io.File;
import java.io.IOException;
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
import org.hibernate.hql.internal.ast.QuerySyntaxException;

public class DisbursementServiceImpl extends DisbursementServiceComponent {
	private static final Logger LOGGER = Logger.getLogger(DisbursementServiceImpl.class.getName());
    
	public int callback(Map<String, Object> requestBody) {
		String workingDir = System.getProperty("user.dir");
		List<File> propertyFiles = new ArrayList<>();
		List<String> vendors = new ArrayList<>();
		String[] targetFiles = {"oy.properties", "flip.properties", "midtrans.properties", "xendit.properties"};

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
	            Map<String, Object> requestMap = config.getCallbackDisbursementRequestBody(requestBody);

	            String idStr = (String) requestMap.get("id");
				String vendorGeneratedIdStr = (String) requestMap.get("vendor_generated_id");
	            String status = (String) requestMap.get("status");

	            LOGGER.info("Processing Vendor: " + vendor);
	            LOGGER.info("ID: " + idStr);
				LOGGER.info("Vendor Generated ID: " + vendorGeneratedIdStr);
	            LOGGER.info("Status: " + status);

				Disbursement disbursement = null;
				if (idStr != null) {
					disbursement = this.getObject(idStr);
				} else {
					List<Disbursement> disbursements = Repository.getAllObject("disbursement_impl");
					for (Disbursement d : disbursements) {
						if (d.getVendorGeneratedId().equals(vendorGeneratedIdStr)) {
							disbursement = d;
						}
					}
				}

				if (disbursement == null) {
					throw new BadRequestException("Disbursement record not found");
				}

				disbursement.setStatus(status.toUpperCase());
		
				this.updateObject(disbursement);
	        } catch (Exception e) {
	            System.err.println("Failed to process vendor: " + vendor);
	            e.printStackTrace();
	        }
	    }

	    return 200;
	}

    public Disbursement createDisbursement(Map<String, Object> requestBody) {
        Map<String, Object> response = sendTransaction(requestBody);
        return createDisbursement(requestBody, response);
    }
	
	public Disbursement createDisbursement(Map<String, Object> requestBody, Map<String, Object> response){
		String vendorName = this.validateVendorName((String) requestBody.get("vendor_name"));
		String bank_code = ((String) requestBody.get("bank_code"));
		String account_number = (String) requestBody.get("account_number");
		double amount = this.validateAmount(requestBody.get("amount"));
		String id = (String) response.get("id");
		int userId = (int) response.get("user_id");
		String status = (String) response.get("status");
		String vendorGeneratedId = (String) response.get("vendor_generated_id");
		
		Disbursement disbursement = DisbursementFactory.createDisbursement(
			"paymentgateway.disbursement.core.DisbursementImpl",
			UUID.fromString(id),
			userId,
			account_number,
			amount,
			bank_code,
			status.toUpperCase(),
			vendorName,
			vendorGeneratedId
		);

		Repository.saveObject(disbursement);
		
		return disbursement;
	}
	
	public HashMap<String, Object> updateDisbursement(Map<String, Object> requestBody) {
		String id = this.validateId(requestBody.get("id"));
		Disbursement disbursement = this.getObject(id);

		if (disbursement == null) {
			throw new BadRequestException(String.format("Disbursement with ID %s does not exist", id));
		}

		double amount = this.validateAmount(requestBody.get("amount"));
		String accountNumber = this.validateRequiredStringField(requestBody, "account_number");
		String bankCode = this.validateRequiredStringField(requestBody, "bank_code");
		disbursement.setAmount(amount);
		disbursement.setAccountNumber(accountNumber);
		disbursement.setBankCode(bankCode);

		this.updateObject(disbursement);
		
		return disbursement.toHashMap();

    }
	
	public List<HashMap<String, Object>> deleteDisbursement(Map<String, Object> requestBody){
		String id = this.validateId(requestBody.get("id"));
		Disbursement disbursement = this.getObject(id);
		
		if (disbursement == null) {
			throw new BadRequestException("Disbursment dengan ID " + id + " tidak ditemukan");
		}

		final String[] disbursementHolder = {null};
		Repository.executeQuery(session -> {
			String sql = String.format("SELECT modulesequence FROM disbursement_comp WHERE id ='%s'", id );
			try {
				String result = (String) session.createNativeQuery(sql).getSingleResult();
				String[] modules = result.split(",");
				disbursementHolder[0] = modules[modules.length - 1].trim();
			} catch (Exception e) {
				disbursementHolder[0] = "";
			}
		});

		if (disbursementHolder[0].equals("disbursement_impl")) {
			this.deleteObject(id);
			return getAllDisbursement(disbursementHolder[0]);
		}

		final String[] targetId = {""};
		Repository.executeQuery(session -> {
			String sql = String.format("SELECT cast(id as varchar) FROM %s WHERE base_component_id = '%s'", disbursementHolder[0], id);
			targetId[0] = (String) session.createNativeQuery(sql).getSingleResult();
		});

		
		this.deleteObject(targetId[0]);

		return getAllDisbursement(disbursementHolder[0]);
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
		String id = UUID.randomUUID().toString();
        String vendorName = this.validateVendorName((String) requestBody.get("vendor_name"));
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
			responseMap = config.getDisbursementResponse(rawResponse, id);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		return responseMap;
	}
	
	public HashMap<String, Object> getDisbursementById(String id){
		List<HashMap<String, Object>> disbursementList = getAllDisbursement("disbursement_impl");
		for (HashMap<String, Object> disbursement : disbursementList){
			String record_id = (String) disbursement.get("record_id");
			if (record_id == id){
				return disbursement;
			}
		}

		return null;
	}

	public HashMap<String, Object> findById(List<HashMap<String, Object>> disbursements, String id) {
		for (HashMap<String, Object> disbursement : disbursements){
			String disbursementId = (String) disbursement.get("id");
			if (disbursementId.equals(id)){
				return disbursement;
			}
		}

		throw new BadRequestException("Disbursement dengan ID " + id + " tidak ditemukan");
	}

	public List<HashMap<String, Object>> getAllDisbursement(){
		return this.getAllDisbursement("disbursement_impl");
	}
	
	public HashMap<String, Object> getDisbursement(String id) {
		Disbursement disbursementImpl = this.getObject(id);

	    if (disbursementImpl == null) {
	    	throw new BadRequestException("Disbursement dengan ID " + id + " tidak ditemukan");
	    }
	    
		return disbursementImpl.toHashMap();
	}
	
	public List<HashMap<String, Object>> getAllDisbursement(String tableName){
		
		try {
			List<Disbursement> list = Repository.getAllObject(tableName);
		    return transformListToHashMap(list);
		} catch (Exception e) {
			throw new BadRequestException("Table name " + tableName + " bukan entity yang valid");
		}
	}

	public String getParamsUrlEncoded(Map<String, Object> requestBody){
		ArrayList<String> paramList = new ArrayList<>();
		for (Map.Entry<String, Object> entry : requestBody.entrySet()) {
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
	
	
	
	public Disbursement getObject(String id) {
        return Repository.getObject(UUID.fromString(id));
    }

    public void deleteObject(String id) {
        Repository.deleteObject(UUID.fromString(id));
    }

    public void updateObject(Disbursement disbursement) {
        Repository.updateObject(disbursement);
    }

    public List<Disbursement> getAllObject(String tableName) {
        return Repository.getAllObject(tableName);
    }

	private Map<String, Object> validateRequestBody(Map<String, Object> requestBody) {
		String vendorName = validateVendorName((String) requestBody.get("vendor_name"));
		Config config = ConfigFactory.createConfig(vendorName,
				ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
        return config.getDisbursementRequestBody(requestBody);
	}

	public String validateVendorName(String vendorName) {
		if (vendorName == null) {
			throw new BadRequestException("vendor_name tidak ditemukan pada payload");
		}
		try {
			// Provide vendor name that supports dibursement only
			System.out.println("Vendor Name:" + vendorName);
			Set<String> vendorNames = new HashSet<>();
			vendorNames.add("Flip");
			vendorNames.add("Xendit");
			
			if (!vendorNames.contains(vendorName)) {
				throw new BadRequestException("vendor_name tidak valid.");
			}
			return vendorName;
		} catch (Exception e) {
			throw new BadRequestException("vendor_name tidak valid");
		}

	}

	public double validateAmount(Object amountObject) {
		Double amount;
		if (amountObject == null) {
			throw new BadRequestException("amount tidak ditemukan pada payload.");
		}
		try {
			amount = ((Double) amountObject);
		} catch (Exception e) {
			try {
				String amountString = (String) amountObject;
				amount = Double.valueOf(amountString);
			} catch (Exception ex) {
				throw new BadRequestException("amount tidak valid.");
			}
		}

		if (amount < 0) {
			throw new BadRequestException("amount tidak boleh negatif.");
		}

		return amount.doubleValue();
	}
	
	public String validateId(Object idObject) {
		String id;
		if (idObject == null) {
			throw new BadRequestException("id tidak ditemukan pada payload.");
		}
		try {
			id = ((String) idObject);
			UUID.fromString(id);
			return id;
		} catch (Exception e) {
			throw new BadRequestException("id tidak valid.");
		}
	}
	
	public String validateRequiredStringField(Map<String, Object> requestBody, String key) {
		Object field = requestBody.get(key);
		if (field == null) {
			throw new BadRequestException(String.format("%s tidak ditemukan pada payload.", key));
		}

		
		String stringField = (String) field;
		if (stringField.length() == 0) {
			throw new BadRequestException(String.format("%s tidak boleh berupa string kosong.", key));
		} 

		return stringField;
	}

}