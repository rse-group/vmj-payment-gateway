package paymentgateway.config.flip;

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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FlipConfiguration extends ConfigDecorator{

    public FlipConfiguration(ConfigComponent record) {
        super(record);
    }

    @Override
    public String getProductName(){
        return "Flip";
    }

    @Override
    public Map<String, Object> getMoneyTransferResponse(String rawResponse){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        
        int id = ((Double) rawResponseMap.get("id")).intValue();
        System.out.println(rawResponseMap.get("user_id"));
        int userId = ((Double) rawResponseMap.get("user_id")).intValue();
        String status = (String) rawResponseMap.get("status");
        response.put("status", status);
        response.put("user_id", userId);
        response.put("id", id);
        return response;
    }

    @Override
    public Map<String, Object> getAggregatorMoneyTransferResponse(String rawResponse){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        int agentId = ((Double) rawResponseMap.get("agent_id")).intValue();
        String direction = (String) rawResponseMap.get("direction");
        int id = ((Double) rawResponseMap.get("id")).intValue();
        String status = (String) rawResponseMap.get("status");
        response.put("status", status);
        response.put("id", id);
        response.put("agent_id", agentId);
        response.put("user_id", agentId);
        response.put("direction", direction);
        return response;
    }

    @Override
    public Map<String, Object> getFacilitatorMoneyTransferResponse(String rawResponse){
        Map<String, Object> response = getMoneyTransferResponse(rawResponse);
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        Map<String, Object> senderMap = (Map<String, Object>) rawResponseMap.get("sender");
        String senderName = (String) senderMap.get("sender_name");
        String senderAddress = (String) senderMap.get("sender_address");
        int senderCountry = ((Double) senderMap.get("sender_country")).intValue();;
        String senderJob = (String) senderMap.get("sender_job");

        String direction = (String) rawResponseMap.get("direction");
        response.put("direction", direction);
        response.put("name", senderName);
        response.put("address", senderAddress);
        response.put("country", senderCountry);
        response.put("job", senderJob);
        return response;
    }

    @Override
    public Map<String, Object> getInternationalMoneyTransferResponse(String rawResponse){
        Map<String, Object> response = getMoneyTransferResponse(rawResponse);
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        double exchangeRate = (double) rawResponseMap.get("exchange_rate");
        double fee = (double) rawResponseMap.get("fee");
        double amount = (double) rawResponseMap.get("amount");
        String sourceCountry = (String) rawResponseMap.get("source_country");
        String destinationCountry = (String) rawResponseMap.get("destination_country");
        String beneficiaryCurrencyCode = (String) rawResponseMap.get("beneficiary_currency_code");

        response.put("exchange_rate", exchangeRate);
        response.put("fee", fee);
        response.put("amount", amount);
        response.put("source_country", sourceCountry);
        response.put("destination_country", destinationCountry);
        response.put("beneficiary_currency_code", beneficiaryCurrencyCode);
        return response;
    }

    @Override
    public Map<String, Object> getVirtualAccountRequestBody(VMJExchange vmjExchange){
        int id = generateId();
        Map<String, Object> requestMap = new HashMap<>();
        String title = (String) vmjExchange.getRequestBodyForm("title");
        String type = (String) vmjExchange.getRequestBodyForm("type");
        int amount = Integer.parseInt((String)vmjExchange.getRequestBodyForm("amount"));
        String senderName = (String) vmjExchange.getRequestBodyForm("sender_name");
        String senderEmail = (String) vmjExchange.getRequestBodyForm("sender_email");
        String senderBank = (String) vmjExchange.getRequestBodyForm("bank");

        requestMap.put("id",id);
        requestMap.put("title", title);
        requestMap.put("type", type);
        requestMap.put("amount",amount);
        requestMap.put("sender_name",senderName);
        requestMap.put("sender_email",senderEmail);
        requestMap.put("sender_bank",senderBank);
        requestMap.put("sender_bank_type",SenderBankType.VIRTUALACCOUNT.getValue());
        requestMap.put("step", PaymentFlow.THIRD.getValue());

        return requestMap;
    }

    @Override
    public Map<String, Object> getVirtualAccountResponse(String rawResponse, int id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        String va_number = (String) rawResponseMap.get("account_number");
        response.put("va_number",va_number);
        response.put("id", id);
        return response;
    }

    @Override
    public Map<String, Object> getEWalletRequestBody(VMJExchange vmjExchange){
        int id = generateId();
        Map<String, Object> requestMap = new HashMap<>();
        String title = (String) vmjExchange.getRequestBodyForm("title");
        String type = (String) vmjExchange.getRequestBodyForm("type");
        int amount = Integer.parseInt((String)vmjExchange.getRequestBodyForm("amount"));
        String senderName = (String) vmjExchange.getRequestBodyForm("sender_name");
        String senderEmail = (String) vmjExchange.getRequestBodyForm("sender_email");
        String senderBank = (String) vmjExchange.getRequestBodyForm("payment_type");
        String senderPhoneNumber = (String) vmjExchange.getRequestBodyForm("phone_number");

        requestMap.put("id",id);
        requestMap.put("title", title);
        requestMap.put("type", type);
        requestMap.put("amount",amount);
        requestMap.put("sender_name",senderName);
        requestMap.put("sender_email",senderEmail);
        requestMap.put("sender_bank",senderBank);
        requestMap.put("is_phone_number_required",PhoneNumberRequired.TRUE.getValue());
        requestMap.put("sender_phone_number", senderPhoneNumber);
        requestMap.put("sender_bank_type",SenderBankType.EWALLET.getValue());
        requestMap.put("step", PaymentFlow.THIRD.getValue());

        return requestMap;
    }

    @Override
    public Map<String, Object> getEWalletResponse(String rawResponse, int id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        String url = (String) rawResponseMap.get("payment_url");
        String paymentType = (String) rawResponseMap.get("bank_code");
        String phoneNumber = (String) rawResponseMap.get("user_phone");
        response.put("phone_number",phoneNumber);
        response.put("url", url);
        response.put("payment_type",paymentType);
        response.put("id", id);
        return response;
    }


    @Override
    public Map<String, Object> getPaymentLinkRequestBody(VMJExchange vmjExchange){
        int id = generateId();
        Map<String, Object> requestMap = new HashMap<>();
        String title = (String) vmjExchange.getRequestBodyForm("title");
        String type = (String) vmjExchange.getRequestBodyForm("type");
        int amount = Integer.parseInt((String)vmjExchange.getRequestBodyForm("amount"));
        String senderEmail = (String) vmjExchange.getRequestBodyForm("sender_email");
        String senderName = (String) vmjExchange.getRequestBodyForm("sender_name");
        
        requestMap.put("id",id);
        requestMap.put("title", title);
        requestMap.put("type", type);
        requestMap.put("sender_name", senderName);
        requestMap.put("sender_email",senderEmail);
        requestMap.put("amount",amount);
        requestMap.put("step", PaymentFlow.SECOND.getValue() );

        return requestMap;
    }

    @Override
    public Map<String, Object> getPaymentLinkResponse(String rawResponse, int id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        String url = (String) rawResponseMap.get("link_url");
        response.put("url", url);
        response.put("id", id);
        return response;
    }

    @Override
    public HashMap<String, String> getHeaderParams() {
        HashMap<String, String> flipHeaderParams = new HashMap<>();
        String contentType = PropertiesReader.getProp("content_type");
        String authorization = PropertiesReader.getProp("authorization");
        String cookie = PropertiesReader.getProp("cookie");
        flipHeaderParams.put("Content-Type",contentType);
        flipHeaderParams.put("idempotency-key", UUID.randomUUID().toString());
        flipHeaderParams.put("X-TIMESTAMP","");
        flipHeaderParams.put("Authorization",authorization);
        flipHeaderParams.put("Cookie",cookie);
        return flipHeaderParams;
    }
}