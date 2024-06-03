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

	public Payment createPayment(VMJExchange vmjExchange) {
		Map<String, Object> response = sendTransaction(vmjExchange);
		String retailOutlet = (String) vmjExchange.getRequestBodyForm("retailOutlet");

		String retailPaymentCode = (String) response.get("retail_payment_code");
		int id = (int) response.get("id");
		
		Payment transaction = record.createPayment(vmjExchange, id);

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
	
	protected Map<String, Object> sendTransaction(VMJExchange vmjExchange) {
		String vendorName = (String) vmjExchange.getRequestBodyForm("vendor_name");

		Config config = ConfigFactory.createConfig(vendorName, ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));

		Gson gson = new Gson();
		Map<String, Object> requestMap = config.getRetailOutletRequestBody(vmjExchange);
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
			System.out.println("rawResponse " + rawResponse);
			responseMap = config.getRetailOutletResponse(rawResponse, id);
		} catch (Exception e) {
			System.out.println(e);
		}

		return responseMap;
	}

	
	@Route(url="call/retailoutlet")
	public HashMap<String,Object> RetailOutlet(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")){
			Payment result = this.createPayment(vmjExchange);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
	}
}

