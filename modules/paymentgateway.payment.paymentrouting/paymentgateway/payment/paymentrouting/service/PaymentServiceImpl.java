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
import paymentgateway.payment.core.PaymentServiceDecorator;
import paymentgateway.payment.core.PaymentImpl;
import paymentgateway.payment.core.PaymentServiceComponent;
import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

public class PaymentServiceImpl extends PaymentServiceDecorator {
	
	public PaymentServiceImpl (PaymentServiceComponent record) {
        super(record);
    }

	public Payment createPayment(Map<String, Object> requestBody) {
		Map<String, Object> response = sendTransaction(requestBody);

		int id = (int) response.get("id");
		String paymentCheckoutUrl = (String) response.get("payment_checkout_url");

		Payment transaction = record.createPayment(requestBody, id);
		Payment paymentRoutingTransaction = PaymentFactory.createPayment(
				"paymentgateway.payment.paymentrouting.PaymentImpl",
				transaction,
				paymentCheckoutUrl
				);

		PaymentRepository.saveObject(paymentRoutingTransaction);
		return paymentRoutingTransaction;
	}
	
	public Map<String, Object> sendTransaction(Map<String, Object> requestBody) {
		String vendorName = (String) requestBody.get("vendor_name");

		Config config = ConfigFactory.createConfig(vendorName, ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
		
		Gson gson = new Gson();
		Map<String, Object> requestMap = config.getPaymentRoutingRequestBody(requestBody);
		int id = ((Integer) requestMap.get("id")).intValue();
		requestMap.remove("id");
		String requestString = config.getRequestString(requestMap);
		String configUrl = config.getProductEnv("PaymentRouting");
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
			responseMap = config.getPaymentRoutingResponse(rawResponse, id);
		} catch (Exception e) {
			System.out.println(e);
		}

		return responseMap;
	}

}

