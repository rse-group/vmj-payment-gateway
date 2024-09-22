package paymentgateway.config.xendit;

import paymentgateway.config.core.ConfigDecorator;
import paymentgateway.config.core.ConfigComponent;
import paymentgateway.config.core.PropertiesReader;
import paymentgateway.config.core.RequestBodyValidator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.lang.reflect.*;

import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.BadRequestException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
        String account_number = RequestBodyValidator.stringRequestBodyValidator(requestBody, "account_number")
        String account_holder_name = RequestBodyValidator.stringRequestBodyValidator(requestBody, "account_holder_name");
        String currency = RequestBodyValidator.stringRequestBodyValidator(requestBody, "currency");
        double amount = RequestBodyValidator.doubleRequestBodyValidator(requestBody, "amount");

        Map<String, Object> channelPropertiesMap = new HashMap<>();
        channelPropertiesMap.put("account_holder_name", accountHolderName);
        channelPropertiesMap.put("account_number", accountNumber);

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("reference_id", UUID.randomUUID().toString().substring(0, 8));
        requestMap.put("channel_code", bank_code);
        requestMap.put("currency", currency);
        requestMap.put("channel_properties", channelPropertiesMap);

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
    public String getRequestString(Map<String, Object> requestMap){
        return EncodeResponse.getParamsUrlEncoded(requestMap);
    }

    @Override
    public Map<String, Object> getDisbursementResponse(String rawResponse){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        
        int id = ((Double) rawResponseMap.get("id")).intValue();
        int businessId = ((Double) rawResponseMap.get("business_id")).intValue();
        String status = (String) rawResponseMap.get("status");
        response.put("status", status);
        response.put("user_id", businessId);
        response.put("id", id);
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