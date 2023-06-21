package paymentgateway.payment.retailoutlet;

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
import java.util.Map;
import java.util.Random;

import paymentgateway.payment.PaymentFactory;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentResourceDecorator;
import paymentgateway.payment.core.PaymentImpl;
import paymentgateway.payment.core.PaymentResourceComponent;

public class PaymentResourceImpl extends PaymentResourceDecorator {
	// implement this with authorization module
	protected String apiKey;
	protected String apiEndpoint;
	protected final String payment_type = "cstore";
    public PaymentResourceImpl (PaymentResourceComponent record) {
    	super(record);
//    	this.apiKey = "SB-Mid-server-NVYFqUidEQUTaozWjW77fFWW";
//    	this.apiEndpoint = "https://api.sandbox.midtrans.com/v2/charge";
    }

	public Payment createPayment(VMJExchange vmjExchange, String productName, String serviceName) {
		RetailOutletResponse response = sendTransaction(vmjExchange,productName,serviceName);
		String retailOutlet = (String) vmjExchange.getRequestBodyForm("retailOutlet");
		String retailPaymentCode = response.getCode();
		int id = response.getId();
		
		Payment transaction = record.createPayment(vmjExchange, id, productName);

		Payment retailOutletChannel =
				PaymentFactory.createPayment(
						"paymentgateway.payment.retailoutlet.RetailOutletImpl",
						transaction,
						retailOutlet,
						retailPaymentCode
						);
		PaymentRepository.saveObject(retailOutletChannel);
		return retailOutletChannel;
	}
	
	protected RetailOutletResponse sendTransaction(VMJExchange vmjExchange, String productName, String serviceName) {
		Gson gson = new Gson();
		Map<String, Object> requestMap = PaymentConfiguration.processRequestMap(vmjExchange,productName,serviceName);
		int id = ((Integer) requestMap.get("id")).intValue();
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


		RetailOutletResponse responseObj = null;
		
		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			System.out.println("raw: " + rawResponse);
			responseObj = gson.fromJson(rawResponse, RetailOutletResponse.class);
		} catch (Exception e) {
			System.out.println(e);
		}
		responseObj.setId(id);

		return responseObj;
	}

	
	@Route(url="call/retailoutlet")
	public HashMap<String,Object> RetailOutlet(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) return null;

		String productName = (String) vmjExchange.getRequestBodyForm("product_name");
		Payment result = this.createPayment(vmjExchange, productName, "RetailOutlet");
		return result.toHashMap();
	}
}

