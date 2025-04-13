package paymentgateway.payment.retailoutlet;

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
import java.util.Map;
import java.util.Random;

import paymentgateway.payment.PaymentFactory;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentServiceDecorator;
import paymentgateway.payment.core.PaymentImpl;
import paymentgateway.payment.core.PaymentServiceComponent;
import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;
import paymentgateway.config.core.CreatePaymentRequestBody;
import paymentgateway.config.core.CreateRetailOutletPaymentRequestBody;
public class PaymentServiceImpl extends PaymentServiceDecorator {

	public PaymentServiceImpl (PaymentServiceComponent record) {
        super(record);
    }

	public Payment createPayment(CreatePaymentRequestBody requestBody) {
		Map<String, Object> response = sendTransaction(requestBody);
		String retailOutlet = ((CreateRetailOutletPaymentRequestBody) requestBody).retailOutlet;

		if (response.containsKey("message")) {
			throw new IllegalStateException((String) response.get("message"));
		}
		
		String retailPaymentCode = (String) response.get("retail_payment_code");
		System.out.println("response " + response);
		int id = (int) response.get("id");
		
		Payment transaction = record.createPayment(requestBody, id);

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
	
	public Map<String, Object> sendTransaction(CreatePaymentRequestBody requestBody) {
		String vendorName = (String) requestBody.vendorName;

		Config config = ConfigFactory.createConfig(vendorName, ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));

		Gson gson = new Gson();
		Map<String, Object> requestMap = config.getRetailOutletRequestBody((CreateRetailOutletPaymentRequestBody) requestBody);
		int id = ((Integer) requestMap.get("id")).intValue();
		requestMap.remove("id");
		String requestString = config.getRequestString(requestMap);
		String configUrl = config.getProductEnv("RetailOutlet");
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
			System.out.println("rawResponse: " + rawResponse);
			responseMap = config.getRetailOutletResponse(rawResponse, id);
		} catch (Exception e) {
			System.out.println(e);
		}

		return responseMap;
	}
}

