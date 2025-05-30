package paymentgateway.config.core;

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

import paymentgateway.config.core.PropertiesReader;

public class ConfigImpl extends ConfigComponent {

    protected ConfigComponent record;

//    public ConfigImpl(){
//
//    }

    public String getVendorName(){
        return "";
    }
    
    public String getRequestString(Map<String, Object> requestMap){
        Gson gson = new Gson();
        return gson.toJson(requestMap);
    }

    public String getProductEnv(String fileName, String serviceName){
        String url = "";
        String baseUrl = (String) PropertiesReader.getProp(fileName, "base_url");
        String apiEndpoint = (String) PropertiesReader.getProp(fileName, serviceName);

        if (apiEndpoint == null) {
            return null;
        }

        url = baseUrl + apiEndpoint;

        return url;
    }
    
    public Map<String, Object> getCallbackPaymentRequestBody(VMJExchange vmjExchange){
    	throw new UnsupportedOperationException();
    }
    
    public Map<String, Object> getCallbackDisbursementRequestBody(Map<String, Object> requestBody){
    	throw new UnsupportedOperationException();
    }

    public String getProductEnv(String serviceName){
        throw new UnsupportedOperationException();
    }
    
    public String constructUrlParam(String serviceName, Map<String, Object> requestBody){
        throw new UnsupportedOperationException();
    }

    public String getPaymentDetailEndpoint(String configUrl, Map<String, Object> paymentMap){
        // Reference: https://stackoverflow.com/a/12595052
        System.out.println(configUrl);
        String idFormat = configUrl.substring(configUrl.indexOf("["));
        idFormat = idFormat.substring(0, idFormat.indexOf("]") + 1);

        String id;

        switch (idFormat) {
            case "[vendorGeneratedId]":
                id = (String) paymentMap.get("vendorGeneratedId");
                break;
            default:
                id = (String) paymentMap.get("id");
                break;
        }

        configUrl = configUrl.replace(idFormat, id);
        return configUrl;
    }

    public Map<String, Object> getPaymentStatusResponse(String rawResponse, String id){
        throw new UnsupportedOperationException();
    }

    public HttpRequest.Builder getBuilder(HttpRequest.Builder builder, HashMap<String, String> headerParams){
        for (Map.Entry<String, String> e : headerParams.entrySet()) {
            builder.header(e.getKey(), e.getValue());
        }
        return builder;
    }

    public HashMap<String, String> getHeaderParams(){
        HashMap<String, String> headerParams = new HashMap<>();
        return headerParams;
    }

    public int generateId(){
        String generateUUIDNo = String.format("%010d",new BigInteger(UUID.randomUUID().toString().replace("-",""),16));
        return Integer.parseInt(generateUUIDNo.substring(0,5));
    }

    public List<Map<String,Object>> toListMap(VMJExchange vmjExchange, String name){
        Gson gson = new Gson();
        Type resultType = new TypeToken<List<Map<String, Object>>>(){}.getType();
        List<Map<String, Object>> result = gson.fromJson(gson.toJson(vmjExchange.getRequestBodyForm(name)), resultType);
        return result;
    }

    public Map<String, Object>  processRequestMap(VMJExchange vmjExchange, String serviceName){
        return vmjExchange.getPayload();
    }

    // Disbursement Request Body

    public Map<String, Object> getDisbursementRequestBody(Map<String, Object> requestBody){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getAgentDisbursementRequestBody(Map<String, Object> requestBody) {
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getDomesticDisbursementRequestBody(Map<String, Object> requestBody){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getInternationalDisbursementRequestBody(Map<String, Object> requestBody){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> validateBaseInternationalDisbursementRequestBody(Map<String, Object> requestBody) {
        throw new UnsupportedOperationException();
    }
    
    // Payment Request Body

    public Map<String, Object> getPaymentLinkRequestBody(Map<String, Object> requestBody){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getRetailOutletRequestBody(Map<String, Object> requestBody){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getVirtualAccountRequestBody(Map<String, Object> requestBody){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getEWalletRequestBody(Map<String, Object> requestBody){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getCardRequestBody(Map<String, Object> requestBody){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getDirectDebitRequestBody(Map<String, Object> requestBody){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getInvoiceRequestBody(Map<String, Object> requestBody){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getPaymentRoutingRequestBody(Map<String, Object> requestBody){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getQRCodeRequestBody(Map<String, Object> requestBody){
        throw new UnsupportedOperationException();
    }

    // Payment Response

    public Map<String, Object> getPaymentLinkResponse(String rawResponse, String id){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getCardResponse(String rawResponse, String id){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getDirectDebitResponse(String rawResponse, String id){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getInvoiceResponse(String rawResponse, String id){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getEWalletResponse(String rawResponse, String id){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getPaymentRoutingResponse(String rawResponse, String id){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getRetailOutletResponse(String rawResponse, String id){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getVirtualAccountResponse(String rawResponse, String id){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getQRCodeResponse(String rawResponse, String id) {
        throw new UnsupportedOperationException();
    }

    // Disbursement Response

    public Map<String, Object> getDisbursementResponse(String rawResponse, String id){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getSpecialDisbursementResponse(String rawResponse, String id){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getInternationalDisbursementResponse(String rawResponse, String id){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getAgentDisbursementResponse(String rawResponse, String id){
        throw new UnsupportedOperationException();
    }
}