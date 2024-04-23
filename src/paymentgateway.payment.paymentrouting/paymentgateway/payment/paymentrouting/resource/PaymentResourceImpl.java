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

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

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

	public Payment createPayment(VMJExchange vmjExchange) {
		Map<String, Object> response = sendTransaction(vmjExchange);

		int id = (int) response.get("id");
		String paymentCheckoutUrl = (String) response.get("payment_checkout_url");

		String paymentMethods = (String) vmjExchange.getRequestBodyForm("list_enable_payment_method");
		String sourceOfFunds = (String) vmjExchange.getRequestBodyForm("list_enable_sof");
		Gson gson = new Gson();
		Type resultType = new TypeToken<List<PaymentRoutingRecipient>>(){}.getType();
		List<PaymentRoutingRecipient> routings = gson.fromJson(gson.toJson(vmjExchange.getRequestBodyForm("routings")), resultType);
//		List<PaymentRoutingRecipient> routings = (List<PaymentRoutingRecipient>) vmjExchange.getRequestBodyForm("routings");

		Payment transaction = record.createPayment(vmjExchange, id);
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
	
	protected Map<String, Object> sendTransaction(VMJExchange vmjExchange) {
		Gson gson = new Gson();

		Config config = ConfigFactory.createConfig(ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));

		Map<String, Object> requestMap = config.getPaymentRoutingRequestBody(vmjExchange);
		int id = ((Integer) requestMap.get("id")).intValue();
		requestMap.remove("id");
		String requestString = config.getRequestString(requestMap);
		String configUrl = config.getProductEnv("PaymentRouting");
		HashMap<String, String> headerParams = config.getHeaderParams();
		System.out.println("configUrl: " + configUrl);
		System.out.println(configUrl);
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
			responseMap = config.getPaymentRoutingResponse(rawResponse, id);
		} catch (Exception e) {
			System.out.println(e);
		}

		return responseMap;
	}
	
	@Route(url="call/paymentrouting")
	public HashMap<String,Object> paymentLinkEndpoint(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")){
			Payment result = this.createPayment(vmjExchange);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
	}

}

