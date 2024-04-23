package paymentgateway.config.midtrans;

import paymentgateway.config.core.ConfigDecorator;
import paymentgateway.config.core.ConfigComponent;
import paymentgateway.config.core.PropertiesReader;

import java.util.*;
import java.lang.reflect.Type;

import vmj.routing.route.VMJExchange;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MidtransConfiguration extends ConfigDecorator{
    private String CONFIG_FILE = "midtrans.properties";

    public MidtransConfiguration(ConfigComponent record) {
        super(record);
    }

    @Override
    public String getVendorName(){
        return "Midtrans";
    }

    @Override
    public Map<String, Object> getPaymentLinkRequestBody(VMJExchange vmjExchange){
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

    @Override
    public Map<String, Object> getRetailOutletRequestBody(VMJExchange vmjExchange){
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

    @Override
    public Map<String, Object> getVirtualAccountRequestBody(VMJExchange vmjExchange){
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

    @Override
    public Map<String, Object> getEWalletRequestBody(VMJExchange vmjExchange){
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

    @Override
    public Map<String, Object> getDebitCardRequestBody(VMJExchange vmjExchange){
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

    @Override
    public Map<String, Object> getCreditCardRequestBody(VMJExchange vmjExchange){
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

    public Map<String, Object> getMidtransPayoutRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();

        int id = generateId();
        double amount = Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount"));
        return requestMap;
    }
    
    @Override
    public Map<String, Object> getPaymentLinkResponse(String rawResponse, int id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        String url = (String) rawResponseMap.get("payment_url");
        response.put("url", url);
        response.put("id", id);
        return response;
    }

    @Override
    public Map<String, Object> getDebitCardResponse(String rawResponse, int id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        String url = (String) rawResponseMap.get("redirect_url");
        String paymentType = (String) rawResponseMap.get("payment_type");
        response.put("payment_type", paymentType);
        response.put("redirect_url", url);
        response.put("id", id);
        return response;
    }

    @Override
    public Map<String, Object> getCreditCardResponse(String rawResponse, int id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        String url = (String) rawResponseMap.get("redirect_url");
        response.put("redirect_url", url);
        response.put("id", id);
        return response;
    }

    @Override
    public Map<String, Object> getRetailOutletResponse(String rawResponse, int id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        String retailPaymentCode = (String) rawResponseMap.get("payment_code");
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
        String paymentType = (String) rawResponseMap.get("payment_type");
        List<Map<String, Object>> actions = (List<Map<String, Object>>) rawResponseMap.get("actions");
        String url = (String) actions.get(0).get("url");

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
        String vaNumber = (String) rawResponseMap.get("permata_va_number");
        if (vaNumber == null) {
            List<Map<String, Object>> vaNums = (List<Map<String, Object>>) rawResponseMap.get("va_numbers");
            vaNumber = (String) vaNums.get(0).get("va_number");
        }
        response.put("va_number", vaNumber);
        response.put("id", id);
        return response;
    }

    @Override
    public String getProductEnv(String serviceName){
        String url = "";
        String baseUrl = (String) PropertiesReader.getProp(CONFIG_FILE, "base_url");
        String apiEndpoint = "";
        if (serviceName.equals("PaymentLink")){
            apiEndpoint = (String) PropertiesReader.getProp(CONFIG_FILE, "paymentlink");
        } else {
            apiEndpoint = (String) PropertiesReader.getProp(CONFIG_FILE, "apiendpoint");
        }
        
        url = baseUrl + apiEndpoint;
        System.out.println("url: " + url);

        return url;
    }

    @Override
    public HashMap<String, String> getHeaderParams() {
        HashMap<String, String> headerParams = new HashMap<>();
        String contentType = PropertiesReader.getProp(CONFIG_FILE, "content_type");
        String accept = PropertiesReader.getProp(CONFIG_FILE, "accept");
        String auth = PropertiesReader.getProp(CONFIG_FILE, "authorization");
        headerParams.put("authorization",auth);
        headerParams.put("content-type",contentType);
        headerParams.put("accept", accept);
        return headerParams;
    }
}