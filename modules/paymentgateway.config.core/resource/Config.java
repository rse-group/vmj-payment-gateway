package paymentgateway.config.core;

import java.util.*;
import java.net.http.HttpRequest;
import vmj.routing.route.VMJExchange;

public interface Config {
    String getVendorName();

    // get url lengkap sama service ex: disbursement
    String getProductEnv(String fileName, String serviceName);
    String getProductEnv(String serviceName);

    //
    String getRequestString(Map<String, Object> requestMap);

    // get config readernya (tetep disini atau dipindah? untuk sekarang dijadiin static)
    // Object getPropertiesReader();

    HttpRequest.Builder getBuilder(HttpRequest.Builder builder, HashMap<String, String> headerParams);

    // get header detail per produk nya (ex: flip) dilempar ke delta config
    HashMap<String, String> getHeaderParams();

    // to construct url with query parameter
    String constructUrlParam(String serviceName, Map<String, Object> requestBody);

    // get request body ini juga dilempar ke deltanya.
    Map<String, Object> processRequestMap(VMJExchange vmjExchange, String serviceName);
    
    List<Map<String,Object>> toListMap(VMJExchange vmjExchange, String name);
    int generateId();
    
    Map<String, Object> getCallbackPaymentRequestBody(VMJExchange vmjExchange);
    Map<String, Object> getCallbackDisbursementRequestBody(Map<String, Object> requestBody);

    // Disbursement Request
    Map<String, Object> getDisbursementRequestBody(Map<String, Object> requestBody);
    Map<String, Object> getDomesticDisbursementRequestBody(Map<String, Object> requestBody);
    Map<String, Object> getInternationalDisbursementRequestBody(Map<String, Object> requestBody);

    // Payment Request
    Map<String, Object> getPaymentLinkRequestBody(CreatePaymentLinkRequestBody requestBody);
    Map<String, Object> getRetailOutletRequestBody(CreateRetailOutletPaymentRequestBody requestBody);
    Map<String, Object> getVirtualAccountRequestBody(CreateVirtualAccountPaymentRequestBody requestBody);
    Map<String, Object> getEWalletRequestBody(CreateEWalletPaymentRequestBody requestBody);
    Map<String, Object> getDebitCardRequestBody(CreateDebitCardPaymentRequestBody requestBody);
    Map<String, Object> getCreditCardRequestBody(CreateCreditCardPaymentRequestBody requestBody);
    Map<String, Object> getInvoiceRequestBody(CreateInvoiceRequestBody requestBody);
    Map<String, Object> getPaymentRoutingRequestBody(CreatePaymentRoutingPaymentRequestBody requestBody);

    String getPaymentDetailEndpoint(String configUrl,String id);

    Map<String, Object> getPaymentStatusResponse(String rawResponse, String id);

    // Payment Response
    Map<String, Object> getPaymentLinkResponse(String rawResponse, int id);
    Map<String, Object> getDebitCardResponse(String rawResponse, int id);
    Map<String, Object> getCreditCardResponse(String rawResponse, int id);
    Map<String, Object> getInvoiceResponse(String rawResponse, int id);
    Map<String, Object> getEWalletResponse(String rawResponse, int id);
    Map<String, Object> getPaymentRoutingResponse(String rawResponse, int id);
    Map<String, Object> getRetailOutletResponse(String rawResponse, int id);
    Map<String, Object> getVirtualAccountResponse(String rawResponse, int id);

    // Disbursement Response
    Map<String, Object> getDisbursementResponse(String rawResponse);
    Map<String, Object> getSpecialDisbursementResponse(String rawResponse);
    Map<String, Object> getInternationalDisbursementResponse(String rawResponse);
    Map<String, Object> getAgentDisbursementResponse(String rawResponse);
}