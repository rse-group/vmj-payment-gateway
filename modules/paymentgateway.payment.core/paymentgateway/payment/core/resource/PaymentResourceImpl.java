package paymentgateway.payment.core;

import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import vmj.routing.route.RequestMethod;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import paymentgateway.payment.PaymentFactory;
import vmj.auth.annotations.Restricted;
import vmj.routing.route.exceptions.*;

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

public class PaymentResourceImpl extends PaymentResourceComponent {
	protected PaymentResourceComponent record;

	private PaymentServiceImpl paymentServiceImpl = new PaymentServiceImpl();

	@Route(url = "call/payment", method = RequestMethod.POST, requestBodyClass = CreatePaymentRequestBody.class)
	public HashMap<String, Object> payment(VMJExchange<CreatePaymentRequestBody> vmjExchange) {
		CreatePaymentRequestBody requestBody = vmjExchange.getParsedPayload();
		Payment result = paymentServiceImpl.createPayment(requestBody);
		return result.toHashMap();
	}

	@Route(url = "call/paymentstatus", method = RequestMethod.POST, requestBodyClass = CheckPaymentStatusRequestBody.class)
	public Map<String, Object> paymentStatus(VMJExchange<CheckPaymentStatusRequestBody> vmjExchange) {
		CheckPaymentStatusRequestBody requestBody = vmjExchange.getParsedPayload();
		return paymentServiceImpl.checkPaymentStatus(requestBody);
	}

	@Route(url = "call/payment/callback")
	public int callback(VMJExchange vmjExchange) {
		String workingDir = System.getProperty("user.dir");
		List<File> propertyFiles = new ArrayList<>();

		List<String> vendors = new ArrayList<>();

		String[] targetFiles = { "oy.properties", "flip.properties", "midtrans.properties" };

		// Iterate through target files
		for (String targetFile : targetFiles) {
			File file = new File(workingDir, targetFile);
			if (file.exists()) {
				String fileName = file.getName();
				String nameBeforeDot = fileName.substring(0, fileName.indexOf('.'));
				String capitalized = nameBeforeDot.substring(0, 1).toUpperCase() + nameBeforeDot.substring(1);
				vendors.add(capitalized);
			}
		}

		for (String vendor : vendors) {
			try {
				Config config = ConfigFactory.createConfig(vendor,
						ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
				Map<String, Object> requestMap = config.getCallbackPaymentRequestBody(vmjExchange);

				String idStr = (String) requestMap.get("id");
				String status = (String) requestMap.get("status");

				String hostAddress = paymentServiceImpl.getEnvVariableHostAddress("AMANAH_HOST_BE");
				int portNum = paymentServiceImpl.getEnvVariablePortNumber("AMANAH_PORT_BE");

				HttpClient client = HttpClient.newHttpClient();
				String configUrl = String.format("http://%s:%d/call/receivecallback", hostAddress, portNum);
				// String configUrl = "http://localhost:443/call/receivecallback";
				String requestString = config.getRequestString(requestMap);
				HttpRequest request = config.getBuilder(HttpRequest.newBuilder(), config.getHeaderParams())
						.uri(URI.create(configUrl))
						.POST(HttpRequest.BodyPublishers.ofString(requestString))
						.build();

				try {
					HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
					String rawResponse = response.body();
				} catch (Exception e) {
					System.err.println("Failed to send request for vendor: " + vendor);
					e.printStackTrace();
				}
			} catch (Exception e) {
				System.err.println("Failed to process vendor: " + vendor);
				e.printStackTrace();
			}
		}

		return 200;
	}

	@Route(url = "call/payment/list", method = RequestMethod.GET, requestBodyClass = GetAllPaymentRequestBody.class)
	public List<HashMap<String, Object>> getAllPayment(VMJExchange<GetAllPaymentRequestBody> vmjExchange) {
		GetAllPaymentRequestBody requestBody = vmjExchange.getParsedPayload();
		return paymentServiceImpl.getAllPayment(requestBody);
	}

	@Route(url = "call/payment/detail", method = RequestMethod.GET, requestBodyClass = GetPaymentRequestBody.class)
	public HashMap<String, Object> getPayment(VMJExchange<GetPaymentRequestBody> vmjExchange) {
		GetPaymentRequestBody requestBody = vmjExchange.getParsedPayload();
		return paymentServiceImpl.getPayment(requestBody);
	}

	@Route(url = "call/payment/delete", method = RequestMethod.DELETE, requestBodyClass = DeletePaymentRequestBody.class)
	public List<HashMap<String, Object>> deletePayment(VMJExchange<DeletePaymentRequestBody> vmjExchange) {
		DeletePaymentRequestBody requestBody = vmjExchange.getParsedPayload();
		System.out.println(requestBody);
		return paymentServiceImpl.deletePayment(requestBody);
	}

	@Route(url = "call/payment/update", method = RequestMethod.PUT, requestBodyClass = UpdatePaymentRequestBody.class)
	public HashMap<String, Object> updatePayment(VMJExchange<UpdatePaymentRequestBody> vmjExchange) {
		UpdatePaymentRequestBody requestBody = vmjExchange.getParsedPayload();
		return paymentServiceImpl.updatePayment(requestBody);
	}

}
