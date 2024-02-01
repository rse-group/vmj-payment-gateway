package paymentgateway.config.core;

import java.util.*;
import java.net.http.HttpRequest;
import vmj.routing.route.VMJExchange;

public interface Config {
    String getProductEnv(String productName, String serviceName);
    Object getPropertiesReader();
    HttpRequest.Builder getBuilder(HttpRequest.Builder builder, HashMap<String, String> headerParams);
    HashMap<String, String> getHeaderParams(String productName);
    Map<String, Object> processRequestMap(VMJExchange vmjExchange, String productName, String serviceName);
    List<Map<String,Object>> toListMap(VMJExchange vmjExchange, String name);
    int generateId();
}