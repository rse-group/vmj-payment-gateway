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

public class ConfigImpl extends ConfigComponent {

    protected ConfigComponent record;

//    public ConfigImpl(){
//
//    }

    public String getProductEnv(String productName, String serviceName){
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

    public Object getPropertiesReader(){
        Object prop = null;
        try {
            String propClassName = "paymentgateway.config.core.PropertiesReader";
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

    public HttpRequest.Builder getBuilder(HttpRequest.Builder builder, HashMap<String, String> headerParams){
        for (Map.Entry<String, String> e : headerParams.entrySet()) {
            builder.header(e.getKey(), e.getValue());
        }
        return builder;
    }

    public HashMap<String, String> getHeaderParams(String productName){
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

    public Map<String, Object>  processRequestMap(VMJExchange vmjExchange, String productName, String serviceName){

        Map<String, Object> result = null;
        Object prop = null;
        String className = "paymentgateway.config." + productName.toLowerCase() + "." + productName + "Configuration";
        try {
            Class<?> clz = Class.forName(className);
            Constructor<?> constructor = clz.getDeclaredConstructors()[0];
            prop = (Config) constructor.newInstance(this);
            Method m = prop.getClass().getMethod("get" + productName + serviceName + "RequestBody", VMJExchange.class);
            result = (Map<String, Object>) m.invoke(prop, vmjExchange);
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
}