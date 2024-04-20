package paymentgateway.disbursement;

import java.lang.reflect.*;
import java.net.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Base64;

public class DisbursementConfiguration {

    public DisbursementConfiguration() {
    }

    public String getProductName(){
        return System.getProperty("user.dir").split("\\\\")[5];
    }

    public static String getProductEnv(String serviceName){
        String url = "";
        try {
            Object prop = getPropertiesReader();

            Method getFlipBaseUrlTest = prop.getClass().getMethod("getFlipBaseUrlTest");
            String baseUrl = (String) getFlipBaseUrlTest.invoke(prop);

            Method getApiEndpoint = prop.getClass().getMethod("get" + serviceName);
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

    public static Object getPropertiesReader(){
        Object prop = null;
        try {
            String propClassName = "paymentgateway.disbursement.core.util.PropertiesReader";
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

    private static final String getBasicAuthenticationHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }

    public static HttpRequest.Builder getBuilder(HttpRequest.Builder builder, HashMap<String, String> headerParams){
        for (Map.Entry<String, String> e : headerParams.entrySet()) {
            builder.header(e.getKey(), e.getValue());
        }
        return builder;
    }

    public static HashMap<String, String> getHeaderParams(String productName){
        HashMap<String, String> headerParams = new HashMap<>();
        Object prop = getPropertiesReader();

        try {
            Method getFlipBaseUrlTest = prop.getClass().getMethod("get" + productName + "HeaderParams");
            headerParams = (HashMap<String, String>) getFlipBaseUrlTest.invoke(prop);

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


}
