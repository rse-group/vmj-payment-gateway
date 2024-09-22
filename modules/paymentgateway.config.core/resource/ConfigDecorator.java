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

    public String getVendorName(){
        return record.getVendorName();
    }

    public String getProductEnv(String fileName, String serviceName){
        return record.getProductEnv(fileName, serviceName);
    }
    
    public Map<String, Object> getCallbackPaymentRequestBody(VMJExchange vmjExchange){
    	return record.getCallbackPaymentRequestBody(vmjExchange);
    }
    
    public Map<String, Object> getCallbackDisbursementRequestBody(VMJExchange vmjExchange){
    	return record.getCallbackDisbursementRequestBody(vmjExchange);
    }

    public String getRequestString(Map<String, Object> requestMap){
        return record.getRequestString(requestMap);
    }

    public String getPaymentDetailEndpoint(String configUrl,String id){
        return record.getPaymentDetailEndpoint(configUrl,id);
    }

    public Map<String, Object> getPaymentStatusResponse(String rawResponse, String id){
        return record.getPaymentStatusResponse(rawResponse,id);
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

    public Map<String, Object> getDisbursementRequestBody(VMJExchange vmjExchange){
        return record.getDisbursementRequestBody(vmjExchange);
    }

    public Map<String, Object> getDomesticDisbursementRequestBody(VMJExchange vmjExchange){
        return record.getDomesticDisbursementRequestBody(vmjExchange);
    }

    public Map<String, Object> getInternationalDisbursementRequestBody(VMJExchange vmjExchange){
        return record.getInternationalDisbursementRequestBody(vmjExchange);
    }

    public Map<String, Object> getPaymentLinkRequestBody(VMJExchange vmjExchange){
        return record.getPaymentLinkRequestBody(vmjExchange);
    }

    public Map<String, Object> getRetailOutletRequestBody(VMJExchange vmjExchange){
        return record.getRetailOutletRequestBody(vmjExchange);
    }

    public Map<String, Object> getVirtualAccountRequestBody(VMJExchange vmjExchange){
        return record.getVirtualAccountRequestBody(vmjExchange);
    }

    public Map<String, Object> getEWalletRequestBody(VMJExchange vmjExchange){
        return record.getEWalletRequestBody(vmjExchange);
    }

    public Map<String, Object> getDebitCardRequestBody(VMJExchange vmjExchange){
        return record.getDebitCardRequestBody(vmjExchange);
    }

    public Map<String, Object> getCreditCardRequestBody(VMJExchange vmjExchange){
        return record.getCreditCardRequestBody(vmjExchange);
    }

    public Map<String, Object> getInvoiceRequestBody(VMJExchange vmjExchange){
        return record.getInvoiceRequestBody(vmjExchange);
    }

    public Map<String, Object> getPaymentRoutingRequestBody(VMJExchange vmjExchange){
        return record.getPaymentRoutingRequestBody(vmjExchange);
    }

    public Map<String, Object> getPaymentLinkResponse(String rawResponse, int id){
        return record.getPaymentLinkResponse(rawResponse, id);
    }

    public Map<String, Object> getDebitCardResponse(String rawResponse, int id){
        return record.getDebitCardResponse(rawResponse, id);
    }

    public Map<String, Object> getCreditCardResponse(String rawResponse, int id){
        return record.getCreditCardResponse(rawResponse, id);
    }

    public Map<String, Object> getInvoiceResponse(String rawResponse, int id){
        return record.getInvoiceResponse(rawResponse, id);
    }

    public Map<String, Object> getEWalletResponse(String rawResponse, int id){
        return record.getEWalletResponse(rawResponse, id);
    }

    public Map<String, Object> getPaymentRoutingResponse(String rawResponse, int id){
        return record.getPaymentRoutingResponse(rawResponse, id);
    }

    public Map<String, Object> getRetailOutletResponse(String rawResponse, int id){
        return record.getRetailOutletResponse(rawResponse, id);
    }

    public Map<String, Object> getVirtualAccountResponse(String rawResponse, int id){
        return record.getVirtualAccountResponse(rawResponse, id);
    }

    public Map<String, Object> getDisbursementResponse(String rawResponse){
        return record.getDisbursementResponse(rawResponse);
    }

    public Map<String, Object> getSpecialDisbursementResponse(String rawResponse){
        return record.getSpecialDisbursementResponse(rawResponse);
    }

    public Map<String, Object> getInternationalDisbursementResponse(String rawResponse){
        return record.getInternationalDisbursementResponse(rawResponse);
    }

    public Map<String, Object> getAgentDisbursementResponse(String rawResponse){
        return record.getAgentDisbursementResponse(rawResponse);
    }
    
}