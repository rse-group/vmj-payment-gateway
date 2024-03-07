package paymentgateway.config.core;

import java.util.*;
import java.net.http.HttpRequest;
import vmj.routing.route.VMJExchange;

public interface Config {
    // get url lengkap sama service ex: moneytransfer
    String getProductEnv(String serviceName);
    // get config readernya (tetep disini atau dipindah? untuk sekarang dijadiin static)
    // Object getPropertiesReader();
    HttpRequest.Builder getBuilder(HttpRequest.Builder builder, HashMap<String, String> headerParams);
    // get header detail per produk nya (ex: flip) dilempar ke delta config
    HashMap<String, String> getHeaderParams();

    // get request body ini juga dilempar ke deltanya. how?
    Map<String, Object> processRequestMap(VMJExchange vmjExchange, String serviceName);
    List<Map<String,Object>> toListMap(VMJExchange vmjExchange, String name);
    int generateId();
}