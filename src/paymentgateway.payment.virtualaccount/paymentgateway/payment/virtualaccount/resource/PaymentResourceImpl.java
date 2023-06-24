package paymentgateway.payment.virtualaccount;

import com.google.gson.Gson;

import paymentgateway.payment.PaymentConfiguration;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
    
	public Payment createPayment(VMJExchange vmjExchange, String productName, String serviceName) {
		VirtualAccountResponse response = sendTransaction(vmjExchange, productName, serviceName);
		System.out.println(response.getVirtual_account_number());
		String vaAccountNumber = response.getVirtual_account_number();
		String bankCode = (String) vmjExchange.getRequestBodyForm("bank");
		int id = response.getIdDB();
		
		Payment transaction = record.createPayment(vmjExchange, id, productName);
		Payment virtualAccountTransaction = PaymentFactory.createPayment(
				"paymentgateway.payment.virtualaccount.VirtualAccountImpl",
				transaction,
				bankCode,
				vaAccountNumber);
		PaymentRepository.saveObject(virtualAccountTransaction);
		return virtualAccountTransaction;
	}
	
	protected VirtualAccountResponse sendTransaction(VMJExchange vmjExchange, String productName, String serviceName) {
		Gson gson = new Gson();

		Config config = ConfigFactory
				.createConfig(
						"paymentgateway.config." + productName.toLowerCase() + "." + productName + "Configuration"
						,
						ConfigFactory.createConfig(
								"paymentgateway.config.core.ConfigImpl"));

		Map<String, Object> requestMap = config.processRequestMap(vmjExchange,productName,serviceName);
		int id = ((Integer) requestMap.get("id")).intValue();
		System.out.println("id:" + Integer.toString(id));
		requestMap.remove("id");
		String requestString = gson.toJson(requestMap);
		String configUrl = PaymentConfiguration.getProductEnv(productName, serviceName);
		HashMap<String, String> headerParams = PaymentConfiguration.getHeaderParams(productName);
		System.out.println("configUrl: " + configUrl);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = (PaymentConfiguration.getBuilder(HttpRequest.newBuilder(),headerParams))
				.uri(URI.create(configUrl))
				.POST(HttpRequest.BodyPublishers.ofString(requestString))
				.build();


		VirtualAccountResponse responseObj = null;
		
		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			System.out.println("raw: " + rawResponse);
			responseObj = gson.fromJson(rawResponse, VirtualAccountResponse.class);
		} catch (Exception e) {
			System.out.println(e);
		}
		responseObj.setIdDB(id);
		
		return responseObj;
	}

	
	@Route(url="call/virtualaccount")
	public HashMap<String,Object> testVirtualAccount(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) return null;

		String productName = (String) vmjExchange.getRequestBodyForm("product_name");
		Payment result = this.createPayment(vmjExchange, productName, "VirtualAccount");
		return result.toHashMap();
	}
}

