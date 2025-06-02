package paymentgateway.config.core;

import java.util.*;
import java.net.http.HttpRequest;
import vmj.routing.route.VMJExchange;

public abstract class ConfigComponent implements Config {
    public abstract String getVendorName();
    public abstract String getProductEnv(String fileName, String serviceName);
    public abstract String getProductEnv(String serviceName);
    public abstract String constructUrlParam(String serviceName, Map<String, Object> requestBody);
    public abstract String getRequestString(Map<String, Object> requestMap);
    public abstract HttpRequest.Builder getBuilder(HttpRequest.Builder builder, HashMap<String, String> headerParams);
    public abstract HashMap<String, String> getHeaderParams();
    public abstract Map<String, Object> processRequestMap(VMJExchange vmjExchange, String serviceName);
    public abstract List<Map<String,Object>> toListMap(VMJExchange vmjExchange, String name);
    public abstract int generateId();
    public abstract String getPaymentDetailEndpoint(String configUrl, Map<String, Object> paymentMap);
    public abstract Map<String, Object> getCallbackPaymentRequestBody(VMJExchange vmjExchange);
    public abstract Map<String, Object> getCallbackDisbursementRequestBody(Map<String, Object> requestBody);

    public abstract HttpRequest createPaymentDetailEndpointRequestObject(String configUrl, Map<String, Object> paymentMap, String tableName);
    public abstract Map<String, Object> getPaymentStatusResponse(String rawResponse, String id);

    public abstract Map<String, Object> getDisbursementRequestBody(Map<String, Object> requestBody);
    public abstract void validateBaseAgentDisbursementRequestBody(Map<String, Object> requestBody);
    public abstract Map<String, Object> getDomesticDisbursementRequestBody(Map<String, Object> requestBody);
    public abstract Map<String, Object> getInternationalDisbursementRequestBody(Map<String, Object> requestBody);
    public abstract void validateBaseInternationalDisbursementRequestBody(Map<String, Object> requestBody);

    public abstract Map<String, Object> getPaymentLinkRequestBody(Map<String, Object> requestBody);
    public abstract Map<String, Object> getRetailOutletRequestBody(Map<String, Object> requestBody);
    public abstract Map<String, Object> getVirtualAccountRequestBody(Map<String, Object> requestBody);
    public abstract Map<String, Object> getEWalletRequestBody(Map<String, Object> requestBody);
    public abstract Map<String, Object> getCardRequestBody(Map<String, Object> requestBody);
    public abstract Map<String, Object> getDirectDebitRequestBody(Map<String, Object> requestBody);
    public abstract Map<String, Object> getInvoiceRequestBody(Map<String, Object> requestBody);
    public abstract Map<String, Object> getPaymentRoutingRequestBody(Map<String, Object> requestBody);
    public abstract Map<String, Object> getQRCodeRequestBody(Map<String, Object> requestBody);
    
    public abstract Map<String, Object> getPaymentLinkResponse(String rawResponse, String id);
    public abstract Map<String, Object> getCardResponse(String rawResponse, String id);
    public abstract Map<String, Object> getDirectDebitResponse(String rawResponse, String id);
    public abstract Map<String, Object> getInvoiceResponse(String rawResponse, String id);
    public abstract Map<String, Object> getEWalletResponse(String rawResponse, String id);
    public abstract Map<String, Object> getPaymentRoutingResponse(String rawResponse, String id);
    public abstract Map<String, Object> getRetailOutletResponse(String rawResponse, String id);
    public abstract Map<String, Object> getVirtualAccountResponse(String rawResponse, String id);
    public abstract Map<String, Object> getQRCodeResponse(String rawResponse, String id);

    public abstract Map<String, Object> getDisbursementResponse(String rawResponse, String id);
    public abstract Map<String, Object> getSpecialDisbursementResponse(String rawResponse, String id);
    public abstract Map<String, Object> getInternationalDisbursementResponse(String rawResponse, String id);
    public abstract Map<String, Object> getAgentDisbursementResponse(String rawResponse, String id);
}