package paymentgateway.config.oy;

import paymentgateway.config.core.ConfigDecorator;
import paymentgateway.config.core.ConfigComponent;
import paymentgateway.config.core.PropertiesReader;

import java.util.*;
import java.lang.reflect.*;

import vmj.routing.route.VMJExchange;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class OyConfiguration extends ConfigDecorator{
    private String CONFIG_FILE = "oy.properties";

    public OyConfiguration(ConfigComponent record) {
        super(record);
    }

    @Override
    public String getVendorName(){
        return "Oy";
    }

    @Override
    public String getProductEnv(String serviceName){
        return record.getProductEnv(CONFIG_FILE, serviceName);
    }

    @Override
    public String getPaymentDetailEndpoint(String configUrl,String Id){
        configUrl = configUrl.replace("[id]", Id);
        return configUrl;
    }
     
    @Override
    public Map<String, Object> getCallbackPaymentRequestBody(VMJExchange vmjExchange){
	    Map<String, Object> requestMap = new HashMap<>();
	    Map<String, Object> payload = vmjExchange.getPayload();
	    String id = "";
	    String status = "";
		
	    if (payload.get("success") != null) {
	        boolean successStatus = ((boolean) payload.get("success"));
	        if (payload.get("partner_trx_id") != null) {
	        	id = (String) payload.get("partner_trx_id");
	        }
	        else if (payload.get("customer_id") != null) {
	        	id = (String) payload.get("customer_id");
	        }
	        else if (payload.get("partner_user_id")!=null) {
	        	id = (String) payload.get("partner_user_id");
	        }
	        status = successStatus ? PaymentStatus.SUCCESSFUL.getStatus() : PaymentStatus.FAILED.getStatus();
	    } else {
			id = (String) payload.get("partner_tx_id");
			status = (String) payload.get("status");
		}

		if(status.equals(PaymentStatus.COMPLETE.getStatus())){
			status = PaymentStatus.SUCCESSFUL.getStatus();
		}
		else if (status.equals(PaymentStatus.CLOSED.getStatus())){
			status = PaymentStatus.CANCELLED.getStatus();
		}
        else if (status.equals(PaymentStatus.FAIL.getStatus())){
            status = PaymentStatus.FAILED.getStatus();
        }

        requestMap.put("id",id);
        requestMap.put("status", status);
        return requestMap;
    }
     
    @Override
    public Map<String, Object> getPaymentStatusResponse(String rawResponse, String id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        Map<String, Object> paymentData = (Map<String, Object>) rawResponseMap.get("data");
        String status = (String) paymentData.get("status");

        response.put("status", status);
        response.put("id", id);
        return response;
    }

    public Map<String, String> getOyBankCode(){
        Map<String, String> immutableMap = Map.of("bni", "009",
                "bca", "014",
                "mandiri", "008",
                "bri", "002",
                "permata", "013");
        Map<String, String> bankCodes = new HashMap<>(immutableMap);
        return bankCodes;
    }

    public Map<String, String> getOyEWalletCode(){
        Map<String, String> immutableMap = Map.of("ovo", "ovo_ewallet",
                "shopeepay", "shopeepay_ewallet",
                "dana", "dana_ewallet");
        Map<String, String> bankCodes = new HashMap<>(immutableMap);
        return bankCodes;
    }

    @Override
    public Map<String, Object> getPaymentLinkRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();

        int id = generateId();
        int amount = (int) (Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount")));
        String name = (String) vmjExchange.getRequestBodyForm("name");
        String email = (String) vmjExchange.getRequestBodyForm("email");
        String description = (String) vmjExchange.getRequestBodyForm("title");

        requestMap.put("partner_tx_id", String.valueOf(id));
        requestMap.put("amount", amount);
        requestMap.put("sender_name", name);
        requestMap.put("email", email);
        requestMap.put("description",description);
        requestMap.put("id",id);
        System.out.println("Oy id:" + String.valueOf(id));
        return requestMap;
    }

    @Override
    public Map<String, Object> getRetailOutletRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();


        int id = generateId();
        int amount = (int) (Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount")));
        String store = (String) vmjExchange.getRequestBodyForm("retail_outlet");

        requestMap.put("partner_trx_id", String.valueOf(id));
        requestMap.put("customer_id", String.valueOf(id));
        requestMap.put("amount", amount);
        requestMap.put("transaction_type", "CASH_IN");
//		requestMap.put("offline_channel",store.toUpperCase());
        requestMap.put("offline_channel","CRM");
        requestMap.put("id",id);
        return requestMap;
    }

    @Override
    public Map<String, Object> getVirtualAccountRequestBody(VMJExchange vmjExchange){
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

    @Override
    public Map<String, Object> getEWalletRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();


        int id = generateId();
        String uuid = UUID.randomUUID().toString();
        int amount = (int) (Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount")));
        String ewallet = (String) vmjExchange.getRequestBodyForm("ewallet_type");
        String phone = (String) vmjExchange.getRequestBodyForm("phone");

        requestMap.put("partner_trx_id", String.valueOf(id));
        requestMap.put("customer_id", String.valueOf(id));
        requestMap.put("amount", amount);
        requestMap.put("mobile_number",phone);
        requestMap.put("ewallet_code", getOyEWalletCode().get(ewallet.toLowerCase()));
        requestMap.put("success_redirect_url","https://myweb.com/usertx/" + uuid);
        requestMap.put("id",id);
        return requestMap;
    }

    @Override
    public Map<String, Object> getInvoiceRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();
        int id = generateId();
        int amount = (int) (Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount")));  
        int quantity = (int) (Double.parseDouble((String) vmjExchange.getRequestBodyForm("quantity")));
        int pricePerItem = (int) (Double.parseDouble((String) vmjExchange.getRequestBodyForm("price_per_item")));
      
        Map<String, Object> invoiceMap = new HashMap<>();
        
        invoiceMap.put("quantity", quantity);
        invoiceMap.put("price_per_item", pricePerItem);
        
        List<Map<String, Object>> invoicesItems = new ArrayList<>();
        invoicesItems.add(invoiceMap);
        
        requestMap.put("partner_tx_id", String.valueOf(id));
        requestMap.put("amount", amount);
        requestMap.put("invoice_items",invoicesItems);

        requestMap.put("id",id);
        return requestMap;
    }
//{
// "amount" : "100000",
// "list_enable_payment_method": "VA",
// "list_enable_sof" : "002",
// "vendor_name": "Oy",
// "routings": [
//     {
//     "recipient_account": "123",
//     "recipient_bank": "e14",
//     "recipient_amount": "50000",
//     "recipient_email" : "budi.arti@gmail.com",
//     "recipient_note" : "donasi"
//     },
//     {
//     "recipient_account": "456",
//     "recipient_bank": "014",
//     "recipient_amount": "50000",
//     "recipient_email" : "ani.wati@gmail.com",
//     "recipient_note": "donasi oy"
//     }
//     ]
//}
    
    @Override
    public Map<String, Object> getPaymentRoutingRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();

        int id = generateId();
        int amount = (int) (Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount")));
        
        String recipientAccount = (String) vmjExchange.getRequestBodyForm("recipient_account");
        String recipientBank = (String) vmjExchange.getRequestBodyForm("recipient_bank");
        String recipientAmount = (String) vmjExchange.getRequestBodyForm("recipient_amount");
        String recipientEmail = (String) vmjExchange.getRequestBodyForm("recipient_email");
        String recipientNote = (String) vmjExchange.getRequestBodyForm("recipient_note");

        requestMap.put("partner_trx_id", String.valueOf(id));
        requestMap.put("partner_user_id", String.valueOf(id));
        requestMap.put("need_frontend", true);
        
        Map<String, Object> routingMap = new HashMap<>();
        
        routingMap.put("recipient_account", recipientAccount);
        routingMap.put("recipient_bank", recipientBank);
        routingMap.put("recipient_amount", recipientAmount);
        routingMap.put("recipient_email", recipientEmail);
        routingMap.put("recipient_note", recipientNote);
        
        List<Map<String, Object>> routings = new ArrayList<>();
        routings.add(routingMap);
        
        requestMap.put("list_enable_sof", "002");
        requestMap.put("list_enable_payment_method", "VA");
        requestMap.put("need_frontend",true);
        requestMap.put("receive_amount",amount);
        requestMap.put("routings",routings);

        requestMap.put("id",id);
        return requestMap;
    }

    @Override
    public Map<String, Object> getPaymentLinkResponse(String rawResponse, int id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        String url = (String) rawResponseMap.get("url");
        response.put("url", url);
        response.put("id", id);
        return response;
    }

    @Override
    public Map<String, Object> getInvoiceResponse(String rawResponse, int id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        String transactionUrl = (String) rawResponseMap.get("url");
        response.put("url", transactionUrl);
        response.put("id", id);
        return response;
    }

    @Override
    public Map<String, Object> getPaymentRoutingResponse(String rawResponse, int id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        Map<String, Object> paymentMap = (Map<String, Object>) rawResponseMap.get("payment_info");
        String url = (String) paymentMap.get("payment_checkout_url");
        response.put("payment_checkout_url", url);
        response.put("id", id);
        return response;
    }

    @Override
    public Map<String, Object> getRetailOutletResponse(String rawResponse, int id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        String retailPaymentCode = (String) rawResponseMap.get("code");
        response.put("retail_payment_code", retailPaymentCode);
        response.put("id", id);
        return response;
    }

    @Override
    public Map<String, Object> getEWalletResponse(String rawResponse, int id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        String paymentType = (String) rawResponseMap.get("ewallet_code");
        String url = (String) rawResponseMap.get("ewallet_url");
        response.put("payment_type", paymentType);
        response.put("url", url);
        response.put("id", id);
        return response;
    }

    @Override
    public Map<String, Object> getVirtualAccountResponse(String rawResponse, int id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        String vaNumber = (String) rawResponseMap.get("va_number");
        response.put("va_number", vaNumber);
        response.put("id", id);
        return response;
    }

    @Override
    public HashMap<String, String> getHeaderParams() {
        HashMap<String, String> headerParams = new HashMap<>();
        String contentType = PropertiesReader.getProp(CONFIG_FILE, "content_type");
        String username = PropertiesReader.getProp(CONFIG_FILE, "api_username");
        String apikey = PropertiesReader.getProp(CONFIG_FILE, "authorization");
        headerParams.put("x-oy-username",username);
        headerParams.put("content-type",contentType);
        headerParams.put("x-api-key", apikey);
        return headerParams;
    }
}