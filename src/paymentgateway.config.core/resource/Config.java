package paymentgateway.config.core;

import java.util.*;
import java.net.http.HttpRequest;
import vmj.routing.route.VMJExchange;

public interface Config {
    String getVendorName();

    // get url lengkap sama service ex: moneytransfer
    String getProductEnv(String fileName, String serviceName);
    String getProductEnv(String serviceName);

    //
    String getRequestString(Map<String, Object> requestMap);

    // get config readernya (tetep disini atau dipindah? untuk sekarang dijadiin static)
    // Object getPropertiesReader();

    HttpRequest.Builder getBuilder(HttpRequest.Builder builder, HashMap<String, String> headerParams);

    // get header detail per produk nya (ex: flip) dilempar ke delta config
    HashMap<String, String> getHeaderParams();

    // get request body ini juga dilempar ke deltanya.
    Map<String, Object> processRequestMap(VMJExchange vmjExchange, String serviceName);
    
    List<Map<String,Object>> toListMap(VMJExchange vmjExchange, String name);
    int generateId();
    
    Map<String, Object> getCallbackPaymentRequestBody(VMJExchange vmjExchange);
    Map<String, Object> getCallbackDisbursementRequestBody(VMJExchange vmjExchange);

    // Disbursement Request
    Map<String, Object> getMoneyTransferRequestBody(VMJExchange vmjExchange);
    Map<String, Object> getSpecialMoneyTransferRequestBody(VMJExchange vmjExchange);
    Map<String, Object> getInternationalMoneyTransferRequestBody(VMJExchange vmjExchange);
    Map<String, Object> getAgentMoneyTransferRequestBody(VMJExchange vmjExchange);

    // Payment Request
    Map<String, Object> getPaymentLinkRequestBody(VMJExchange vmjExchange);
    Map<String, Object> getRetailOutletRequestBody(VMJExchange vmjExchange);
    Map<String, Object> getVirtualAccountRequestBody(VMJExchange vmjExchange);
    Map<String, Object> getEWalletRequestBody(VMJExchange vmjExchange);
    Map<String, Object> getDebitCardRequestBody(VMJExchange vmjExchange);
    Map<String, Object> getCreditCardRequestBody(VMJExchange vmjExchange);
    Map<String, Object> getInvoiceRequestBody(VMJExchange vmjExchange);
    Map<String, Object> getPaymentRoutingRequestBody(VMJExchange vmjExchange);

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
    Map<String, Object> getMoneyTransferResponse(String rawResponse);
    Map<String, Object> getSpecialMoneyTransferResponse(String rawResponse);
    Map<String, Object> getInternationalMoneyTransferResponse(String rawResponse);
    Map<String, Object> getAgentMoneyTransferResponse(String rawResponse);
}