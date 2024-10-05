package paymentgateway.config.xendit;

import paymentgateway.config.core.ConfigDecorator;
import paymentgateway.config.core.ConfigComponent;
import paymentgateway.config.core.PropertiesReader;
import paymentgateway.config.core.RequestBodyValidator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.lang.reflect.*;

import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.BadRequestException;

public class XenditConfiguration extends ConfigDecorator{
    private String CONFIG_FILE = "xendit.properties";

    public XenditConfiguration(ConfigComponent record) {
        super(record);
    }

    @Override
    public String getVendorName(){
        return "Xendit";
    }
    
    @Override
    public Map<String, Object> getCallbackDisbursementRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> payload = vmjExchange.getPayload();
        
        // TODO: implement disbursement callback handler
        
        return requestMap;
    }

    @Override
    public Map<String, Object> getDisbursementRequestBody(Map<String, Object> requestBody) {
        String vendor_name = RequestBodyValidator.stringRequestBodyValidator(requestBody, "vendor_name");
        String bank_code = RequestBodyValidator.stringRequestBodyValidator(requestBody, "bank_code");
        String account_number = RequestBodyValidator.stringRequestBodyValidator(requestBody, "account_number");
        String account_holder_name = RequestBodyValidator.stringRequestBodyValidator(requestBody, "account_holder_name");
        String currency = RequestBodyValidator.stringRequestBodyValidator(requestBody, "currency");
        double amount = RequestBodyValidator.doubleRequestBodyValidator(requestBody, "amount");

        DisbursementCurrency.validate(currency);
        
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("vendor_name", vendor_name);
        requestMap.put("channel_code", bank_code);
        requestMap.put("bank_code", bank_code);
        requestMap.put("account_number", account_number);
        requestMap.put("currency", currency);
        requestMap.put("amount", amount);
        
        Map<String, Object> channelPropertiesMap = new HashMap<>();
        channelPropertiesMap.put("account_holder_name", account_holder_name);
        channelPropertiesMap.put("account_number", account_number);
        requestMap.put("channel_properties", channelPropertiesMap);
        
        String uuidString = UUID.randomUUID().toString().replace("-", "");
        int uniqueInteger = Math.abs(uuidString.hashCode()) % 100000;
        requestMap.put("reference_id", String.format("%05d", uniqueInteger));

        return requestMap;
    }
    
    @Override
    public String getPaymentDetailEndpoint(String configUrl, String id){
        configUrl = configUrl.replace("[id]", id);
        return configUrl;
    }

    @Override
    public String getProductEnv(String serviceName){
        return record.getProductEnv(CONFIG_FILE, serviceName);
    }
        
    @Override
    public Map<String, Object> getDisbursementResponse(String rawResponse) {
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);

        String idString = (String) rawResponseMap.get("reference_id");
        String userIdString = (String) rawResponseMap.get("business_id");
        String accountNumber = (String) ((Map<String, Object>) rawResponseMap.get("channel_properties")).get("account_number");
        String accountHolderName = (String) ((Map<String, Object>) rawResponseMap.get("channel_properties")).get("account_holder_name");
        double amount = ((Number) rawResponseMap.get("amount")).doubleValue();
        
        int id = Integer.parseInt(idString);
        int userId = Integer.parseInt(userIdString.substring(0,5).replaceAll("[^0-9]", ""));

        response.put("user_id", userId);
        response.put("id", id);
        response.put("amount", amount);
        response.put("account_number", accountNumber);
        response.put("account_holder_name", accountHolderName);
        response.put("status", rawResponseMap.get("status"));
        response.put("bank_code", rawResponseMap.get("channel_code"));
        response.put("currency", rawResponseMap.get("currency"));

        return response;
    }

    @Override
    public HashMap<String, String> getHeaderParams() {
        HashMap<String, String> xenditHeaderParams = new HashMap<>();
        String contentType = PropertiesReader.getProp(CONFIG_FILE, "content_type");
        String authorization = PropertiesReader.getProp(CONFIG_FILE, "authorization");
        xenditHeaderParams.put("Content-Type",contentType);
        xenditHeaderParams.put("idempotency-key", UUID.randomUUID().toString());
        xenditHeaderParams.put("X-TIMESTAMP","");
        xenditHeaderParams.put("Authorization",authorization);
        return xenditHeaderParams;
    }
}