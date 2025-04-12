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

import paymentgateway.config.core.CreatePaymentRequestBody;
import paymentgateway.config.core.CheckPaymentStatusRequestBody;
import paymentgateway.config.core.GetPaymentRequestBody;
import paymentgateway.config.core.GetAllPaymentRequestBody;
import paymentgateway.config.core.UpdatePaymentRequestBody;
import paymentgateway.config.core.DeletePaymentRequestBody;

public class PaymentServiceImpl extends PaymentServiceComponent {
	protected PaymentServiceComponent record;

	public Payment createPayment(CreatePaymentRequestBody requestBody, int id) {
		String vendorName = requestBody.vendorName;
		double amount = requestBody.amount;
		Payment transaction = PaymentFactory.createPayment("paymentgateway.payment.core.PaymentImpl",
				id,
				vendorName,
				amount);
		sendTransaction(requestBody);
		PaymentRepository.saveObject(transaction);
		return transaction;
	}
	
	public Payment createPayment(CreatePaymentRequestBody requestBody) {
		String vendorName = requestBody.vendorName;
		double amount = requestBody.amount;

		String generateUUIDNo = String.format("%010d",new BigInteger(UUID.randomUUID().toString().replace("-",""),16));
		String uniqueNo = generateUUIDNo.substring(0,5);
		int id = Integer.parseInt(uniqueNo);

		Payment transaction = PaymentFactory.createPayment("paymentgateway.payment.core.PaymentImpl",
				id,		
				vendorName,
				amount);
		sendTransaction(requestBody);
		PaymentRepository.saveObject(transaction);
		return transaction;
	}

	public Map<String, Object> sendTransaction(CreatePaymentRequestBody requestBody) {
		// to do implement this in deltas
		return null;
	}
	
	public Map<String, Object> checkPaymentStatus(CheckPaymentStatusRequestBody requestBody) {
		String vendorName = requestBody.vendorName;
		String Id = String.valueOf(requestBody.id);

		Config config = ConfigFactory.createConfig(vendorName, ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
		HttpClient client = HttpClient.newHttpClient();
		final String[] paymentMethodHolder = {""};
		
		PaymentRepository.executeQuery(session -> {
			String sql = String.format("SELECT modulesequence FROM payment_comp WHERE idtransaction ='%s'", Id );
			String result = (String) session.createNativeQuery(sql).getSingleResult();
			
			String[] modules = result.split(",");
			paymentMethodHolder[0] = modules[modules.length - 1].trim();
		});
		
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
        Map<String, Object> responseMap = new HashMap<>();
		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
            responseMap = config.getPaymentStatusResponse(rawResponse, Id);
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
	
	public List<HashMap<String, Object>> getAllPayment(String tableName){
		List<Payment> List = PaymentRepository.getAllObject(tableName);
		return transformListToHashMap(List);
	}
	
	public HashMap<String, Object> getPayment(GetPaymentRequestBody requestBody){
		int id = requestBody.id;
		Payment paymentImpl = this.getObject(id);
		HashMap<String, Object> paymentDataMap = paymentImpl.toHashMap();
		return paymentDataMap;
	}
	
	public List<HashMap<String, Object>> getAllPayment(GetAllPaymentRequestBody requestBody){
		String table = requestBody.tableName;
		List<Payment> List = PaymentRepository.getAllObject(table);
		return transformListToHashMap(List);
	}
	
	public HashMap<String, Object> getPaymentById(int id){
		List<HashMap<String, Object>> paymentList = getAllPayment("payment_impl");
		for (HashMap<String, Object> payment : paymentList){
			int record_id = ((Double) payment.get("record_id")).intValue();
			if (record_id == id){
				return payment;
			}
		}

		return null;
	}


	public HashMap<String, Object> updatePayment(UpdatePaymentRequestBody requestBody) {
		int id = requestBody.id;
		Payment payment = this.getObject(id);

		try {
			payment.setAmount(requestBody.amount);
		} catch (Exception e){
			e.printStackTrace();
		}

		this.updateObject(payment);
		
		return payment.toHashMap();

    }
	
	public List<HashMap<String, Object>> deletePayment(DeletePaymentRequestBody requestBody){
		int id = requestBody.id;
		Payment payment = this.getObject(id);
		this.deleteObject(id);

		return getAllPayment("payment_impl");
	}
	
	public Payment getObject(int id) {
        return PaymentRepository.getObject(id);
    }

    public void deleteObject(int id) {
        PaymentRepository.deleteObject(id);
    }

    public void updateObject(Payment payment) {
        PaymentRepository.updateObject(payment);
    }

    public List<Payment> getAllObject(String tableName) {
        return PaymentRepository.getAllObject(tableName);
    }
}
