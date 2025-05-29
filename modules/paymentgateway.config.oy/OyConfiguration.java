package paymentgateway.config.oy;

import paymentgateway.config.core.ConfigDecorator;
import paymentgateway.config.core.ConfigComponent;
import paymentgateway.config.core.PropertiesReader;
import paymentgateway.config.core.RequestBodyValidator;
import vmj.routing.route.exceptions.BadRequestException;

import java.util.*;
import java.lang.reflect.*;

import vmj.routing.route.VMJExchange;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class OyConfiguration extends ConfigDecorator{
    private String CONFIG_FILE = "oy.properties";

    public OyConfiguration(ConfigComponent record) {
        super(record);
    }

    @Override
    public String getVendorName(){
        return "Oy";
    }

    @Override
    public String getProductEnv(String serviceName){
        return record.getProductEnv(CONFIG_FILE, serviceName);
    }

    @Override
    public Map<String, Object> getCallbackPaymentRequestBody(VMJExchange vmjExchange){
	    Map<String, Object> requestMap = new HashMap<>();
	    Map<String, Object> payload = vmjExchange.getPayload();
	    String id = "";
	    String status = "";
		
	    if (payload.get("success") != null) {
	        boolean successStatus = ((boolean) payload.get("success"));
	        if (payload.get("partner_trx_id") != null) {
	        	id = (String) payload.get("partner_trx_id");
	        } else if (payload.get("partner_user_id")!=null) {
	        	id = (String) payload.get("partner_user_id");
	        }
	        status = successStatus ? PaymentStatus.SUCCESSFUL.getStatus() : PaymentStatus.FAILED.getStatus();
	    } else {
			id = (String) payload.get("partner_tx_id");
			status = (String) payload.get("status");
		}

        requestMap.put("id",id);
        requestMap.put("status", status);
        return requestMap;
    }
     
    @Override
    public Map<String, Object> getPaymentStatusResponse(String rawResponse, String id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        System.out.println("rawResponseMap" + rawResponseMap);

        Boolean isSuccess = (Boolean) rawResponseMap.get("success");

        if (Boolean.FALSE.equals(isSuccess)) {
            Map<String, Object> error = (Map<String, Object>) rawResponseMap.get("error");
            String errorMessage = error != null ? (String) error.get("message") : "Unknown error";
            throw new BadRequestException(errorMessage);
        }

        Map<String, Object> paymentData = (Map<String, Object>) rawResponseMap.get("data");
        
        String status;
        if (paymentData != null) {
            // for payment link, invoice, payment routing
            status = (String) paymentData.get("status");
        } else {
            // for ewallet payment
            status = (String) rawResponseMap.get("ewallet_trx_status");
            if (status == null) {
                // for virtual account
                status = (String) rawResponseMap.get("va_status");
            }
        }       

        response.put("status", status);
        response.put("id", id);
        return response;
    }

    public Map<String, String> getOyBankCode(){
        Map<String, String> immutableMap = Map.of("bni", "009",
                "bca", "014",
                "mandiri", "008",
                "bri", "002",
                "permata", "013",
                "cimb", "022",
                "smbc", "213",
                "bsi", "451");
        Map<String, String> bankCodes = new HashMap<>(immutableMap);
        return bankCodes;
    }

    public Map<String, String> getOyEWalletCode(){
        Map<String, String> immutableMap = Map.of("ovo", "ovo_ewallet",
                "shopeepay", "shopeepay_ewallet",
                "dana", "dana_ewallet");
        Map<String, String> bankCodes = new HashMap<>(immutableMap);
        return bankCodes;
    }

    @Override
    public Map<String, Object> getPaymentLinkRequestBody(Map<String, Object> requestBody){
        Map<String, Object> requestMap = new HashMap<>();

        String id = UUID.randomUUID().toString();

        int amount = ((Double) requestBody.get("amount")).intValue();
        String name = RequestBodyValidator.stringRequestBodyValidator(requestBody, "sender_name");
        String email = RequestBodyValidator.stringRequestBodyValidator(requestBody, "email");
        String description = RequestBodyValidator.stringRequestBodyValidator(requestBody, "title");

        requestMap.put("partner_tx_id", String.valueOf(id));
        requestMap.put("amount", amount);
        requestMap.put("sender_name", name);
        requestMap.put("email", email);
        requestMap.put("description",description);
        requestMap.put("id",id);
        return requestMap;
    }

    @Override
    public Map<String, Object> getRetailOutletRequestBody(Map<String, Object> requestBody){
        Map<String, Object> requestMap = new HashMap<>();

        String id = UUID.randomUUID().toString();
        int amount = ((Double) requestBody.get("amount")).intValue();
        String store = RequestBodyValidator.stringRequestBodyValidator(requestBody, "retail_outlet");

        requestMap.put("partner_trx_id", id);
        requestMap.put("customer_id", String.join("", id.split("-"))); // this is done because if the raw uuid is used here, there will be error from vendor
        requestMap.put("amount", amount);
        requestMap.put("transaction_type", "CASH_IN");
//		requestMap.put("offline_channel",store.toUpperCase());
        requestMap.put("offline_channel", store);
        requestMap.put("id",id);
        return requestMap;
    }

    @Override
    public Map<String, Object> getVirtualAccountRequestBody(Map<String, Object> requestBody){
        Map<String, Object> requestMap = new HashMap<>();
        String id = UUID.randomUUID().toString();

        int amount = ((Double) requestBody.get("amount")).intValue();
        String bank = RequestBodyValidator.stringRequestBodyValidator(requestBody, "bank");

        requestMap.put("partner_trx_id", id);
        requestMap.put("partner_user_id", id);
        requestMap.put("bank_code", getOyBankCode().get(bank));
        requestMap.put("amount", amount);
        requestMap.put("is_open", false);
        requestMap.put("id",id);
        return requestMap;
    }

    @Override
    public Map<String, Object> getEWalletRequestBody(Map<String, Object> requestBody){
        Map<String, Object> requestMap = new HashMap<>();

        String id = UUID.randomUUID().toString();

        String ewallet = RequestBodyValidator.stringRequestBodyValidator(requestBody, "ewallet_type");
        String phone = RequestBodyValidator.stringRequestBodyValidator(requestBody, "phone");
        int amount = ((Double) requestBody.get("amount")).intValue();
        String successRedirectUrl = RequestBodyValidator.stringRequestBodyValidator(requestBody, "success_redirect_url");

        requestMap.put("partner_trx_id", id);
        requestMap.put("customer_id", id);
        requestMap.put("amount", amount);
        requestMap.put("mobile_number",phone);
        requestMap.put("ewallet_code", getOyEWalletCode().get(ewallet.toLowerCase()));
        requestMap.put("success_redirect_url", successRedirectUrl);
        requestMap.put("id",id);
        return requestMap;
    }

    @Override
    public Map<String, Object> getInvoiceRequestBody(Map<String, Object> requestBody){
        Map<String, Object> requestMap = new HashMap<>();
        String id = UUID.randomUUID().toString();
       
        int amount = ((Double) requestBody.get("amount")).intValue();
        int quantity = RequestBodyValidator.intRequestBodyValidator(requestBody, "quantity");
        int pricePerItem = RequestBodyValidator.intRequestBodyValidator(requestBody, "price_per_item");

        if (pricePerItem * quantity != amount) {
            throw new BadRequestException(
                "Jumlah quantity dan price_per_item tidak sesuai dengan amount."
            );
        }
      
        Map<String, Object> invoiceMap = new HashMap<>();
        
        invoiceMap.put("quantity", quantity);
        invoiceMap.put("price_per_item", pricePerItem);
        
        List<Map<String, Object>> invoicesItems = new ArrayList<>();
        invoicesItems.add(invoiceMap);
        
        requestMap.put("partner_tx_id", id);
        requestMap.put("amount", amount);
        requestMap.put("invoice_items",invoicesItems);

        requestMap.put("id",id);
        return requestMap;
    }
    
    @Override
    public Map<String, Object> getPaymentRoutingRequestBody(Map<String, Object> requestBody){
        Map<String, Object> requestMap = new HashMap<>();

        String id = UUID.randomUUID().toString();
        int amount = ((Double) requestBody.get("amount")).intValue();
        int recipientAmountInt = RequestBodyValidator.intRequestBodyValidator(requestBody, "recipient_amount");
        String recipientAmount = String.valueOf(recipientAmountInt);
        String recipientAccount = RequestBodyValidator.stringRequestBodyValidator(requestBody, "recipient_account");
        String recipientBank = RequestBodyValidator.stringRequestBodyValidator(requestBody, "recipient_bank");
        String recipientBankCode = getOyBankCode().get(recipientBank);
        String recipientEmail = RequestBodyValidator.stringRequestBodyValidator(requestBody, "recipient_email");
        String recipientNote = RequestBodyValidator.stringRequestBodyValidator(requestBody, "recipient_note");

        requestMap.put("partner_trx_id", id);
        requestMap.put("partner_user_id", id);
        requestMap.put("need_frontend", true);
        
        Map<String, Object> routingMap = new HashMap<>();
        
        routingMap.put("recipient_account", recipientAccount);
        routingMap.put("recipient_bank", recipientBankCode);
        routingMap.put("recipient_amount", recipientAmount);
        routingMap.put("recipient_email", recipientEmail);
        routingMap.put("recipient_note", recipientNote);
        
        List<Map<String, Object>> routings = new ArrayList<>();
        routings.add(routingMap);
        
        requestMap.put("list_enable_sof", "002");
        requestMap.put("list_enable_payment_method", "VA");
        requestMap.put("need_frontend",true);
        requestMap.put("receive_amount",amount);
        requestMap.put("routings",routings);

        requestMap.put("id",id);
        return requestMap;
    }

    @Override
    public Map<String, Object> getPaymentLinkResponse(String rawResponse, String id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        
        if (!rawResponseMap.containsKey("payment_link_id")) {
            String errorMessageString = (String) rawResponseMap.get("message");
            throw new BadRequestException(errorMessageString);
        }
        String url = (String) rawResponseMap.get("url");
        String paymentLinkId = (String) rawResponseMap.get("payment_link_id");
        response.put("status", "CREATED");
        response.put("url", url);
        response.put("vendor_generated_id", paymentLinkId);
        response.put("id", id);
        return response;
    }

    @Override
    public Map<String, Object> getInvoiceResponse(String rawResponse, String id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        String transactionUrl = (String) rawResponseMap.get("url");
        String paymentLinkId = (String) rawResponseMap.get("payment_link_id");
        
        if (transactionUrl == null) {
            Map<String, Object> statusObject = (Map<String, Object>) rawResponseMap.get("status");
            String errorMessageString = (String) statusObject.get("message");
            throw new BadRequestException(errorMessageString);
        }

        response.put("status", "CREATED"); 
        response.put("url", transactionUrl);
        response.put("vendor_generated_id", paymentLinkId);
        response.put("id", id);
        return response;
    }

    @Override
    public Map<String, Object> getPaymentRoutingResponse(String rawResponse, String id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        
        if (!rawResponseMap.containsKey("trx_id")) {
            Map<String, Object> statusObject = (Map<String, Object>) rawResponseMap.get("status");
            String errorMessageString = (String) statusObject.get("message");
            throw new BadRequestException(errorMessageString);
        }
        
        Map<String, Object> paymentMap = (Map<String, Object>) rawResponseMap.get("payment_info");
        String url = (String) paymentMap.get("payment_checkout_url");
        String vendorGeneratedId = (String) rawResponseMap.get("trx_id");
        response.put("status", "CREATED");
        response.put("payment_checkout_url", url);
        response.put("vendor_generated_id", vendorGeneratedId);
        response.put("id", id);
        return response;
    }

    @Override
    public Map<String, Object> getRetailOutletResponse(String rawResponse, String id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        Map<String, Object> status = (Map<String, Object>) rawResponseMap.get("status");
        String statusMessage = (String) status.get("message");
        String retailPaymentCode = (String) rawResponseMap.get("code");
        if (retailPaymentCode == null) {
            throw new BadRequestException(statusMessage);
        }
        String vendorGeneratedId = (String) rawResponseMap.get("tx_id");
        response.put("status", statusMessage.toUpperCase());
        response.put("retail_payment_code", retailPaymentCode);
        response.put("vendor_generated_id", vendorGeneratedId);
        response.put("id", id);
        return response;
    }

    @Override
    public Map<String, Object> getEWalletResponse(String rawResponse, String id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);

        if (!rawResponseMap.containsKey("trx_id")) {
            Map<String, Object> statusObject = (Map<String, Object>) rawResponseMap.get("status");
            String errorMessageString = (String) statusObject.get("message");
            throw new BadRequestException(errorMessageString);
        }

        String ewalletTrxStatus = (String) rawResponseMap.get("ewallet_trx_status");
        String vendorGeneratedId = (String) rawResponseMap.get("trx_id");
        String paymentType = (String) rawResponseMap.get("ewallet_code");
        String url = (String) rawResponseMap.get("ewallet_url");
        response.put("status", ewalletTrxStatus);
        response.put("payment_type", paymentType);
        response.put("url", url);
        response.put("vendor_generated_id", vendorGeneratedId);
        response.put("id", id);
        return response;
    }

    @Override
    public Map<String, Object> getVirtualAccountResponse(String rawResponse, String id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);

        String vaNumber = (String) rawResponseMap.get("va_number");

        if (vaNumber == null) {
            Map<String, Object> statusObject = (Map<String, Object>) rawResponseMap.get("status");
            String errorMessageString = (String) statusObject.get("message");
            throw new BadRequestException(errorMessageString);
        }

        String vaStatus = (String) rawResponseMap.get("va_status");
        String vendorGeneratedId = (String) rawResponseMap.get("id");
        response.put("status", vaStatus);
        response.put("va_number", vaNumber);
        response.put("vendor_generated_id", vendorGeneratedId);
        response.put("id", id);
        return response;
    }

    @Override
    public HashMap<String, String> getHeaderParams() {
        HashMap<String, String> headerParams = new HashMap<>();
        String contentType = PropertiesReader.getProp(CONFIG_FILE, "content_type");
        String username = PropertiesReader.getProp(CONFIG_FILE, "api_username");
        String apikey = PropertiesReader.getProp(CONFIG_FILE, "authorization");
        headerParams.put("x-oy-username",username);
        headerParams.put("content-type",contentType);
        headerParams.put("x-api-key", apikey);
        return headerParams;
    }
}