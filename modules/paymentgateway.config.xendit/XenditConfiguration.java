package paymentgateway.config.xendit;

import paymentgateway.config.core.ConfigDecorator;
import paymentgateway.config.core.ConfigComponent;
import paymentgateway.config.core.PropertiesReader;
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
        
        // TODO
        
        return requestMap;
    }

    @Override
    public Map<String, Object> getDomesticMoneyTransferRequestBody(VMJExchange vmjExchange) {
        String bank_code = (String) vmjExchange.getRequestBodyForm("bank_code");

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("channel_code", bank_code);

        return requestMap;
    }
    
    @Override
    public String getPaymentDetailEndpoint(String configUrl,String id){
        configUrl = configUrl.replace("[id]", id);
        return configUrl;
    }

    @Override
    public Map<String, Object> getPaymentStatusResponse(String rawResponse, String id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        if (rawResponseMap.containsKey("errors")) {
            List<Map<String, Object>> errors = (List<Map<String, Object>>) rawResponseMap.get("errors");
            if (!errors.isEmpty()) {
                Map<String, Object> firstError = errors.get(0);
                String errorMessage = (String) firstError.get("message");
                response.put("error", errorMessage);
                return response;
            }
        }
        ArrayList<Object> dataList = (ArrayList<Object>) rawResponseMap.get("data");
        if (!dataList.isEmpty()) {
            Map<String, Object> dataObject = (Map<String, Object>) dataList.get(0);
            String status = (String) dataObject.get("status");
            response.put("status", status);
            response.put("id", id);
        } else {
            response.put("id", id);
            response.put("status", PaymentStatus.PENDING.getStatus());
        }
        return response;
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
    public Map<String, Object> getMoneyTransferResponse(String rawResponse){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        
        // TODO: convert to Xendit response parsing
        int id = ((Double) rawResponseMap.get("id")).intValue();
        int userId = ((Double) rawResponseMap.get("user_id")).intValue();
        String status = (String) rawResponseMap.get("status");
        response.put("status", status);
        response.put("user_id", userId);
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