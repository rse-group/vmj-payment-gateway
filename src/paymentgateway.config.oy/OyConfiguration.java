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
    public Map<String, Object> getPaymentStatusResponse(String rawResponse, String id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        Map<String, Object> paymentData = (Map<String, Object>) rawResponseMap.get("data");

        String name = (String) paymentData.get("sender_name");
        int amount = (int) ((Double) paymentData.get("amount")).doubleValue();
        String status = (String) paymentData.get("status");
        String paymentMethod = (String) paymentData.get("payment_method");

        response.put("name", name);
        response.put("amount", amount);
        response.put("status", status);
        response.put("payment_method", paymentMethod);
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
        String name = (String) vmjExchange.getRequestBodyForm("sender_name");
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

    @Override
    public Map<String, Object> getInvoiceRequestBody(VMJExchange vmjExchange){
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

    @Override
    public Map<String, Object> getPaymentRoutingRequestBody(VMJExchange vmjExchange){
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
        String transactionToken = (String) rawResponseMap.get("payment_link_id");
        response.put("transaction_token", transactionToken);
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