package paymentgateway.disbursement.approval;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import java.nio.charset.StandardCharsets;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import paymentgateway.disbursement.DisbursementFactory;
import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementResourceDecorator;
import paymentgateway.disbursement.core.DisbursementImpl;
import paymentgateway.disbursement.core.DisbursementResourceComponent;

public class ApprovalResourceImpl extends DisbursementResourceDecorator {

	public ApprovalResourceImpl(DisbursementResourceComponent record) {
		super(record);
	}

	public Disbursement createDisbursement(VMJExchange vmjExchange) {
		System.out.println("before");
		MoneyTransferResponse response = sendTransaction(vmjExchange);
		String status = response.getStatus();
		String userId = response.getUser_id();
		String approvalID = (String) vmjExchange.getRequestBodyForm("approvalID");
		Disbursement transaction = record.createDisbursement(vmjExchange,userId);
		System.out.println("11a");
		Disbursement moneyTransferTransaction = DisbursementFactory.createDisbursement(
				"paymentgateway.disbursement.moneytransfer.MoneyTransferImpl",
				transaction, status);
		System.out.println("11b");
		Disbursement approvalTransaction = DisbursementFactory.createDisbursement(
				"paymentgateway.disbursement.approval.ApprovalImpl",
				moneyTransferTransaction, approvalID);
		System.out.println("22");
		return approvalTransaction;
	}

	protected MoneyTransferResponse sendTransaction(VMJExchange vmjExchange) {
//		String id = (String) vmjExchange.get("id");
//		String user_id = (String) vmjExchange.get("user_id");
		int amount = ((Double) vmjExchange.getRequestBodyForm("amount")).intValue();
		String bank_code = (String) vmjExchange.getRequestBodyForm("bank_code");
		String account_number = (String) vmjExchange.getRequestBodyForm("account_number");

		Gson gson = new Gson();
		Map<String, Object> requestMap = new HashMap<String, Object>();

		// midtrans
		// transaction_details.put("order_id", idTransaction);
		// transaction_details.put("gross_amount", amount);
		// Map<String, Object> requestMap = new HashMap<String, Object>();
		// requestMap.put("transaction_details", transaction_details);

		// String requestString = gson.toJson(requestMap);
		// System.out.println("this is request String: " + requestString);
		// HttpClient client = HttpClient.newHttpClient();
		// HttpRequest request = HttpRequest.newBuilder()
		// .header("Authorization", getBasicAuthenticationHeader(apiKey, ""))
		// .header("Content-Type", "application/json")
		// .header("Accept", "application/json")
		// .uri(URI.create(apiEndpoint))
		// .POST(HttpRequest.BodyPublishers.ofString(requestString))
		// .build();

		HttpClient client = HttpClient.newHttpClient();
		String body = "account_number=" + account_number + "&";
		body += "bank_code=" + bank_code + "&";
		body += "amount=" + String.valueOf(amount) + "&";
		HttpRequest request = HttpRequest.newBuilder()
				.header("Content-Type", "application/x-www-form-urlencoded")
				.header("idempotency-key", "8anU9saqIU798wOo")
				.header("X-TIMESTAMP", "")
				.header("Authorization",
						"Basic SkRKNUpERXpKR3A0YlU5WVppNU9kRGRuU0VoU2JYbFBibXhEVVM1VVJGaHRTM0pEZFZwc2NWVTFMemgxUldwSVVqVldielpMYkhOMkwybDE=")
				.header("Cookie", "_csrf=I_hH_U80Wpc07Yx-pV_HBDI4KO64F3ES")
				.uri(URI.create("https://bigflip.id/big_sandbox_api/v2/disbursement"))
				.POST(HttpRequest.BodyPublishers.ofString(getParamsUrlEncoded(vmjExchange)))
				.build();

		String status = "";
		MoneyTransferResponse responseObj = null;

		try {
			 HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			 String rawResponse = response.body().toString();
			 System.out.println("rawResponse: " + rawResponse);
			 responseObj = gson.fromJson(rawResponse,
			 MoneyTransferResponse.class);
			 status += responseObj.getStatus();
			 System.out.println("this is status: " + status);
		} catch (Exception e) {
			System.out.println(e);
		}

		return responseObj;
	}

	private static final String getBasicAuthenticationHeader(String username, String password) {
		String valueToEncode = username + ":" + password;
		return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
	}

	private static String getParamsUrlEncoded(VMJExchange vmjExchange) {
		ArrayList<String> paramList = new ArrayList<>();
		for (Map.Entry<String, Object> entry : vmjExchange.getPayload().entrySet()) {
			String key = entry.getKey();
			Object val = entry.getValue();
			if (val instanceof String) {
				System.out.println(val.toString() + ": this is string");
				paramList.add(key + "=" + URLEncoder.encode(val.toString(), StandardCharsets.UTF_8));
			} else if (val instanceof Integer) {
				System.out.println(val.toString() + ": this is int");
				paramList.add(key + "=" + URLEncoder.encode(val.toString(), StandardCharsets.UTF_8));
			} else if (val instanceof Double) {
				System.out.println(val.toString() + ": this is int Double");
				int temp = ((Double) val).intValue();
				paramList.add(key + "=" + URLEncoder.encode(Integer.toString(temp), StandardCharsets.UTF_8));
			}

		}
		String encodedURL = String.join("&",paramList);
//		String encodedURL = vmjExchange.entrySet()
//				.stream()
//				.map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue().toString(), StandardCharsets.UTF_8))
//				.collect(Collectors.joining("&"));
		System.out.println("encodedURL: " + encodedURL);
		return encodedURL;
	}



	@Route(url = "test/call/approvalTransfer")
	public HashMap<String, Object> moneyTransfer(VMJExchange vmjExchange) {
		 if (vmjExchange.getHttpMethod().equals("OPTIONS"))
		 return null;

		Disbursement result = this.createDisbursement(vmjExchange);
		return result.toHashMap();
	}
}
