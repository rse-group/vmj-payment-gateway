package paymentgateway.payment.debitcard;

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
	
	protected String apiKey;
	protected String apiEndpoint;
    public PaymentResourceImpl (PaymentResourceComponent record) {
    	super(record);
    }

	public Payment createPayment(VMJExchange vmjExchange) {
		Map<String, Object> response = sendTransaction(vmjExchange);

		String bankCode = (String) response.get("payment_type");
		int id = (int) response.get("id");
		String directDebitUrl = (String) response.get("redirect_url");

		Payment transaction = record.createPayment(vmjExchange, id);

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
	
	protected Map<String, Object> sendTransaction(VMJExchange vmjExchange) {
		Gson gson = new Gson();

		Config config = ConfigFactory.createConfig(ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));

		Map<String, Object> requestMap = config.getDebitCardRequestBody(vmjExchange);
		int id = ((Integer) requestMap.get("id")).intValue();
		requestMap.remove("id");
		String requestString = config.getRequestString(requestMap);
		String configUrl = config.getProductEnv("DebitCard");
		HashMap<String, String> headerParams = config.getHeaderParams();
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = (config.getBuilder(HttpRequest.newBuilder(),headerParams))
				.uri(URI.create(configUrl))
				.POST(HttpRequest.BodyPublishers.ofString(requestString))
				.build();

		Map<String, Object> responseMap = new HashMap<>();;
		
		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			System.out.println("rawResponse " + rawResponse);
			responseMap = config.getDebitCardResponse(rawResponse, id);
		} catch (Exception e) {
			System.out.println(e);
		}
		return responseMap;
	}

	
	@Route(url="call/debitcard")
	public HashMap<String,Object> testDebitCard(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")){
			Payment result = this.createPayment(vmjExchange);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
	}
}

