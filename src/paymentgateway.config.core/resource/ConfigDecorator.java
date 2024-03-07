package paymentgateway.config.core;

import java.util.*;
import java.net.http.HttpRequest;
import vmj.routing.route.VMJExchange;

public abstract class ConfigDecorator extends ConfigComponent{

    protected ConfigComponent record;

    public ConfigDecorator(ConfigComponent record){
        this.record = record;
    }

    public ConfigDecorator(){
    }

    public String getProductEnv(String serviceName){
        return record.getProductEnv(serviceName);
        }
    public HttpRequest.Builder getBuilder(HttpRequest.Builder builder, HashMap<String, String> headerParams){
        return record.getBuilder(builder,headerParams);
        }
    public HashMap<String, String> getHeaderParams(){
        return record.getHeaderParams();
        }
    public Map<String, Object> processRequestMap(VMJExchange vmjExchange, String serviceName){
        return record.processRequestMap(vmjExchange, serviceName);
        }
    public List<Map<String,Object>> toListMap(VMJExchange vmjExchange, String name){
        return record.toListMap(vmjExchange, name);
        }
    public int generateId(){

        return record.generateId();
        }
}