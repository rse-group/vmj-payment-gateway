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

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import paymentgateway.payment.PaymentFactory;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentResourceDecorator;
import paymentgateway.payment.core.PaymentImpl;
import paymentgateway.payment.core.PaymentResourceComponent;
import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

import com.midtrans.Midtrans;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;

public class PaymentResourceImpl extends PaymentResourceDecorator {
	
	protected String apiKey;
	protected String apiEndpoint;
    public PaymentResourceImpl (PaymentResourceComponent record) {
        super(record);
    }

	public Payment createPayment(VMJExchange vmjExchange) {
		Map<String, Object> response = sendTransaction(vmjExchange);

		int id = (int) response.get("id");
		String transactionToken = (String) response.get("transaction_token");

		Payment transaction = record.createPayment(vmjExchange, id);
		Payment invoiceTransaction = PaymentFactory.createPayment(
				"paymentgateway.payment.invoice.PaymentImpl", transaction, transactionToken);
		PaymentRepository.saveObject(invoiceTransaction);
		return invoiceTransaction;
	}
	
	protected Map<String, Object> sendTransaction(VMJExchange vmjExchange) {
		Gson gson = new Gson();

		Config config = ConfigFactory.createConfig(ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));

		Map<String, Object> requestMap = config.getInvoiceRequestBody(vmjExchange);
		int id = ((Integer) requestMap.get("id")).intValue();
		requestMap.remove("id");
		String requestString = gson.toJson(requestMap);
		String configUrl = config.getProductEnv("Invoice");
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
			System.out.println("rawResponse " + rawResponse);
			responseMap = config.getInvoiceResponse(rawResponse, id);
		} catch (Exception e) {
			System.out.println(e);
		}

		return responseMap;
	}
	
	@Route(url="call/invoice")
	public HashMap<String,Object> testInvoice(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")){
			Payment result = this.createPayment(vmjExchange);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
	}
}

