package paymentgateway.payment.qrcode;

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

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;
import paymentgateway.payment.core.CreatePaymentRequestBody;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentServiceDecorator;
import paymentgateway.payment.core.PaymentImpl;
import paymentgateway.payment.core.PaymentServiceComponent;
import paymentgateway.payment.PaymentFactory;

public class PaymentServiceImpl extends PaymentServiceDecorator {

	public PaymentServiceImpl(PaymentServiceComponent record) {
		super(record);
	}

	public Payment createPayment(CreatePaymentRequestBody requestBody) {
		Map<String, Object> response = sendTransaction(requestBody);
		String currency = ((CreateQRCodePaymentRequestBody) requestBody).currency;

		if (response.containsKey("message")) {
			throw new IllegalStateException((String) response.get("message"));
		}

		String qrCodeString = (String) response.get("qr_code_string");
		System.out.println("response " + response);
		int id = (int) response.get("id");

		Payment transaction = record.createPayment(requestBody, id);

		Payment qrCodeChannel = PaymentFactory.createPayment(
				"paymentgateway.payment.qrcode.QRCodeImpl",
				transaction,
				currency,
				qrCodeString);
		PaymentRepository.saveObject(qrCodeChannel);
		return qrCodeChannel;
	}

	public Map<String, Object> sendTransaction(CreatePaymentRequestBody requestBody) {
		String vendorName = (String) requestBody.vendorName;

		Config config = ConfigFactory.createConfig(vendorName,
				ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));

		Gson gson = new Gson();
		Map<String, Object> requestMap = config.getQRCodeRequestBody(requestBody.toMap());
		int id = ((Integer) requestMap.get("id")).intValue();
		requestMap.remove("id");
		String requestString = config.getRequestString(requestMap);
		String configUrl = config.getProductEnv("QRCode");
		HashMap<String, String> headerParams = config.getHeaderParams();
		System.out.println("configUrl: " + configUrl);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = (config.getBuilder(HttpRequest.newBuilder(), headerParams))
				.uri(URI.create(configUrl))
				.POST(HttpRequest.BodyPublishers.ofString(requestString))
				.build();

		Map<String, Object> responseMap = new HashMap<>();

		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			System.out.println("rawResponse: " + rawResponse);
			responseMap = config.getQRCodeResponse(rawResponse, id);
		} catch (Exception e) {
			System.out.println(e);
		}

		return responseMap;
	}
}
