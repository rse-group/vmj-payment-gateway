package paymentgateway.payment.core;

import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import paymentgateway.payment.PaymentFactory;
import vmj.auth.annotations.Restricted;
import vmj.routing.route.exceptions.*;

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;
import javax.persistence.PersistenceException;

public class PaymentServiceImpl extends PaymentServiceComponent {
	protected PaymentServiceComponent record;

	public Payment createPayment(Map<String, Object> requestBody, String id, String status) {
		String vendorName = (String) requestBody.get("vendor_name");
		double amount = ((Double) requestBody.get("amount")).doubleValue();
		Payment transaction = PaymentFactory.createPayment("paymentgateway.payment.core.PaymentImpl",
				UUID.fromString(id),
				vendorName,
				amount,
				status);
		sendTransaction(requestBody);
		PaymentRepository.saveObject(transaction);
		return transaction;
	}
	
	public Payment createPayment(CreatePaymentRequestBody requestBody) {
		String vendorName = requestBody.vendorName;
		double amount = requestBody.amount.doubleValue();

		UUID id = UUID.randomUUID();
		String status = "MANUALLY_ADDED";

		Payment transaction = PaymentFactory.createPayment("paymentgateway.payment.core.PaymentImpl",
				id,		
				vendorName,
				amount,
				status);
		sendTransaction(requestBody.toMap());
		PaymentRepository.saveObject(transaction);
		return transaction;
	}

	public Map<String, Object> sendTransaction(Map<String, Object> requestBody) {
		// to do implement this in deltas
		return null;
	}
	
	public Map<String, Object> checkPaymentStatus(CheckPaymentStatusRequestBody requestBody) {
		String vendorName = requestBody.vendorName;
		String Id = String.valueOf(requestBody.id);

		Config config = ConfigFactory.createConfig(vendorName, ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
		HttpClient client = HttpClient.newHttpClient();
		final String[] paymentMethodHolder = {""};
		
		Map<String, Object> responseMap = new HashMap<>();
		
		PaymentRepository.executeQuery(session -> {
			String sql = String.format("SELECT modulesequence FROM payment_comp WHERE idtransaction ='%s'", Id );
			try {
                String result = (String) session.createNativeQuery(sql).getSingleResult();
                String[] modules = result.split(",");
                paymentMethodHolder[0] = modules[modules.length - 1].trim();
            } catch (Exception  e) {
                paymentMethodHolder[0] = "";
            }
		});
		
		System.out.println("paymentMethodHolder" + paymentMethodHolder);
	    if (paymentMethodHolder[0].isEmpty()) {
	    	responseMap.put("status", "Payment does not exist");
	    	responseMap.put("id", Id);
	        return responseMap; 
	        
	    }
		
		String configUrl;
		if (paymentMethodHolder[0].equals("paymentlink_impl") && vendorName.toLowerCase().equals("midtrans")){
			configUrl = config.getProductEnv("PaymentStatus");
		} else {
			configUrl = config.getProductEnv("PaymentDetail");
		}
		
		System.out.println(configUrl + paymentMethodHolder[0]);
        configUrl = config.getPaymentDetailEndpoint(configUrl, Id);
        HttpRequest request = (config.getBuilder(HttpRequest.newBuilder(),config.getHeaderParams()))
				.uri(URI.create(configUrl))
				.GET()
				.build();
		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
            responseMap = config.getPaymentStatusResponse(rawResponse, Id);
            System.out.println("responseMap" + responseMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return responseMap;
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

	public List<HashMap<String,Object>> getAll(String name) {
		List<Payment> paymentVariation = PaymentRepository.getAllObject(name);
		return transformListToHashMap(paymentVariation);
	}

	public List<HashMap<String,Object>> transformListToHashMap(List<Payment> List){
		List<HashMap<String,Object>> resultList = new ArrayList<HashMap<String,Object>>();
		for(int i = 0; i < List.size(); i++) {
			resultList.add(List.get(i).toHashMap());
		}
		return resultList;
	}
	
	public HashMap<String, Object> getPayment(String id){
		Payment paymentImpl = this.getObject(id);
		
		HashMap<String, Object> paymentDataMap = new HashMap<>();
		
		if (paymentImpl == null) {
			paymentDataMap.put("status", "Payment detail does not exist");
			paymentDataMap.put("id", id);
	        return paymentDataMap;
	    }
		
		return paymentImpl.toHashMap();
	}
	
	public List<HashMap<String, Object>> getAllPayment() {
		List<Payment> List = PaymentRepository.getAllObject("payment_impl");
		return transformListToHashMap(List);
	}
	
	public HashMap<String, Object> getPaymentById(String id){
		List<HashMap<String, Object>> paymentList = getAllPayment();
		for (HashMap<String, Object> payment : paymentList){
			String record_id = (String) payment.get("record_id");
			if (record_id == id){
				return payment;
			}
		}

		return null;
	}


	public HashMap<String, Object> updatePayment(UpdatePaymentRequestBody requestBody) {
		String id = requestBody.id;
		Payment payment = this.getObject(id);

		if (payment == null) {
			HashMap<String, Object> notFoundMap = new HashMap<>();
			notFoundMap.put("message", "Payment with ID " + id + " does not exist");
			return notFoundMap;
		}

		try {
			payment.setAmount(requestBody.amount.doubleValue());
		} catch (Exception e){
			e.printStackTrace();
		}

		this.updateObject(payment);
		
		return payment.toHashMap();

    }
	
	public List<HashMap<String, Object>> deletePayment(DeletePaymentRequestBody requestBody){
		String id = requestBody.id;
		Payment payment = this.getObject(id);
		
		if (payment == null) {
			HashMap<String, Object> notFoundMap = new HashMap<>();
			notFoundMap.put("message", "Payment with ID " + id + " does not exist");
			return Collections.singletonList(notFoundMap);
		}
		
		this.deleteObject(id);

		return getAllPayment();
	}
	
	public Payment getObject(String id) {
        return PaymentRepository.getObject(UUID.fromString(id));
    }

    public void deleteObject(String id) {
        PaymentRepository.deleteObject(UUID.fromString(id));
    }

    public void updateObject(Payment payment) {
        PaymentRepository.updateObject(payment);
    }

    public List<Payment> getAllObject(String tableName) {
        return PaymentRepository.getAllObject(tableName);
    }

	public void callback(VMJExchange vmjExchange) {
		String workingDir = System.getProperty("user.dir");
		List<File> propertyFiles = new ArrayList<>();

		List<String> vendors = new ArrayList<>();

		String[] targetFiles = { "oy.properties", "flip.properties", "midtrans.properties", "xendit.properties" };

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
				Config config = ConfigFactory.createConfig(vendor,
						ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
				Map<String, Object> requestMap = config.getCallbackPaymentRequestBody(vmjExchange);

				String idStr = (String) requestMap.get("id");
				String status = (String) requestMap.get("status");

				Payment payment = this.getObject(idStr);

				try {
					payment.setStatus(status);
				} catch (Exception e){
					e.printStackTrace();
				}
		
				this.updateObject(payment);
			} catch (Exception e) {
				System.err.println("Failed to process vendor: " + vendor);
				e.printStackTrace();
			}
		}
	}
}
