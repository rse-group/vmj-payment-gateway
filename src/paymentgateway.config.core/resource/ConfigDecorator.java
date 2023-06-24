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

    public String getProductEnv(String productName, String serviceName){
        return record.getProductEnv(productName, serviceName);
        }
    public Object getPropertiesReader(){
        return record.getPropertiesReader();
        }
    public HttpRequest.Builder getBuilder(HttpRequest.Builder builder, HashMap<String, String> headerParams){
        return record.getBuilder(builder,headerParams);
        }
    public HashMap<String, String> getHeaderParams(String productName){
        return record.getHeaderParams(productName);
        }
    public Map<String, Object> processRequestMap(VMJExchange vmjExchange, String productName, String serviceName){
        return record.processRequestMap(vmjExchange, productName, serviceName);
        }
    public List<Map<String,Object>> toListMap(VMJExchange vmjExchange, String name){
        return record.toListMap(vmjExchange, name);
        }
    public int generateId(){

        return record.generateId();
        }
}