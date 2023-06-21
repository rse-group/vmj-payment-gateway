package paymentgateway.payment.ewallet;

import com.google.gson.Gson;

import java.lang.reflect.*;

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
import paymentgateway.payment.PaymentConfiguration;

public class PaymentResourceImpl extends PaymentResourceDecorator {
	// implement this with author

	protected String apiKey;
	protected String apiEndpoint;
    public PaymentResourceImpl (PaymentResourceComponent record) {
        super(record);
//        this.apiKey = "SB-Mid-server-NVYFqUidEQUTaozWjW77fFWW";
//		this.apiEndpoint = "https://api.sandbox.midtrans.com/v2/charge";
    }

	
	public Payment createPayment(VMJExchange vmjExchange, String productName, String serviceName) {
		EWalletResponse response = sendTransaction(vmjExchange, productName, serviceName);
		String url = response.getUrl();
		String type = response.getPayment_type();
		int id = response.getId();
		String phoneNumber = (String) vmjExchange.getRequestBodyForm("phone_number");
		System.out.println(id);
		Payment transaction = record.createPayment(vmjExchange, id, productName);
		Payment ewalletTransaction =
			PaymentFactory.createPayment(
					"paymentgateway.payment.ewallet.EWalletImpl",
					transaction,
					phoneNumber,
					type,
					url
					);
		PaymentRepository.saveObject(ewalletTransaction);
		return ewalletTransaction;
	}

	protected EWalletResponse sendTransaction(VMJExchange vmjExchange, String productName, String serviceName) {
		
		Gson gson = new Gson();
		Map<String, Object> requestMap = PaymentConfiguration.processRequestMap(vmjExchange,productName,serviceName);
		int id = ((Integer) requestMap.get("id")).intValue();
		requestMap.remove("id");
		String requestString = gson.toJson(requestMap);
		String configUrl = PaymentConfiguration.getProductEnv(productName, serviceName);
		HashMap<String, String> headerParams = PaymentConfiguration.getHeaderParams(productName);
		System.out.println("configUrl: " + configUrl);
		System.out.println(configUrl);
//		Gson gson = new Gson();
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = (PaymentConfiguration.getBuilder(HttpRequest.newBuilder(),headerParams))
				.uri(URI.create(configUrl))
				.POST(HttpRequest.BodyPublishers.ofString(requestString))
				.build();

		EWalletResponse responseObj = null;
		
		try {
			System.out.println(request.toString());
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			System.out.println("raw: " + rawResponse);
			responseObj = gson.fromJson(rawResponse, EWalletResponse.class);
			
		} catch (Exception e) {
			System.out.println(e);
		}
		responseObj.setId(id);
		
		return responseObj;
	}

	
	@Route(url="call/ewallet")
	public HashMap<String,Object> Ewallet(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS"))
			return null;

		System.out.println("masuk call");
		String productName = (String) vmjExchange.getRequestBodyForm("product_name");
		Payment result = this.createPayment(vmjExchange, productName,"EWallet");
		return result.toHashMap();
	}
}

