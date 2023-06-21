package paymentgateway.payment;

import java.lang.reflect.*;

import java.math.BigInteger;
import java.util.*;
import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import vmj.routing.route.VMJExchange;

public class PaymentConfiguration {
    public PaymentConfiguration() {
    }

	public String getProductName(){
		return System.getProperty("user.dir").split("\\\\")[5];
	}

	public static String getProductEnv(String productName, String serviceName){
		String url = "";
		try {
			Object prop = getPropertiesReader();

			Method getFlipBaseUrlTest = prop.getClass().getMethod("get"+ productName +"BaseUrl");
			String baseUrl = (String) getFlipBaseUrlTest.invoke(prop);

			Method getApiEndpoint = prop.getClass().getMethod("get" + productName+ serviceName);
			String apiEndpoint = (String) getApiEndpoint.invoke(prop);

			url = baseUrl + apiEndpoint;
			System.out.println("url: " + url);
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
		}
		catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return url;
	}

	public static Object getPropertiesReader(){
		Object prop = null;
		try {
			String propClassName = "paymentgateway.payment.core.util.PropertiesReader";
			Class<?> propClass = Class.forName(propClassName); // convert string classname to class
			prop = propClass.newInstance();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return prop;
	}

	public static HttpRequest.Builder getBuilder(HttpRequest.Builder builder, HashMap<String, String> headerParams){
		for (Map.Entry<String, String> e : headerParams.entrySet()) {
			builder.header(e.getKey(), e.getValue());
		}
		return builder;
	}

	public static HashMap<String, String> getHeaderParams(String productName){
		HashMap<String, String> headerParams = new HashMap<>();
		Object prop = getPropertiesReader();

		try {
			Method method = prop.getClass().getMethod("get" + productName + "HeaderParams");
			headerParams = (HashMap<String, String>) method.invoke(prop);

		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
		}
		catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}


		return headerParams;
	}

	public static HashMap<String, String> getBodyKeyParams(String productName){
		HashMap<String, String> keyParams = new HashMap<>();
		Object prop = getPropertiesReader();

		try {
			Method method = prop.getClass().getMethod("get" + productName + "BodyKey");
			keyParams = (HashMap<String, String>) method.invoke(prop);

		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
		}
		catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}


