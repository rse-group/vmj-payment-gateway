package paymentgateway.payment.retailoutlet;

import com.google.gson.Gson;

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
import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;
public class PaymentResourceImpl extends PaymentResourceDecorator {
	// implement this with authorization module
	protected String apiKey;
	protected String apiEndpoint;
    public PaymentResourceImpl (PaymentResourceComponent record) {
    	super(record);
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

		Config config = ConfigFactory
				.createConfig(
						"paymentgateway.config." + productName.toLowerCase() + "." + productName + "Configuration"
						,
						ConfigFactory.createConfig(
								"paymentgateway.config.core.ConfigImpl"));

		Map<String, Object> requestMap = config.processRequestMap(vmjExchange,productName,serviceName);
		int id = ((Integer) requestMap.get("id")).intValue();
		requestMap.remove("id");
		String requestString = gson.toJson(requestMap);
		String configUrl = config.getProductEnv(productName, serviceName);
		HashMap<String, String> headerParams = config.getHeaderParams(productName);
		System.out.println("configUrl: " + configUrl);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = (config.getBuilder(HttpRequest.newBuilder(),headerParams))
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

