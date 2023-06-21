package paymentgateway.disbursement.special;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import java.nio.charset.StandardCharsets;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import java.text.SimpleDateFormat;

import paymentgateway.disbursement.DisbursementFactory;
import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementResourceDecorator;
import paymentgateway.disbursement.core.DisbursementImpl;
import paymentgateway.disbursement.core.DisbursementResourceComponent;
import paymentgateway.disbursement.core.MoneyTransferResponse;
import paymentgateway.disbursement.core.Sender;


public class SpecialResourceImpl extends DisbursementResourceDecorator {

	public SpecialResourceImpl(DisbursementResourceComponent record) {
		super(record);
	}

	protected MoneyTransferResponse sendTransaction(VMJExchange vmjExchange) {

		Gson gson = new Gson();
		Map<String, Object> requestMap = new HashMap<String, Object>();

		HttpClient client = HttpClient.newHttpClient();

		HttpRequest request = HttpRequest.newBuilder()
				.header("Content-Type", "application/x-www-form-urlencoded")
				.header("idempotency-key", UUID.randomUUID().toString())
				.header("X-TIMESTAMP","")
				.header("Authorization",
						"Basic SkRKNUpERXpKR3A0YlU5WVppNU9kRGRuU0VoU2JYbFBibXhEVVM1VVJGaHRTM0pEZFZwc2NWVTFMemgxUldwSVVqVldielpMYkhOMkwybDE=")
				.header("Cookie", "_csrf=I_hH_U80Wpc07Yx-pV_HBDI4KO64F3ES")
				.uri(URI.create("https://bigflip.id/big_sandbox_api/v2/special-disbursement"))
				.POST(HttpRequest.BodyPublishers.ofString(getParamsUrlEncoded(vmjExchange)))
				.build();

		MoneyTransferResponse responseObj = null;

		try {
			 HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			 String rawResponse = response.body().toString();
			 responseObj = gson.fromJson(rawResponse,
			 MoneyTransferResponse.class);
		} catch (Exception e) {
			System.out.println(e);
		}

		return responseObj;
	}


//	private static String getParamsUrlEncoded(VMJExchange vmjExchange) {
//		ArrayList<String> paramList = new ArrayList<>();
//		for (Map.Entry<String, Object> entry : vmjExchange.getPayload().entrySet()) {
//			String key = entry.getKey();
//			Object val = entry.getValue();
//			if (val instanceof String) {
//				System.out.println(val.toString() + ": this is string");
//				paramList.add(key + "=" + URLEncoder.encode(val.toString(), StandardCharsets.UTF_8));
//			} else if (val instanceof Integer) {
//				System.out.println(val.toString() + ": this is int");
//				paramList.add(key + "=" + URLEncoder.encode(val.toString(), StandardCharsets.UTF_8));
//			} else if (val instanceof Double) {
//				System.out.println(val.toString() + ": this is int Double");
//				int temp = ((Double) val).intValue();
//				paramList.add(key + "=" + URLEncoder.encode(Integer.toString(temp), StandardCharsets.UTF_8));
//			}
//
//		}
//		String encodedURL = String.join("&",paramList);
//		return encodedURL;
//	}

}
