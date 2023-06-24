package paymentgateway.payment.debitcard;

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
import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

public class PaymentResourceImpl extends PaymentResourceDecorator {
	
	protected String apiKey;
	protected String apiEndpoint;
    public PaymentResourceImpl (PaymentResourceComponent record) {
    	super(record);
    }

	public Payment createPayment(VMJExchange vmjExchange, String productName, String serviceName) {
		DebitCardResponse response = sendTransaction(vmjExchange, productName, serviceName);
		String bankCode = (String) vmjExchange.getRequestBodyForm("payment_type");
		int id = response.getId();
		String directDebitUrl = response.getRedirect_url();
		Payment transaction = record.createPayment(vmjExchange, id, productName);

		Payment directDebitTransaction = 
				PaymentFactory.createPayment(
						"paymentgateway.payment.debitcard.PaymentImpl",
						transaction,
						bankCode,
						directDebitUrl
						);
		PaymentRepository.saveObject(directDebitTransaction);
		return directDebitTransaction;
	}
	
	protected DebitCardResponse sendTransaction(VMJExchange vmjExchange, String productName, String serviceName) {
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
		String configUrl = PaymentConfiguration.getProductEnv(productName, serviceName);
		HashMap<String, String> headerParams = PaymentConfiguration.getHeaderParams(productName);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = (PaymentConfiguration.getBuilder(HttpRequest.newBuilder(),headerParams))
				.uri(URI.create(configUrl))
				.POST(HttpRequest.BodyPublishers.ofString(requestString))
				.build();

		DebitCardResponse responseObj = null;
		
		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			System.out.println(rawResponse);
			responseObj = gson.fromJson(rawResponse, DebitCardResponse.class);
		} catch (Exception e) {
			System.out.println(e);
		}
		responseObj.setId(id);
		return responseObj;
	}

	
	@Route(url="call/debitcard")
	public HashMap<String,Object> testDebitCard(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) return null;

		String productName = (String) vmjExchange.getRequestBodyForm("product_name");
		Payment result = this.createPayment(vmjExchange, productName, "DebitCard");
		return result.toHashMap();
	}
}

