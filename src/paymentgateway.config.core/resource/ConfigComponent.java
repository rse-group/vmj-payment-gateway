package paymentgateway.config.core;

import java.util.*;
import java.net.http.HttpRequest;
import vmj.routing.route.VMJExchange;

public abstract class ConfigComponent implements Config {
    public abstract String getProductEnv(String productName, String serviceName);
    public abstract  Object getPropertiesReader();
    public abstract HttpRequest.Builder getBuilder(HttpRequest.Builder builder, HashMap<String, String> headerParams);
    public abstract HashMap<String, String> getHeaderParams(String productName);
    public abstract Map<String, Object> processRequestMap(VMJExchange vmjExchange, String productName, String serviceName);
    public abstract List<Map<String,Object>> toListMap(VMJExchange vmjExchange, String name);
    public abstract int generateId();
}