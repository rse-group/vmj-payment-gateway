package paymentgateway.payment.virtualaccount;

import com.google.gson.Gson;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentServiceDecorator;
import paymentgateway.payment.core.PaymentImpl;
import paymentgateway.payment.core.PaymentServiceComponent;
import paymentgateway.payment.PaymentFactory;

public class PaymentServiceImpl extends PaymentServiceDecorator {

	public PaymentServiceImpl (PaymentServiceComponent record) {
        super(record);
    }
    
	public Payment createPayment(Map<String, Object> requestBody) {
		record.validateVendorName((String) requestBody.get("vendor_name"));
		double amount = record.validateAmount(requestBody.get("amount"));
		requestBody.put("amount", amount);
		
		Map<String, Object> response = sendTransaction(requestBody);

		String vaAccountNumber = (String) response.get("va_number");
		String id = (String) response.get("id");
		String status = (String) response.get("status");
		String vendorGeneratedId = (String) response.get("vendor_generated_id");

		String bankCode = (String) requestBody.get("bank");
		Payment transaction = record.createPayment(requestBody, id, status, vendorGeneratedId);
		Payment virtualAccountTransaction = PaymentFactory.createPayment(
				"paymentgateway.payment.virtualaccount.VirtualAccountImpl",
				transaction,
				bankCode,
				vaAccountNumber);
		PaymentRepository.saveObject(virtualAccountTransaction);
		return virtualAccountTransaction;
	}
	
	public Map<String, Object> sendTransaction(Map<String, Object> requestBody) {
		String vendorName = (String) requestBody.get("vendor_name");

		Config config = ConfigFactory.createConfig(vendorName, ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
		
		Gson gson = new Gson();
		Map<String, Object> requestMap = config.getVirtualAccountRequestBody(requestBody);
		String id = (String) requestMap.get("id");
		System.out.println("id:" + id);
		requestMap.remove("id");
		String requestString = config.getRequestString(requestMap);
		String configUrl = config.getProductEnv("VirtualAccount");
		HashMap<String, String> headerParams = config.getHeaderParams();
		System.out.println("configUrl: " + configUrl);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = (config.getBuilder(HttpRequest.newBuilder(),headerParams))
				.uri(URI.create(configUrl))
				.POST(HttpRequest.BodyPublishers.ofString(requestString))
				.build();

		Map<String, Object> responseMap = new HashMap<>();
		
		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			System.out.println("rawResponse " + rawResponse);
			responseMap = config.getVirtualAccountResponse(rawResponse, id);
		} catch (IOException | InterruptedException e) {
			System.out.println(e);
		}
		
		return responseMap;
	}
}

