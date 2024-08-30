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

public class PaymentResourceImpl extends PaymentResourceComponent {
	protected PaymentResourceComponent record;

	public Payment createPayment(VMJExchange vmjExchange, int id) {
		String vendorName = (String) vmjExchange.getRequestBodyForm("vendor_name");
		double amount = Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount"));
		Payment transaction = PaymentFactory.createPayment("paymentgateway.payment.core.PaymentImpl",
				id,
				vendorName,
				amount);
		sendTransaction();
		PaymentRepository.saveObject(transaction);
		return transaction;
	}

	private void sendTransaction() {
		// to do implement this in deltas
	}
	
	protected Map<String, Object> checkPaymentStatus(VMJExchange vmjExchange) {
		String vendorName = (String) vmjExchange.getRequestBodyForm("vendor_name");
		String Id = (String) vmjExchange.getRequestBodyForm("id");

		Config config = ConfigFactory.createConfig(vendorName, ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
		HttpClient client = HttpClient.newHttpClient();
        String configUrl = config.getProductEnv("PaymentDetail");
        configUrl = config.getPaymentDetailEndpoint(configUrl, Id);
        HttpRequest request = (config.getBuilder(HttpRequest.newBuilder(),config.getHeaderParams()))
				.uri(URI.create(configUrl))
				.GET()
				.build();
        Map<String, Object> responseMap = new HashMap<>();
		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			System.out.println("rawResponse " + rawResponse);
            responseMap = config.getPaymentStatusResponse(rawResponse, Id);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return responseMap;
	}

	@Route(url = "call/paymentstatus")
	public Map<String, Object> paymentStatus(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")){
			return this.checkPaymentStatus(vmjExchange);
		}
		throw new NotFoundException("Route tidak ditemukan");
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

		
	@Route(url = "call/payment/callback")
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
	    	System.out.println(vendor);
	        try {
	            Config config = ConfigFactory.createConfig(vendor, ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
	            Map<String, Object> requestMap = config.getCallbackPaymentRequestBody(vmjExchange);

	            String idStr = (String) requestMap.get("id");
	            String status = (String) requestMap.get("status");

	            System.out.println("Processing vendor: " + vendor);
	            System.out.println("ID: " + idStr);
	            System.out.println("Status: " + status);

				String hostAddress = getEnvVariableHostAddress("AMANAH_HOST_BE");
        		int portNum = getEnvVariablePortNumber("AMANAH_PORT_BE");

	            HttpClient client = HttpClient.newHttpClient();
				String configUrl = String.format("http://%s:%d/call/receivecallback", hostAddress, portNum);
	            // String configUrl = "http://localhost:443/call/receivecallback";
	            String requestString = config.getRequestString(requestMap);
	            HttpRequest request = config.getBuilder(HttpRequest.newBuilder(), config.getHeaderParams())
	                                       .uri(URI.create(configUrl))
	                                       .POST(HttpRequest.BodyPublishers.ofString(requestString))
	                                       .build();

	            try {
	                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
	                String rawResponse = response.body();
	                System.out.println("rawResponse: " + rawResponse);
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
	




	@Route(url = "call/payment/list")
	public List<HashMap<String,Object>> getAll(VMJExchange vmjExchange) {
		String name = (String) vmjExchange.getRequestBodyForm("table_name");
		List<Payment> paymentVariation = PaymentRepository.getAllObject(name);
		return transformListToHashMap(paymentVariation);
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

	public void deletePayment(VMJExchange vmjExchange){
		System.out.println("hello");
	}
}
