package paymentgateway.payment.ewallet;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.*;

import vmj.hibernate.integrator.RepositoryUtil;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentServiceDecorator;
import paymentgateway.payment.core.PaymentImpl;
import paymentgateway.payment.core.PaymentServiceComponent;
import paymentgateway.payment.PaymentFactory;

public class PaymentServiceImpl extends PaymentServiceDecorator {
	// implement this with author

	RepositoryUtil<EWalletImpl> ewalletPaymentRepository;

	public PaymentServiceImpl (PaymentServiceComponent record) {
        super(record);
		this.ewalletPaymentRepository = new RepositoryUtil<EWalletImpl>(paymentgateway.payment.ewallet.EWalletImpl.class);
    }

	
	public Payment createPayment(Map<String, Object> requestBody) {
		record.validateVendorName((String) requestBody.get("vendor_name"));
		double amount = record.validateAmount(requestBody.get("amount"));
		requestBody.put("amount", amount);

		Map<String, Object> response = sendTransaction(requestBody);

		String url = (String) response.get("url");
		String type = (String) response.get("payment_type");
		String id = (String) response.get("id");
		String status = (String) response.get("status");
		String vendorGeneratedId = (String) response.get("vendor_generated_id");

		String phoneNumber = (String) requestBody.get("phone");
		System.out.println(id);
		Payment transaction = record.createPayment(requestBody, id, status, vendorGeneratedId);
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

	public Map<String, Object> sendTransaction(Map<String, Object> requestBody) {
		String vendorName = (String) requestBody.get("vendor_name");

		Config config = ConfigFactory.createConfig(vendorName, ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
		
		Gson gson = new Gson();
		Map<String, Object> requestMap = config.getEWalletRequestBody(requestBody);
		String id = (String) requestMap.get("id");
		requestMap.remove("id");
		String configUrl = config.getProductEnv("EWallet");
		HashMap<String, String> headerParams = config.getHeaderParams();
		System.out.println("configUrl: " + configUrl);
		String requestString = config.getRequestString(requestMap);
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
			
		} catch (IOException | InterruptedException e) {
			System.out.println(e);
		}
		
		return responseMap;
	}

	public List<EWalletImpl> getByVendorName(Map<String, String> queryParams) {
		String vendorName = (String) queryParams.get("vendor_name");
		record.validateVendorName(vendorName);
		List<EWalletImpl> result = new ArrayList<>();
		List<EWalletImpl> ewalletPayments = ewalletPaymentRepository.getAllObject("ewallet_impl");
		for(EWalletImpl payment : ewalletPayments){
			if (payment.getVendorName().equals(vendorName)){
				result.add(payment);
			}
		}
		return result;
	}

	public HashMap<String, Object> getById(Map<String, String> queryParams) {
		String id = (String) queryParams.get("id");
		String validatedId = record.validateId(id);
		List<EWalletImpl> ewalletPayments = ewalletPaymentRepository.getAllObject("ewallet_impl");
		for(EWalletImpl payment : ewalletPayments){
			if (payment.getIdTransaction().toString().equals(validatedId)){
				return payment.toHashMap();
			}
		}
		throw new BadRequestException("E-wallet payment dengan ID " + validatedId + " tidak ditemukan");
	}
}

