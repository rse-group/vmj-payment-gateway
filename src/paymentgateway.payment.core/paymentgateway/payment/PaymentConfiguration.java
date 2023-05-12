package paymentgateway.payment;

import java.lang.reflect.*;

import java.util.HashMap;
import java.util.Map;

import java.util.Base64;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class PaymentConfiguration {
    protected String apiKey;
    protected String apiUsername;
    protected String apiEndPoint;
    protected String paymentLinkApiEndPoint;

    public PaymentConfiguration() {
    }
    
    public String getProductName(){
    	return System.getProperty("user.dir").split("\\\\")[5];
    }
    
    public String getApiKey () {
    	return apiKey;
    }
    
    public void setApiKey(String apiKey) {
    	this.apiKey = apiKey;
    }
    
    public String getApiEndpoint() {
    	return apiEndPoint;
    }
    
    public void setApiEndPoint(String apiEndpoint) {
    	this.apiEndPoint = apiEndPoint;
    }
    
    public String getApiUsername() {
    	return apiUsername;
    }
    
    public String getPaymentLinkApiEndPoint() {
    	return paymentLinkApiEndPoint;
    }
    
    public static HashMap<String,Object> getProductEnv(){
    	HashMap<String,Object> prodEnv = new HashMap<>();
		try {
			String propClassName = "paymentgateway.payment.PropertiesReader";
			Class<?> propClass = Class.forName(propClassName); // convert string classname to class
			Object prop = propClass.newInstance();
			
			
			for (Method methodName : propClass.getDeclaredMethods()){
				if (methodName.toString().contains("Midtrans")) {
					System.out.println("name: " + methodName.toString());
					String[] name = methodName.toString().split(".");
//					System.out.println("name: " + name.get(name.length - 1));
				}	
			}
//			for (String name : productNames) {
//				Method getApiKey = prop.getClass().getMethod("getMidtransApiKey");
//		        apiKey = (String) getApiKey.invoke(prop);
//			}
	        Method getApiKey = prop.getClass().getMethod("getMidtransApiKey");
	        String apiKey = (String) getApiKey.invoke(prop);
	        
	        Method getApiEndpoint = prop.getClass().getMethod("getMidtransApiEndpoint");
	        String apiEndpoint = (String) getApiEndpoint.invoke(prop);
	        
	        prodEnv.put("MidtransApiKey",apiKey);
	        prodEnv.put("MidtransApiEndpoint",apiEndpoint);
		}
		catch (ClassNotFoundException e) {
			System.out.println(e);
		}
		catch (IllegalArgumentException e){
			System.out.println(e);
		}
		catch (InstantiationException e) {
			System.out.println(e);
		}
		catch (NoSuchMethodException e) {
			System.out.println(e);
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
		return prodEnv;
    }
    
	private static final String getBasicAuthenticationHeader(String username, String password) {
		String valueToEncode = username + ":" + password;
		return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
	}
	
//	public HttpRequest setHttpHeader (HashMap<String,String> headerElements) {
//		HttpRequest.Builder headerConfig = HttpRequest.newBuilder();
//		for (Map.Entry<String, String> entry : headerElements.entrySet()) {
//		    String key = entry.getKey();
//		    String val = entry.getValue();
//		    headerConfig.header(key,val);
//		}
//
//		return headerConfig;
//	}
	
//	public HttpRequest setHttpBody (HashMap<String,Object> bodyElements, HttpRequest header) {
//		HttpRequest headerConfig = HttpRequest.newBuilder();
//		for (Map.Entry<String, Object> entry : bodyElements.entrySet()) {
//		    String key = entry.getKey();
//		    String val = entry.getValue();
//		    headerConfig.header(key,val);
//		}
//
//		return headerConfig;
//	}
	
	
}	