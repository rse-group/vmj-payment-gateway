package paymentgateway.config.core;

import java.util.*;
import java.net.http.HttpRequest;
import vmj.routing.route.VMJExchange;

public abstract class ConfigComponent implements Config {
    public abstract String getVendorName();
    public abstract String getProductEnv(String fileName, String serviceName);
    public abstract String getProductEnv(String serviceName);
    public abstract String getRequestString(Map<String, Object> requestMap);
    public abstract HttpRequest.Builder getBuilder(HttpRequest.Builder builder, HashMap<String, String> headerParams);
    public abstract HashMap<String, String> getHeaderParams();
    public abstract Map<String, Object> processRequestMap(VMJExchange vmjExchange, String serviceName);
    public abstract List<Map<String,Object>> toListMap(VMJExchange vmjExchange, String name);
    public abstract int generateId();
    public abstract String getPaymentDetailEndpoint(String configUrl,String id);
    public abstract Map<String, Object> getCallbackPaymentRequestBody(VMJExchange vmjExchange);
    public abstract Map<String, Object> getCallbackDisbursementRequestBody(VMJExchange vmjExchange);

    public abstract Map<String, Object> getPaymentStatusResponse(String rawResponse, String id);

    public abstract Map<String, Object> getDomesticMoneyTransferRequestBody(Map<String, Object> requestBody);
    public abstract Map<String, Object> getInternationalMoneyTransferRequestBody(Map<String, Object> requestBody);

    public abstract Map<String, Object> getPaymentLinkRequestBody(VMJExchange vmjExchange);
    public abstract Map<String, Object> getRetailOutletRequestBody(VMJExchange vmjExchange);
    public abstract Map<String, Object> getVirtualAccountRequestBody(VMJExchange vmjExchange);
    public abstract Map<String, Object> getEWalletRequestBody(VMJExchange vmjExchange);
    public abstract Map<String, Object> getDebitCardRequestBody(VMJExchange vmjExchange);
    public abstract Map<String, Object> getCreditCardRequestBody(VMJExchange vmjExchange);
    public abstract Map<String, Object> getInvoiceRequestBody(VMJExchange vmjExchange);
    public abstract Map<String, Object> getPaymentRoutingRequestBody(VMJExchange vmjExchange);
    
    public abstract Map<String, Object> getPaymentLinkResponse(String rawResponse, int id);
    public abstract Map<String, Object> getDebitCardResponse(String rawResponse, int id);
    public abstract Map<String, Object> getCreditCardResponse(String rawResponse, int id);
    public abstract Map<String, Object> getInvoiceResponse(String rawResponse, int id);
    public abstract Map<String, Object> getEWalletResponse(String rawResponse, int id);
    public abstract Map<String, Object> getPaymentRoutingResponse(String rawResponse, int id);
    public abstract Map<String, Object> getRetailOutletResponse(String rawResponse, int id);
    public abstract Map<String, Object> getVirtualAccountResponse(String rawResponse, int id);

    public abstract Map<String, Object> getMoneyTransferResponse(String rawResponse);
    public abstract Map<String, Object> getSpecialMoneyTransferResponse(String rawResponse);
    public abstract Map<String, Object> getInternationalMoneyTransferResponse(String rawResponse);
    public abstract Map<String, Object> getAgentMoneyTransferResponse(String rawResponse);
}