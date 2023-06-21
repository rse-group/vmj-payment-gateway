package paymentgateway.payment.invoice;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import paymentgateway.payment.PaymentConfiguration;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import paymentgateway.payment.PaymentFactory;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentResourceDecorator;
import paymentgateway.payment.core.PaymentImpl;
import paymentgateway.payment.core.PaymentResourceComponent;

import com.midtrans.Midtrans;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;

public class PaymentResourceImpl extends PaymentResourceDecorator {
	
	protected String apiKey;
	protected String apiEndpoint;
    public PaymentResourceImpl (PaymentResourceComponent record) {
        super(record);
        this.apiKey = "SB-Mid-server-NVYFqUidEQUTaozWjW77fFWW";
		this.apiEndpoint = "https://api.sandbox.midtrans.com/v2/charge";
    }

	public Payment createPayment(VMJExchange vmjExchange, String productName, String serviceName) {
		InvoiceResponse response = sendTransaction(vmjExchange, productName, serviceName);
		int id = response.getId();
		String transactionToken = response.getPayment_link_id();
		Payment transaction = record.createPayment(vmjExchange, id, productName);
		Payment invoiceTransaction = PaymentFactory.createPayment(
				"paymentgateway.payment.invoice.PaymentImpl", transaction, transactionToken);
		PaymentRepository.saveObject(invoiceTransaction);
		return invoiceTransaction;
	}
	
	protected InvoiceResponse sendTransaction(VMJExchange vmjExchange, String productName, String serviceName) {
//		String idTransaction = (String) vmjExchange.get("idTransaction");
//		int amount = (int) vmjExchange.get("amount");
//
//		Gson gson = new Gson();
//		Map<String,Object> transactionDetails = new HashMap<String,Object>();
//		transactionDetails.put("order_id", idTransaction);
//		transactionDetails.put("gross_amount", amount);
//		Map<String,Object> params = new HashMap<String,Object>();
//		params.put("transaction_details", transactionDetails);
//
//		String token = "";
//		Midtrans.serverKey = apiKey;
//		Midtrans.isProduction = false;

//		try {
//			String apiCallResult = SnapApi.createTransactionToken(params);
//			token = token + apiCallResult;
//		} catch (Exception e) {
//			System.out.println(e);
//		}
		Gson gson = new Gson();
		Map<String, Object> requestMap = PaymentConfiguration.processRequestMap(vmjExchange,productName,serviceName);
		int id = ((Integer) requestMap.get("id")).intValue();
		requestMap.remove("id");
		String requestString = gson.toJson(requestMap);
		String configUrl = PaymentConfiguration.getProductEnv(productName, serviceName);
		HashMap<String, String> headerParams = PaymentConfiguration.getHeaderParams(productName);
		System.out.println("configUrl: " + configUrl);
		System.out.println(configUrl);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = (PaymentConfiguration.getBuilder(HttpRequest.newBuilder(),headerParams))
				.uri(URI.create(configUrl))
				.POST(HttpRequest.BodyPublishers.ofString(requestString))
				.build();

		InvoiceResponse responseObj = null;

		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String rawResponse = response.body().toString();
			System.out.println(rawResponse);
			responseObj = gson.fromJson(rawResponse, InvoiceResponse.class);
		} catch (Exception e) {
			System.out.println(e);
		}

		responseObj.setId(id);
		return responseObj;
	}
	
	@Route(url="call/invoice")
	public HashMap<String,Object> testInvoice(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) return null;
		
//		int amount = ((Double) vmjExchange.getRequestBodyForm("amount")).intValue();
//		String idTransaction = (String) vmjExchange.getRequestBodyForm("idTransaction");
//
//		HashMap<String,Object> testExchange = new HashMap<>();
//		testExchange.put("idTransaction", idTransaction);
//		testExchange.put("amount", amount);
//		Payment result = this.createPayment(testExchange);
		String productName = (String) vmjExchange.getRequestBodyForm("product_name");
		Payment result = this.createPayment(vmjExchange, productName,"Invoice");
		return result.toHashMap();
	}
}

