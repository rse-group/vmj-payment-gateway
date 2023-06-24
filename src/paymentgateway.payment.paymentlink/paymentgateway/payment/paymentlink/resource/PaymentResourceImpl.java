package paymentgateway.payment.paymentlink;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;


import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import paymentgateway.payment.PaymentFactory;
import paymentgateway.payment.PaymentResourceFactory;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentResourceDecorator;
import vmj.hibernate.integrator.RepositoryUtil;
import paymentgateway.payment.PaymentConfiguration;
import paymentgateway.payment.core.PaymentResourceComponent;
import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

//import paymentgateway.config.core.ConfigImpl;
public class PaymentResourceImpl extends PaymentResourceDecorator {
	RepositoryUtil<PaymentLinkImpl> paymentLinkRepository;

	public PaymentResourceImpl(PaymentResourceComponent record) {
		super(record);
		this.paymentLinkRepository = new RepositoryUtil<PaymentLinkImpl>(paymentgateway.payment.paymentlink.PaymentLinkImpl.class);
	}

	public Payment createPayment(VMJExchange vmjExchange, String productName , String serviceName) {
		PaymentLinkResponse response = sendTransaction(vmjExchange, productName, serviceName);
		String paymentLink = response.getUrl();
		int id = response.getId();
		Payment transaction = record.createPayment(vmjExchange, id, productName);
		Payment paymentLinkTransaction =
			PaymentFactory.createPayment("paymentgateway.payment.paymentlink.PaymentLinkImpl",
			transaction,id, paymentLink);
		PaymentRepository.saveObject(paymentLinkTransaction);
		return paymentLinkTransaction;
	}

	protected PaymentLinkResponse sendTransaction(VMJExchange vmjExchange, String productName, String serviceName) {
		PaymentLinkResponse responseObj = null;

		Config config = ConfigFactory
				.createConfig(
						"paymentgateway.config." + productName.toLowerCase() + "." + productName + "Configuration"
						,
						ConfigFactory.createConfig(
								"paymentgateway.config.core.ConfigImpl"));

		Gson gson = new Gson();
		Map<String, Object> requestMap = config.processRequestMap(vmjExchange,productName,serviceName);
		System.out.println("sebelum id");
		int id = ((Integer) requestMap.get("id")).intValue();
		requestMap.remove("id");
		String requestString = gson.toJson(requestMap);
		String configUrl = config.getProductEnv(productName, serviceName);
		HashMap<String, String> headerParams = config.getHeaderParams(productName);
		System.out.println("configUrl: " + configUrl);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = (PaymentConfiguration.getBuilder(HttpRequest.newBuilder(),headerParams))
				.uri(URI.create(configUrl))
				.POST(HttpRequest.BodyPublishers.ofString(requestString))
				.build();

		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			System.out.println("rawResponse" + rawResponse);
			responseObj = gson.fromJson(rawResponse, PaymentLinkResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		responseObj.setId(id);

		return responseObj;
	}


	@Route(url = "call/paymentlink")
	public HashMap<String, Object> paymentLink(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS"))
			return null;

		System.out.println("masuk call");
		String productName = (String) vmjExchange.getRequestBodyForm("product_name");
		Payment result = this.createPayment(vmjExchange, productName,"PaymentLink");
		return result.toHashMap();
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
