package paymentgateway.payment.qrcode;

import com.google.gson.Gson;

import vmj.hibernate.integrator.RepositoryUtil;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentServiceDecorator;
import paymentgateway.payment.core.PaymentImpl;
import paymentgateway.payment.core.PaymentServiceComponent;
import paymentgateway.payment.PaymentFactory;

public class PaymentServiceImpl extends PaymentServiceDecorator {
	RepositoryUtil<QRCodeImpl> qrCodePaymentRepository;

	public PaymentServiceImpl(PaymentServiceComponent record) {
		super(record);
		this.qrCodePaymentRepository = new RepositoryUtil<QRCodeImpl>(paymentgateway.payment.qrcode.QRCodeImpl.class);
	}

	public Payment createPayment(Map<String, Object> requestBody) {
		record.validateVendorName((String) requestBody.get("vendor_name"));
		double amount = record.validateAmount(requestBody.get("amount"));
		requestBody.put("amount", amount);
		
		Map<String, Object> response = sendTransaction(requestBody);

		System.out.println("response " + response);

		String qrCodeString = (String) response.get("qr_code_string");
		String id = (String) response.get("id");
		String status = (String) response.get("status");
		String vendorGeneratedId = (String) response.get("vendor_generated_id");
		String expiryDateString = (String) response.get("expires_at");
		String channelCode = (String) response.get("channel_code");

		Payment transaction = record.createPayment(requestBody, id, status, vendorGeneratedId);		

		Payment qrCodeChannel = PaymentFactory.createPayment(
				"paymentgateway.payment.qrcode.QRCodeImpl",
				transaction,
				qrCodeString,
				channelCode,
				expiryDateString);
		PaymentRepository.saveObject(qrCodeChannel);
		return qrCodeChannel;
	}

	public Map<String, Object> sendTransaction(Map<String, Object> requestBody) {
		String vendorName = (String) requestBody.get("vendor_name");

		Config config = ConfigFactory.createConfig(vendorName,
				ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));

		Gson gson = new Gson();
		Map<String, Object> requestMap = config.getQRCodeRequestBody(requestBody);
		String id = (String) requestMap.get("id");
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
		} catch (IOException | InterruptedException e) {
			System.out.println(e);
		}
		System.out.println("==============================================");
        System.out.println("qr code send transaction response: " + responseMap);
        System.out.println("==============================================");
		return responseMap;
	}

	public List<QRCodeImpl> getByVendorName(Map<String, String> queryParams) {
		String vendorName = (String) queryParams.get("vendor_name");
		record.validateVendorName(vendorName);
		List<QRCodeImpl> result = new ArrayList<>();
		List<QRCodeImpl> qrCodePayments = qrCodePaymentRepository.getAllObject("qrcode_impl");
		for(QRCodeImpl qrCodePayment : qrCodePayments){
			if (qrCodePayment.getVendorName().equals(vendorName)){
				result.add(qrCodePayment);
			}
		}
		return result;
	}

	public HashMap<String, Object> getById(Map<String, String> queryParams) {
		String id = (String) queryParams.get("id");
		String validatedId = record.validateId(id);
		List<QRCodeImpl> qrCodePayments = qrCodePaymentRepository.getAllObject("qrcode_impl");
		for(QRCodeImpl qrCodePayment : qrCodePayments){
			if (qrCodePayment.getIdTransaction().toString().equals(validatedId)){
				return qrCodePayment.toHashMap();
			}
		}
		throw new BadRequestException("QR Code payment dengan ID " + validatedId + " tidak ditemukan");
	}
}
