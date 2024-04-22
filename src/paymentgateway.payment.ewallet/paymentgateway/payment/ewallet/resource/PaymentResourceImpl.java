package paymentgateway.payment.ewallet;

import com.google.gson.Gson;

import java.lang.reflect.*;

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
import java.util.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import paymentgateway.payment.PaymentFactory;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentResourceDecorator;
import paymentgateway.payment.core.PaymentImpl;
import paymentgateway.payment.core.PaymentResourceComponent;
import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

public class PaymentResourceImpl extends PaymentResourceDecorator {
	// implement this with author

	protected String apiKey;
	protected String apiEndpoint;
    public PaymentResourceImpl (PaymentResourceComponent record) {
        super(record);
    }

	
	public Payment createPayment(VMJExchange vmjExchange) {
		Map<String, Object> response = sendTransaction(vmjExchange);

		String url = (String) response.get("url");
		String type = (String) response.get("payment_type");
		int id = (int) response.get("id");

		String phoneNumber = (String) vmjExchange.getRequestBodyForm("phone_number");
		System.out.println(id);
		Payment transaction = record.createPayment(vmjExchange, id);
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

	protected Map<String, Object> sendTransaction(VMJExchange vmjExchange) {
		Gson gson = new Gson();

		Config config = ConfigFactory.createConfig(ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));

		Map<String, Object> requestMap = config.getEWalletRequestBody(vmjExchange);
		int id = ((Integer) requestMap.get("id")).intValue();
		requestMap.remove("id");
		String requestString = gson.toJson(requestMap);
		String configUrl = config.getProductEnv("EWallet");
		HashMap<String, String> headerParams = config.getHeaderParams();
		System.out.println("configUrl: " + configUrl);
		System.out.println(9);

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = (config.getBuilder(HttpRequest.newBuilder(),headerParams))
				.uri(URI.create(configUrl))
				.POST(HttpRequest.BodyPublishers.ofString(requestString))
				.build();

		Map<String, Object> responseMap = new HashMap<>();
		
		try {
			System.out.println(request.toString());
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			System.out.println("rawResponse " + rawResponse);
			responseMap = config.getEWalletResponse(rawResponse, id);
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return responseMap;
	}

	public String getParamsUrlEncoded(Map<String, Object> vmjExchange) {
		ArrayList<String> paramList = new ArrayList<>();
		for (Map.Entry<String, Object> entry : vmjExchange.entrySet()) {
			String key = entry.getKey();
			Object val = entry.getValue();
			if (val instanceof String) {
				paramList.add(key + "=" + URLEncoder.encode(val.toString(), StandardCharsets.UTF_8));
			} else if (val instanceof Integer) {
				paramList.add(key + "=" + URLEncoder.encode(val.toString(), StandardCharsets.UTF_8));
			} else if (val instanceof Double) {
				int temp = ((Double) val).intValue();
				paramList.add(key + "=" + URLEncoder.encode(Integer.toString(temp), StandardCharsets.UTF_8));
			}

		}
		String encodedURL = String.join("&",paramList);
		return encodedURL;
	}

	
	@Route(url="call/ewallet")
	public HashMap<String,Object> Ewallet(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")){
			Payment result = this.createPayment(vmjExchange);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
	}
}

