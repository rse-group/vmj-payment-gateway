package paymentgateway.payment.virtualaccount;

import com.google.gson.Gson;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

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

import paymentgateway.payment.PaymentFactory;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentResourceDecorator;
import paymentgateway.payment.core.PaymentImpl;
import paymentgateway.payment.core.PaymentResourceComponent;
import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

public class PaymentResourceImpl extends PaymentResourceDecorator {
	// implement this to work with authorization module
    public PaymentResourceImpl (PaymentResourceComponent record) {
        super(record);
    }
    
	public Payment createPayment(VMJExchange vmjExchange) {
		Map<String, Object> response = sendTransaction(vmjExchange);

		String vaAccountNumber = (String) response.get("va_number");
		int id = (int) response.get("id");

		String bankCode = (String) vmjExchange.getRequestBodyForm("bank");
		Payment transaction = record.createPayment(vmjExchange, id);
		Payment virtualAccountTransaction = PaymentFactory.createPayment(
				"paymentgateway.payment.virtualaccount.VirtualAccountImpl",
				transaction,
				bankCode,
				vaAccountNumber);
		PaymentRepository.saveObject(virtualAccountTransaction);
		return virtualAccountTransaction;
	}
	
	protected Map<String, Object> sendTransaction(VMJExchange vmjExchange) {
		Gson gson = new Gson();

		Config config = ConfigFactory.createConfig(ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));

		Map<String, Object> requestMap = config.getVirtualAccountRequestBody(vmjExchange);
		int id = ((Integer) requestMap.get("id")).intValue();
		System.out.println("id:" + Integer.toString(id));
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
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return responseMap;
	}
	
	@Route(url="call/virtualaccount")
	public HashMap<String,Object> testVirtualAccount(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")){
			Payment result = this.createPayment(vmjExchange);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
	}

	
}