		return keyParams;
	}


	public static Map<String, String> getOyBankCode(){
		Map<String, String> immutableMap = Map.of("bni", "009",
				"bca", "014",
				"mandiri", "008",
				"bri", "002",
				"permata", "013");
		Map<String, String> bankCodes = new HashMap<>(immutableMap);
		return bankCodes;
	}

	public static Map<String, String> getOyEWalletCode(){
		Map<String, String> immutableMap = Map.of("ovo", "ovo_ewallet",
				"shopeepay", "shopeepay_ewallet",
				"dana", "dana_ewallet");
		Map<String, String> bankCodes = new HashMap<>(immutableMap);
		return bankCodes;
	}

	public static int generateId(){
		String generateUUIDNo = String.format("%010d",new BigInteger(UUID.randomUUID().toString().replace("-",""),16));
		return Integer.parseInt(generateUUIDNo.substring(0,5));
	}

	public static List<Map<String,Object>> toListMap(VMJExchange vmjExchange, String name){
		Gson gson = new Gson();
		Type resultType = new TypeToken<List<Map<String, Object>>>(){}.getType();
		List<Map<String, Object>> result = gson.fromJson(gson.toJson(vmjExchange.getRequestBodyForm(name)), resultType);
		return result;
	}

	public static Map<String, Object>  processRequestMap(VMJExchange vmjExchange, String productName, String serviceName){

		Map<String, Object> result = null;
		try {
			Class c = PaymentConfiguration.class;
			System.out.println(1);
			Method m = c.getMethod("get" + productName + serviceName + "RequestBody", VMJExchange.class);
			System.out.println(2);
			result = (Map<String, Object>) m.invoke(null, vmjExchange);
			System.out.println(3);
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
		}
		catch (InvocationTargetException e){
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Map<String, Object> getMidtransPaymentLinkRequestBody(VMJExchange vmjExchange){
		Map<String, Object> requestMap = new HashMap<>();
		Map<String, Object> customer_details = new HashMap<String, Object>();
		Map<String, Object> transaction_details = new HashMap<String, Object>();

		int id = generateId();
		double amount = Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount"));


		transaction_details.put("order_id", String.valueOf(id));
		transaction_details.put("gross_amount", amount);
		requestMap.put("transaction_details", transaction_details);

		String name = (String) vmjExchange.getRequestBodyForm("sender_name");
		String[] arr = name.split(" ", 2);
		if(arr.length > 1){
			customer_details.put("first_name", arr[0]);
			customer_details.put("last_name", arr[1]);
		} else{
			customer_details.put("first_name", arr[0]);
		}
		requestMap.put("costumer_details", customer_details);
		requestMap.put("id",id);
		System.out.println("Midtrans id:" + String.valueOf(id));
		return requestMap;
	}

	public static Map<String, Object> getMidtransRetailOutletRequestBody(VMJExchange vmjExchange){
		Map<String, Object> requestMap = new HashMap<>();
		Map<String, Object> cstore = new HashMap<String, Object>();
		Map<String, Object> transaction_details = new HashMap<String, Object>();

		int id = generateId();
		double amount = Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount"));
		String store = (String) vmjExchange.getRequestBodyForm("retailOutlet");


		transaction_details.put("order_id", String.valueOf(id));
		transaction_details.put("gross_amount", amount);

		cstore.put("store", store);

		requestMap.put("payment_type", "cstore");
		requestMap.put("transaction_details", transaction_details);
		requestMap.put("cstore", cstore);
		requestMap.put("id",id);
		return requestMap;
	}

	public static Map<String, Object> getMidtransVirtualAccountRequestBody(VMJExchange vmjExchange){
		Map<String, Object> requestMap = new HashMap<>();
		Map<String, Object> bank_transfer = new HashMap<String, Object>();
		Map<String, Object> transaction_details = new HashMap<String, Object>();

		int id = generateId();
		double amount = Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount"));
		String bank = (String) vmjExchange.getRequestBodyForm("bank");


		transaction_details.put("order_id", String.valueOf(id));
		transaction_details.put("gross_amount", amount);

		bank_transfer.put("bank", bank);

		requestMap.put("payment_type", "bank_transfer");
		requestMap.put("transaction_details", transaction_details);
		requestMap.put("bank_transfer", bank_transfer);
		requestMap.put("id",id);
		return requestMap;
	}

	public static Map<String, Object> getMidtransEWalletRequestBody(VMJExchange vmjExchange){
		Map<String, Object> requestMap = new HashMap<>();
		Map<String, Object> customer_details = new HashMap<String, Object>();
		Map<String, Object> transaction_details = new HashMap<String, Object>();

		int id = generateId();
		double amount = Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount"));
		String ewallet = (String) vmjExchange.getRequestBodyForm("ewalletType");
		String phone = (String) vmjExchange.getRequestBodyForm("phone_number");


		transaction_details.put("order_id", String.valueOf(id));
		transaction_details.put("gross_amount", amount);

		customer_details.put("phone", phone);

		requestMap.put("payment_type", ewallet);
		requestMap.put("transaction_details", transaction_details);
		requestMap.put("customer_details", customer_details);
		requestMap.put("id",id);
		return requestMap;
	}

	public static Map<String, Object> getMidtransDebitCardRequestBody(VMJExchange vmjExchange){
		Map<String, Object> requestMap = new HashMap<>();
		Map<String, Object> item_details = new HashMap<>();
		Map<String, Object> transaction_details = new HashMap<String, Object>();

		int id = generateId();
		double amount = Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount"));
		String type = (String) vmjExchange.getRequestBodyForm("payment_type");


		transaction_details.put("order_id", String.valueOf(id));
		transaction_details.put("gross_amount", amount);

		item_details.put("name","test item");
		item_details.put("quantity",1);
		item_details.put("price",amount);
		requestMap.put("item_details",item_details);

		requestMap.put("payment_type", type);
		requestMap.put("transaction_details", transaction_details);
		requestMap.put("id",id);
		return requestMap;
	}

	public static Map<String, Object> getMidtransCreditCardRequestBody(VMJExchange vmjExchange){
		Map<String, Object> requestMap = new HashMap<>();
		Map<String, Object> credit_card = new HashMap<>();
		Map<String, Object> transaction_details = new HashMap<String, Object>();

		int id = generateId();
		double amount = Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount"));
		String token = (String) vmjExchange.getRequestBodyForm("token_id");


		transaction_details.put("order_id", String.valueOf(id));
		transaction_details.put("gross_amount", amount);

		credit_card.put("token_id",token);
		requestMap.put("credit_card",credit_card);

		requestMap.put("payment_type", "credit_card");
		requestMap.put("transaction_details", transaction_details);
		requestMap.put("id",id);
		return requestMap;
	}

	public static Map<String, Object> getOyPaymentLinkRequestBody(VMJExchange vmjExchange){
		Map<String, Object> requestMap = new HashMap<>();

		int id = generateId();
		int amount = (int) (Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount")));
		String name = (String) vmjExchange.getRequestBodyForm("sender_name");

		requestMap.put("partner_tx_id", String.valueOf(id));
		requestMap.put("amount", amount);
		requestMap.put("sender_name", name);
		requestMap.put("id",id);
		System.out.println("Oy id:" + String.valueOf(id));
		return requestMap;
	}

	public static Map<String, Object> getOyRetailOutletRequestBody(VMJExchange vmjExchange){
		Map<String, Object> requestMap = new HashMap<>();


		int id = generateId();
		int amount = (int) (Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount")));
		String store = (String) vmjExchange.getRequestBodyForm("retailOutlet");

		requestMap.put("partner_trx_id", String.valueOf(id));
		requestMap.put("customer_id", String.valueOf(id));
		requestMap.put("amount", amount);
		requestMap.put("transaction_type", "CASH_IN");
//		requestMap.put("offline_channel",store.toUpperCase());
		requestMap.put("offline_channel","CRM");
		requestMap.put("id",id);
		return requestMap;
	}

	public static Map<String, Object> getOyVirtualAccountRequestBody(VMJExchange vmjExchange){
		Map<String, Object> requestMap = new HashMap<>();


		int id = generateId();
		int amount = (int) (Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount")));
		String bank = (String) vmjExchange.getRequestBodyForm("bank");

		requestMap.put("partner_user_id", String.valueOf(id));
		requestMap.put("bank_code", getOyBankCode().get(bank));
		requestMap.put("amount", amount);
		requestMap.put("is_open", false);
		requestMap.put("id",id);
		return requestMap;
	}

	public static Map<String, Object> getOyEWalletRequestBody(VMJExchange vmjExchange){
		Map<String, Object> requestMap = new HashMap<>();


		int id = generateId();
		String uuid = String.format("%010d",new BigInteger(UUID.randomUUID().toString().replace("-",""),8));
		int amount = (int) (Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount")));
		String ewallet = (String) vmjExchange.getRequestBodyForm("ewalletType");
		String phone = (String) vmjExchange.getRequestBodyForm("phone_number");

		requestMap.put("partner_trx_id", String.valueOf(id));
		requestMap.put("customer_id", String.valueOf(id));
		requestMap.put("amount", amount);
		requestMap.put("mobile_numer",phone);
		requestMap.put("ewallet_code", getOyEWalletCode().get(ewallet.toLowerCase()));
		requestMap.put("success_redirect_url","https://myweb.com/usertx/" + uuid);
		requestMap.put("id",id);
		return requestMap;
	}

	public static Map<String, Object> getOyInvoiceRequestBody(VMJExchange vmjExchange){
		Map<String, Object> requestMap = new HashMap<>();

		int id = generateId();
		int amount = (int) (Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount")));

		requestMap.put("partner_tx_id", String.valueOf(id));
		requestMap.put("amount", amount);

		List<Map<String, Object>> items = toListMap(vmjExchange, "invoice_items");
		requestMap.put("invoice_items",items);

		requestMap.put("id",id);
		return requestMap;
	}

	public static Map<String, Object> getOyPaymentRoutingRequestBody(VMJExchange vmjExchange){
		Map<String, Object> requestMap = new HashMap<>();

		int id = generateId();
		int amount = (int) (Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount")));

		requestMap.put("partner_trx_id", String.valueOf(id));
		requestMap.put("partner_user_id", String.valueOf(id));
		requestMap.put("need_frontend", true);

		List<Map<String,Object>> routings = toListMap(vmjExchange,"routings");
		requestMap.put("list_enable_sof", vmjExchange.getRequestBodyForm("list_enable_sof"));
		requestMap.put("list_enable_payment_method",vmjExchange.getRequestBodyForm("list_enable_payment_method"));
		requestMap.put("need_frontend",true);
		requestMap.put("receive_amount",amount);
		requestMap.put("routings",routings);

		requestMap.put("id",id);
		return requestMap;
	}

//	public static Map<String, Object> processRequestMap(VMJExchange vmjExchange, String producName){
//		String generateUUIDNo = String.format("%010d",new BigInteger(UUID.randomUUID().toString().replace("-",""),16));
//		int id = Integer.parseInt(generateUUIDNo.substring(0,5));
//		Map<String, Object> body = vmjExchange.getPayload();
//		Map<String, Object> requestMap = new HashMap<>();
//		if (producName.equals("Midtrans")){
//			System.out.println("b");
////			int amount = ((Double) vmjExchange.getRequestBodyForm("amount")).intValue();
//			double amount = Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount"));
//			Map<String, Object> transaction_details = new HashMap<String, Object>();
//			transaction_details.put("order_id", id);
//			transaction_details.put("gross_amount", amount);
//			requestMap.put("transaction_details", transaction_details);
//			if(vmjExchange.getPayload().containsKey("payment_type")){
//				String type = (String) vmjExchange.getRequestBodyForm("payment_type");
//				requestMap.put("payment_type",type);
//				if(type.equals("bank_transfer")){
//					Map<String, Object> bankTransfer = new HashMap<>();
//					String bank = (String) vmjExchange.getRequestBodyForm("bank");
//					bankTransfer.put("bank",bank);
//					requestMap.put("bank_transfer", bankTransfer);
//				} else if (type.equals("credit_card")) {
//					Map<String, Object> creditCard = new HashMap<>();
//					requestMap.put("credit_card",creditCard);
//				} else if (type.equals("cstore")) {
//					Map<String, Object> storeData = new HashMap<>();
//					String store = (String) vmjExchange.getRequestBodyForm("store");
//					storeData.put("store",store);
//					requestMap.put("cstore",storeData);
//				}else if (type.equals("danamon_online")) {
//					Map<String, Object> itemDetails = new HashMap<>();
//					itemDetails.put("name","test item");
//					itemDetails.put("quantity",1);
//					itemDetails.put("price",amount);
//					requestMap.put("item_details",itemDetails);
//
//					Map<String, Object> customerDetails = new HashMap<>();
//					requestMap.put("customer_details",customerDetails);
//				}else if (type.equals("credit_card")) {
//					Map<String, Object> creditCard = new HashMap<>();
//					creditCard.put("token_id",vmjExchange.getRequestBodyForm("token_id"));
//					creditCard.put("authentication",true);
//					requestMap.put("credit_card", creditCard);
//				}else if(type.equals("gopay")){
//					Map<String, Object> customerDetail= new HashMap<>();
//					String phoneNumber = (String) vmjExchange.getRequestBodyForm("phone_number");
//					customerDetail.put("phone",phoneNumber);
//					requestMap.put("customer_detail",customerDetail);
//				}
//			}
//			requestMap.put("id",id);
//			return requestMap;
//		} else if (producName.equals("Oy")) {
//			System.out.println("c");
//			int amount = (int) (Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount")));
////			double amount = Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount"));
//			requestMap.put("amount",amount);
//			requestMap.put("id",id);
//			requestMap.put("partner_trx_id",String.valueOf(id));
//			requestMap.put("partner_user_id",String.valueOf(id));
//			requestMap.put("partner_tx_id",String.valueOf(id));
//			requestMap.put("customer_id",String.valueOf(id));
//			requestMap.put("success_redirect_url","https://myweb.com/usertx/123456");
//			requestMap.put("transaction_type","CASH_IN");
//			requestMap.put("offline_channel","CRM");
//			if(body.containsKey("sender_name")) {
//				System.out.println("masuk if");
//				String senderName = (String) vmjExchange.getRequestBodyForm("sender_name");
//				requestMap.put("sender_name", senderName);
//			}
//
//			if(body.containsKey("payment_type")){
//				String type = (String) vmjExchange.getRequestBodyForm("payment_type");
//				String phoneNumber = (String) vmjExchange.getRequestBodyForm("phone_number");
//				requestMap.put("mobile_number",phoneNumber);
//				requestMap.put("ewallet_code",type);
//			}
//
//			if(body.containsKey("bank")){
//				requestMap.put("partner_user_id",id);
//				System.out.println("hello" + getOyBankCode().get(body.get("bank")));
//				requestMap.put("bank_code", getOyBankCode().get(body.get("bank")));
//				requestMap.put("is_open", false);
//			}
//
//			if(body.containsKey("routings")){
//				List<Map<String,Object>> routings = toListMap(vmjExchange,"routings");
//				requestMap.put("list_enable_sof", vmjExchange.getRequestBodyForm("list_enable_sof"));
//				requestMap.put("list_enable_payment_method",vmjExchange.getRequestBodyForm("list_enable_payment_method"));
//				requestMap.put("need_frontend",true);
//				requestMap.put("receive_amount",amount);
//				requestMap.put("routings",routings);
//			}
//
//			if(body.containsKey("invoice_items")) {
//				List<Map<String, Object>> items = toListMap(vmjExchange, "invoice_items");
//				requestMap.put("invoice_items",items);
//			}
//			return requestMap;
//		}
//		return vmjExchange.getPayload();
//	}

	
}	