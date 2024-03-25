package paymentgateway.payment.paymentlink;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import paymentgateway.payment.PaymentFactory;
import paymentgateway.payment.PaymentResourceFactory;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentResourceDecorator;
import vmj.hibernate.integrator.RepositoryUtil;
import paymentgateway.payment.core.PaymentResourceComponent;

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

public class PaymentResourceImpl extends PaymentResourceDecorator {
	RepositoryUtil<PaymentLinkImpl> paymentLinkRepository;

	public PaymentResourceImpl(PaymentResourceComponent record) {
		super(record);
		this.paymentLinkRepository = new RepositoryUtil<PaymentLinkImpl>(paymentgateway.payment.paymentlink.PaymentLinkImpl.class);
	}

	public Payment createPayment(VMJExchange vmjExchange) {
		Map<String, Object> response = sendTransaction(vmjExchange);
		String paymentLink = (String) response.get("url");
		int id = (int) response.get("id");
		Payment transaction = record.createPayment(vmjExchange, id);
		Payment paymentLinkTransaction =
			PaymentFactory.createPayment("paymentgateway.payment.paymentlink.PaymentLinkImpl",
			transaction,id, paymentLink);
		PaymentRepository.saveObject(paymentLinkTransaction);
		return paymentLinkTransaction;
	}

	protected Map<String, Object> sendTransaction(VMJExchange vmjExchange) {
		Config config = ConfigFactory.createConfig(ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));

		Gson gson = new Gson();
		Map<String, Object> requestMap = config.getPaymentLinkRequestBody(vmjExchange);
		int id = ((Integer) requestMap.get("id")).intValue();
		requestMap.remove("id");
		String requestString = gson.toJson(requestMap);
		String configUrl = config.getProductEnv("PaymentLink");
		HashMap<String, String> headerParams = config.getHeaderParams();
		System.out.println("configUrl: " + configUrl);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = (config.getBuilder(HttpRequest.newBuilder(),headerParams))
				.uri(URI.create(configUrl))
				.POST(HttpRequest.BodyPublishers.ofString(getParamsUrlEncoded(body)))
				.build();

		Map<String, Object> responseMap = new HashMap<>();

		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			System.out.println("rawResponse " + rawResponse);
			responseMap = config.getPaymentLinkResponse(rawResponse, id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseMap;
	}


	@Route(url = "call/paymentlink")
	public HashMap<String, Object> paymentLink(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")){
			Payment result = this.createPayment(vmjExchange);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
	}


	@Route(url = "call/paymentlink/productname")
	public List<PaymentLinkImpl> getByProductName(VMJExchange vmjExchange) {
		String productName = (String) vmjExchange.getRequestBodyForm("product_name");
		List<PaymentLinkImpl> result = new ArrayList<>();
		List<PaymentLinkImpl> paymentLink = paymentLinkRepository.getAllObject("paymentlink_impl");
		for(PaymentLinkImpl payment : paymentLink){
			if (payment.getProductName().equals(productName)){
				result.add(payment);
			}
		}
		return result;
	}

	@Route(url = "call/paymentlink/detail")
	public HashMap<String, Object> getById(VMJExchange vmjExchange) {
		int id = ((Double) vmjExchange.getRequestBodyForm("id")).intValue();
		List<PaymentLinkImpl> paymentLink = paymentLinkRepository.getAllObject("paymentlink_impl");
		for(PaymentLinkImpl payment : paymentLink){
			if (payment.getIdTransaction() == id){
				return payment.toHashMap();
			}
		}
		return null;
	}


	@Route(url = "call/paymentlink/delete")
	public String deletePaymentLinkById(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS"))
			return null;

		int id = ((Double) vmjExchange.getRequestBodyForm("id")).intValue();
//		String id = (String) vmjExchange.getRequestBodyForm("id");
		List<PaymentLinkImpl> paymentLinks = paymentLinkRepository.getAllObject("paymentlink_impl");
		for(PaymentLinkImpl payment : paymentLinks){
			if(payment.getIdTransaction() == id){
				HashMap<String, Object> paymentMap = payment.toHashMap();
				int intId = ((Integer) paymentMap.get("idTransaction")).intValue();
				System.out.println(intId);
				paymentLinkRepository.deleteObject(intId);
//				System.out.println("idTransaction: " + payment.getIdTransaction());
//				System.out.println(payment.getIdTransaction() == nul);
				return "SUCCESS";
			}
		}

		return "There is no paymentlink with id: " + id;
	}

	@Route(url = "call/paymentlink/deleted")
	public void deletePaymentLinkByIdTransaction(VMJExchange vmjExchange) {
		int id = ((Double) vmjExchange.getRequestBodyForm("id")).intValue();
//		PaymentRepository.deleteObject(id);
		record.deletePayment(vmjExchange);

	}
}
