package paymentgateway.payment.retailoutlet;

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
	RepositoryUtil<RetailOutletImpl> retailOutletPaymentRepository;

	public PaymentServiceImpl (PaymentServiceComponent record) {
        super(record);
		this.retailOutletPaymentRepository = new RepositoryUtil<RetailOutletImpl>(paymentgateway.payment.retailoutlet.RetailOutletImpl.class);
    }

	public Payment createPayment(Map<String, Object> requestBody) {
		record.validateVendorName((String) requestBody.get("vendor_name"));
		double amount = record.validateAmount(requestBody.get("amount"));
		requestBody.put("amount", amount);
		
		Map<String, Object> response = sendTransaction(requestBody);
		String retailOutlet = (String) requestBody.get("retail_outlet");
		
		String retailPaymentCode = (String) response.get("retail_payment_code");
		System.out.println("response " + response);
		String id = (String) response.get("id");
		String status = (String) response.get("status");
		String vendorGeneratedId = (String) response.get("vendor_generated_id");
		
		Payment transaction = record.createPayment(requestBody, id, status, vendorGeneratedId);

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
	
	public Map<String, Object> sendTransaction(Map<String, Object> requestBody) {
		String vendorName = (String) requestBody.get("vendor_name");

		Config config = ConfigFactory.createConfig(vendorName, ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));

		Gson gson = new Gson();
		Map<String, Object> requestMap = config.getRetailOutletRequestBody(requestBody);
		String id = (String) requestMap.get("id");
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
		} catch (IOException | InterruptedException e) {
			System.out.println(e);
		}

		return responseMap;
	}

	public List<RetailOutletImpl> getByVendorName(Map<String, String> queryParams) {
		String vendorName = (String) queryParams.get("vendor_name");
		record.validateVendorName(vendorName);
		List<RetailOutletImpl> result = new ArrayList<>();
		List<RetailOutletImpl> retailOutletPayments = retailOutletPaymentRepository.getAllObject("retailoutlet_impl");
		for(RetailOutletImpl retailOutletPayment : retailOutletPayments){
			if (retailOutletPayment.getVendorName().equals(vendorName)){
				result.add(retailOutletPayment);
			}
		}
		return result;
	}

	public HashMap<String, Object> getById(Map<String, String> queryParams) {
		String id = (String) queryParams.get("id");
		String validatedId = record.validateId(id);
		List<RetailOutletImpl> retailOutletPayments = retailOutletPaymentRepository.getAllObject("retailoutlet_impl");
		for(RetailOutletImpl retailOutletPayment : retailOutletPayments){
			if (retailOutletPayment.getIdTransaction().toString().equals(validatedId)){
				return retailOutletPayment.toHashMap();
			}
		}
		throw new BadRequestException("Retail outlet payment dengan ID " + validatedId + " tidak ditemukan");
	}
}

