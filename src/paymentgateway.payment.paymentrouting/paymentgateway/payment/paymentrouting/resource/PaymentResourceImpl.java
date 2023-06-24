package paymentgateway.payment.paymentrouting;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import paymentgateway.payment.PaymentConfiguration;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import paymentgateway.payment.PaymentFactory;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentResourceDecorator;
import paymentgateway.payment.core.PaymentImpl;
import paymentgateway.payment.core.PaymentResourceComponent;
import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

public class PaymentResourceImpl extends PaymentResourceDecorator {
	
	protected String apiKey;
	protected String apiUsername;
	protected String apiEndpoint;
    public PaymentResourceImpl (PaymentResourceComponent record) {
        super(record);
    }

	public Payment createPayment(VMJExchange vmjExchange, String productName, String serviceName) {
		PaymentRoutingResponse response = sendTransaction(vmjExchange, productName, serviceName);
		int id = response.getId();
		String paymentCheckoutUrl = response.getPayment_info().getPayment_checkout_url();
		String paymentMethods = (String) vmjExchange.getRequestBodyForm("list_enable_payment_method");
		String sourceOfFunds = (String) vmjExchange.getRequestBodyForm("list_enable_sof");

		Gson gson = new Gson();
		Type resultType = new TypeToken<List<PaymentRoutingRecipient>>(){}.getType();
		List<PaymentRoutingRecipient> routings = gson.fromJson(gson.toJson(vmjExchange.getRequestBodyForm("routings")), resultType);
//		List<PaymentRoutingRecipient> routings = (List<PaymentRoutingRecipient>) vmjExchange.getRequestBodyForm("routings");

		Payment transaction = record.createPayment(vmjExchange, id, productName);
		Payment paymentRoutingTransaction = PaymentFactory.createPayment(
				"paymentgateway.payment.paymentrouting.PaymentImpl",
				transaction,
				paymentMethods,
				sourceOfFunds,
				routings,
				paymentCheckoutUrl
				);

		PaymentRepository.saveObject(paymentRoutingTransaction);
		return paymentRoutingTransaction;
	}
	
	protected PaymentRoutingResponse sendTransaction(VMJExchange vmjExchange, String productName, String serviceName) {
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
		System.out.println("configUrl: " + configUrl);
		System.out.println(configUrl);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = (PaymentConfiguration.getBuilder(HttpRequest.newBuilder(),headerParams))
				.uri(URI.create(configUrl))
				.POST(HttpRequest.BodyPublishers.ofString(requestString))
				.build();

		PaymentRoutingResponse responseObj = null;
		
		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			System.out.println(rawResponse);
			responseObj = gson.fromJson(rawResponse, PaymentRoutingResponse.class);
		} catch (Exception e) {
			System.out.println(e);
		}

		responseObj.setId(id);
		return responseObj;
	}
	
	@Route(url="call/paymentrouting")
	public HashMap<String,Object> paymentLinkEndpoint(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) return null;
//		int amount = ((Double) vmjExchange.getRequestBodyForm("amount")).intValue();
//		String idTransaction = (String) vmjExchange.getRequestBodyForm("idTransaction");
//		String paymentMethods = (String) vmjExchange.getRequestBodyForm("paymentMethods");
//		String sourceOfFunds = (String) vmjExchange.getRequestBodyForm("sourceOfFunds");
//		List<PaymentRoutingRecipient> routings = new ArrayList<PaymentRoutingRecipient>();
//		List<Map<String,Object>> recipient = (List<Map<String,Object>>) vmjExchange.getRequestBodyForm("routings");
//		for(Map<String,Object> x : recipient) {
//			PaymentRoutingRecipient temp = new PaymentRoutingRecipient();
//			temp.setRecipient_account((String) x.get("recipient_account"));
//			temp.setRecipient_bank((String) x.get("recipient_bank"));
//			temp.setRecipient_amount(((Double) x.get("recipient_amount")).intValue());
//			temp.setRecipient_email((String) x.get("recipient_email"));
//			temp.setRecipient_note((String) x.get("recipient_note"));
//			routings.add(temp);
//		}
//
//		HashMap<String,Object> testVMJExchange = new HashMap<String,Object>();
//		testVMJExchange.put("idTransaction", idTransaction);
//		testVMJExchange.put("amount", amount);
//		testVMJExchange.put("paymentMethods", paymentMethods);
//		testVMJExchange.put("sourceOfFunds", sourceOfFunds);
//		testVMJExchange.put("routings", routings);
		String productName = (String) vmjExchange.getRequestBodyForm("product_name");
		Payment result = this.createPayment(vmjExchange,productName,"PaymentRouting");
//		Payment result = this.createPayment(vmjExchange, "Oy", );
		return result.toHashMap();
	}

}

