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

    public String getProductName(){
        return "";
    }

    public String getProductEnv(String serviceName){
        String url = "";
        String baseUrl = (String) PropertiesReader.getProp("base_url");
        String apiEndpoint = (String) PropertiesReader.getProp(serviceName);

        url = baseUrl + apiEndpoint;
        System.out.println("url: " + url);
        // try {
        //     String baseUrl = (String) PropertiesReader.getProp("base_url");
        //     String apiEndpoint = (String) PropertiesReader.getProp(serviceName);

        //     url = baseUrl + apiEndpoint;
        //     System.out.println("url: " + url);
        // }
        // catch (IllegalArgumentException e){
        //     e.printStackTrace();
        // }
        // catch (NoSuchMethodException e) {
        //     e.printStackTrace();
        // }
        // catch (Exception e) {
        //     e.printStackTrace();
        // }

        return url;
    }

    // public Object getPropertiesReader(){
    //     Object prop = null;
    //     try {
    //         String propClassName = "paymentgateway.config.core.PropertiesReader";
    //         Class<?> propClass = Class.forName(propClassName); // convert string classname to class
    //         prop = propClass.newInstance();
    //     }
    //     catch (ClassNotFoundException e) {
    //         e.printStackTrace();
    //     }
    //     catch (IllegalArgumentException e){
    //         e.printStackTrace();
    //     }
    //     catch (InstantiationException e) {
    //         e.printStackTrace();
    //     }
    //     catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return prop;
    // }

    public HttpRequest.Builder getBuilder(HttpRequest.Builder builder, HashMap<String, String> headerParams){
        for (Map.Entry<String, String> e : headerParams.entrySet()) {
            builder.header(e.getKey(), e.getValue());
        }
        return builder;
    }

    public HashMap<String, String> getHeaderParams(){
        HashMap<String, String> headerParams = new HashMap<>();
        // Object prop = getPropertiesReader();

        // try {
        //     Method method = prop.getClass().getMethod("get" + productName + "HeaderParams");
        //     headerParams = (HashMap<String, String>) method.invoke(prop);

        // }
        // catch (IllegalArgumentException e){
        //     e.printStackTrace();
        // }
        // catch (NoSuchMethodException e) {
        //     e.printStackTrace();
        // }
        // catch (Exception e) {
        //     e.printStackTrace();
        // }


        return headerParams;
    }

    public int generateId(){
        System.out.println("masuk generate id");
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
        // Map<String, Object> result = new HashMap<>();
        // Map<String, Object> result = null;
        // Object prop = null;
        // String className = "paymentgateway.config." + productName.toLowerCase() + "." + productName + "Configuration";
        // try {
        //     Class<?> clz = Class.forName(className);
        //     Constructor<?> constructor = clz.getDeclaredConstructors()[0];
        //     prop = (Config) constructor.newInstance(this);
        //     Method m = prop.getClass().getMethod("get" + productName + serviceName + "RequestBody", VMJExchange.class);
        //     result = (Map<String, Object>) m.invoke(prop, vmjExchange);
        // }
        // catch (IllegalArgumentException e){
        //     e.printStackTrace();
        // }
        // catch (InvocationTargetException e){
        //     e.printStackTrace();
        // }
        // catch (Exception e) {
        //     e.printStackTrace();
        // }
        // return result;
        return vmjExchange.getPayload();
    }

    public Map<String, Object> getMoneyTransferRequestBody(VMJExchange vmjExchange){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getSpecialMoneyTransferRequestBody(VMJExchange vmjExchange){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getInternationalMoneyTransferRequestBody(VMJExchange vmjExchange){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getAgentMoneyTransferRequestBody(VMJExchange vmjExchange){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getPaymentLinkRequestBody(VMJExchange vmjExchange){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getRetailOutletRequestBody(VMJExchange vmjExchange){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getVirtualAccountRequestBody(VMJExchange vmjExchange){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getEWalletRequestBody(VMJExchange vmjExchange){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getDebitCardRequestBody(VMJExchange vmjExchange){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getCreditCardRequestBody(VMJExchange vmjExchange){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getInvoiceRequestBody(VMJExchange vmjExchange){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getPaymentRoutingRequestBody(VMJExchange vmjExchange){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getPaymentLinkResponse(String rawResponse, int id){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getDebitCardResponse(String rawResponse, int id){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getCreditCardResponse(String rawResponse, int id){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getInvoiceResponse(String rawResponse, int id){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getEWalletResponse(String rawResponse, int id){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getPaymentRoutingResponse(String rawResponse, int id){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getRetailOutletResponse(String rawResponse, int id){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getVirtualAccountResponse(String rawResponse, int id){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getMoneyTransferResponse(String rawResponse){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getSpecialMoneyTransferResponse(String rawResponse){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getInternationalMoneyTransferResponse(String rawResponse){
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> getAgentMoneyTransferResponse(String rawResponse){
        throw new UnsupportedOperationException();
    }
}