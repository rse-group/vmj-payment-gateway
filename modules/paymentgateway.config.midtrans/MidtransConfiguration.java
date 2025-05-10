package paymentgateway.config.midtrans;

import paymentgateway.config.core.ConfigDecorator;
import paymentgateway.config.core.ConfigComponent;
import paymentgateway.config.core.PropertiesReader;
import paymentgateway.config.core.RequestBodyValidator;

import java.util.*;
import java.lang.reflect.Type;

import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.BadRequestException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MidtransConfiguration extends ConfigDecorator{
    private String CONFIG_FILE = "midtrans.properties";

    public MidtransConfiguration(ConfigComponent record) {
        super(record);
    }

    @Override
    public String getVendorName(){
        return "Midtrans";
    }

    @Override
    public String getPaymentDetailEndpoint(String configUrl,String Id){
        configUrl = configUrl.replace("[id]", Id);
        return configUrl;
    }    

    @Override
    public Map<String, Object> getPaymentStatusResponse(String rawResponse, String id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        
        System.out.println("rawResponseMap" + rawResponseMap);
        String status = rawResponseMap.get("transaction_status") != null
        	    ? (String) rawResponseMap.get("transaction_status")
        	    : (String) rawResponseMap.get("last_snap_transaction_status");
        
        if (status == null){
            response.put("status", rawResponseMap.get("status_message"));
            response.put("id", id);
            return response;
        }
        
        else{
            if (status.toLowerCase().equals(PaymentStatus.SETTLEMENT.getStatus()) || status.toLowerCase().equals(PaymentStatus.CAPTURE.getStatus())) {
                status = PaymentStatus.SUCCESSFUL.getStatus();
    
            }
            else if (status.toLowerCase().equals(PaymentStatus.CANCEL.getStatus())){
                status = PaymentStatus.CANCELLED.getStatus();
    
            }
            else if (status.toLowerCase().equals(PaymentStatus.FAIL.getStatus())){
                status = PaymentStatus.FAILED.getStatus();
            }
        }

        response.put("status", status);
        response.put("id", id);
        return response;
    }

    @Override
    public Map<String, Object> getCallbackPaymentRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> requestBody = vmjExchange.getPayload();
        String id = (String) requestBody.get("order_id");
        String status = (String) requestBody.get("transaction_status");

	    requestMap.put("id", id);
	    requestMap.put("status", status);
	    return requestMap;
    }
    

    @Override
    public Map<String, Object> getPaymentLinkRequestBody(Map<String, Object> requestBody){
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> customer_details = new HashMap<String, Object>();
        Map<String, Object> transaction_details = new HashMap<String, Object>();
        
        String id = UUID.randomUUID().toString();

        double amount = ((Double) requestBody.get("amount")).doubleValue();
        transaction_details.put("order_id", String.valueOf(id));
        transaction_details.put("gross_amount", amount);
        requestMap.put("transaction_details", transaction_details);
        requestMap.put( "customer_required",true);

        String name = RequestBodyValidator.stringRequestBodyValidator(requestBody, "sender_name");
        String email = RequestBodyValidator.stringRequestBodyValidator(requestBody, "email");
        String title = RequestBodyValidator.stringRequestBodyValidator(requestBody, "title");
        String[] arr = name.split(" ", 2);
        if(arr.length > 1){
            customer_details.put("first_name", arr[0]);
            customer_details.put("last_name", arr[1]);
        } else{
            customer_details.put("first_name", arr[0]);
        }
        customer_details.put("email",email);
        requestMap.put("costumer_details", customer_details);
        requestMap.put("title",title);
        requestMap.put("id",id);
        return requestMap;
    }

    @Override
    public Map<String, Object> getRetailOutletRequestBody(Map<String, Object> requestBody){
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> cstore = new HashMap<String, Object>();
        Map<String, Object> transaction_details = new HashMap<String, Object>();

        String id = UUID.randomUUID().toString();
        double amount = ((Double) requestBody.get("amount")).doubleValue();
        String store = RequestBodyValidator.stringRequestBodyValidator(requestBody, "retail_outlet");

        transaction_details.put("order_id", String.valueOf(id));
        transaction_details.put("gross_amount", amount);

        cstore.put("store", store);

        requestMap.put("payment_type", "cstore");
        requestMap.put("transaction_details", transaction_details);
        requestMap.put("cstore", cstore);
        requestMap.put("id",id);
        return requestMap;
    }

    @Override
    public Map<String, Object> getVirtualAccountRequestBody(Map<String, Object> requestBody){
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> bank_transfer = new HashMap<String, Object>();
        Map<String, Object> transaction_details = new HashMap<String, Object>();

        String id = UUID.randomUUID().toString();
        double amount = ((Double) requestBody.get("amount")).doubleValue();
        String bank = RequestBodyValidator.stringRequestBodyValidator(requestBody, "bank");

        transaction_details.put("order_id", String.valueOf(id));
        transaction_details.put("gross_amount", amount);

        bank_transfer.put("bank", bank);

        requestMap.put("payment_type", "bank_transfer");
        requestMap.put("transaction_details", transaction_details);
        requestMap.put("bank_transfer", bank_transfer);
        requestMap.put("id",id);
        return requestMap;
    }

    @Override
    public Map<String, Object> getEWalletRequestBody(Map<String, Object> requestBody){
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> customer_details = new HashMap<String, Object>();
        Map<String, Object> transaction_details = new HashMap<String, Object>();

        String id = UUID.randomUUID().toString();

        double amount = ((Double) requestBody.get("amount")).doubleValue();
        String ewallet = RequestBodyValidator.stringRequestBodyValidator(requestBody, "ewallet_type");
        String phone = RequestBodyValidator.stringRequestBodyValidator(requestBody, "phone");

        transaction_details.put("order_id", String.valueOf(id));
        transaction_details.put("gross_amount", amount);

        customer_details.put("phone", phone);

        requestMap.put("payment_type", ewallet);
        requestMap.put("transaction_details", transaction_details);
        requestMap.put("customer_details", customer_details);
        requestMap.put("id",id);
        return requestMap;
    }

    @Override
    public Map<String, Object> getCardRequestBody(Map<String, Object> requestBody){
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> credit_card = new HashMap<>();
        Map<String, Object> transaction_details = new HashMap<String, Object>();

        String id = UUID.randomUUID().toString();
        
        double amount = ((Double) requestBody.get("amount")).doubleValue();
        String token = (String) requestBody.get("token_id");

        transaction_details.put("order_id", String.valueOf(id));
        transaction_details.put("gross_amount", amount);

        credit_card.put("token_id",token);
        requestMap.put("credit_card",credit_card);

        requestMap.put("payment_type", "credit_card");
        requestMap.put("transaction_details", transaction_details);
        requestMap.put("id",id);
        return requestMap;
    }

    public String constructUrlParam(String serviceName, Map<String, Object> requestBody) {
        String baseUrl = (String) PropertiesReader.getProp(CONFIG_FILE, "base_url");
        String apiEndpoint = "";

        String cardNumber = RequestBodyValidator.stringRequestBodyValidator(requestBody, "card_number");
        String cardExpMonth = RequestBodyValidator.stringRequestBodyValidator(requestBody, "card_exp_month");
        String cardExpYear = RequestBodyValidator.stringRequestBodyValidator(requestBody, "card_exp_year");
        String cardCVV = RequestBodyValidator.stringRequestBodyValidator(requestBody, "card_cvv");
    
        // Determine the appropriate endpoint based on the service name
        if (serviceName.equals("CardToken")){
            apiEndpoint = (String) PropertiesReader.getProp(CONFIG_FILE, "token") 
                + "?client_key=" + PropertiesReader.getProp(CONFIG_FILE, "clientKey") 
                + "&card_number=" + cardNumber 
                + "&card_exp_month=" + cardExpMonth 
                + "&card_exp_year=" + cardExpYear 
                + "&card_cvv=" + cardCVV;
        }

        return baseUrl + apiEndpoint;
    }

    public Map<String, Object> getMidtransPayoutRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();

        int id = generateId();
        double amount = Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount"));
        return requestMap;
    }
    
    @Override
    public Map<String, Object> getPaymentLinkResponse(String rawResponse, String id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        String url = (String) rawResponseMap.get("payment_url");
        response.put("status", ""); // no status is provided in the request body
        response.put("vendor_generated_id", ""); // no transaction id provided in the request body
        response.put("url", url);
        response.put("id", id);
        return response;
    }

    @Override
    public Map<String, Object> getCardResponse(String rawResponse, String id){
        Map<String, Object> response = new HashMap<>();
        String status = "";
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        String statusCode = (String) rawResponseMap.get("status_code");
        
        if (!statusCode.equals("200") && !statusCode.equals("201")) {
            String errorMessageString = (String) rawResponseMap.get("status_message");
            throw new BadRequestException(errorMessageString);
        }
        
        String transactionStatus = (String) rawResponseMap.get("transaction_status");
        String vendorGeneratedId = (String) rawResponseMap.get("transaction_id");
        response.put("status", transactionStatus);
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
        String retailPaymentCode = (String) rawResponseMap.get("payment_code");
        if (retailPaymentCode == null) {
            String statusMessage = (String) rawResponseMap.get("status_message");
        	response.put("message", statusMessage);
            return response;
        }
        String transactionStatus = (String) rawResponseMap.get("transaction_status");
        String vendorGeneratedId = (String) rawResponseMap.get("transaction_id");
        response.put("status", transactionStatus);
        response.put("vendor_generated_id", vendorGeneratedId);
        response.put("retail_payment_code", retailPaymentCode);
        response.put("id", id);
        return response;
    }
    
    @Override
    public Map<String, Object> getEWalletResponse(String rawResponse, String id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        String paymentType = (String) rawResponseMap.get("payment_type");
        if (paymentType == null) {
            List<String> errorMessages = (List<String>) rawResponseMap.get("validation_messages");
			String errorMessageString = String.join(", ", errorMessages);
        	throw new BadRequestException(errorMessageString);
        }
        String transactionStatus = (String) rawResponseMap.get("transaction_status");
        List<Map<String, Object>> actions = (List<Map<String, Object>>) rawResponseMap.get("actions");
        String url = (String) actions.get(0).get("url");

        String vendorGeneratedId = (String) rawResponseMap.get("transaction_id");

        response.put("status", transactionStatus);
        response.put("vendor_generated_id", vendorGeneratedId);
        response.put("payment_type", paymentType);
        response.put("url", url);
        response.put("id", id);
        return response;
    }

    @Override
    public Map<String, Object> getVirtualAccountResponse(String rawResponse, String id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        String vaNumber = (String) rawResponseMap.get("permata_va_number");
        if (vaNumber == null) {
            List<Map<String, Object>> vaNums = (List<Map<String, Object>>) rawResponseMap.get("va_numbers");
            vaNumber = (String) vaNums.get(0).get("va_number");
        }
        String transactionStatus = (String) rawResponseMap.get("transaction_status");
        String vendorGeneratedId = (String) rawResponseMap.get("transaction_id");

        response.put("status", transactionStatus);
        response.put("vendor_generated_id", vendorGeneratedId);
        response.put("va_number", vaNumber);
        response.put("id", id);
        return response;
    }

    @Override
    public String getProductEnv(String serviceName){
        String url = "";
        String baseUrl = (String) PropertiesReader.getProp(CONFIG_FILE, "base_url");
        String apiEndpoint = "";
        if (serviceName.equals("PaymentLink")){
            apiEndpoint = (String) PropertiesReader.getProp(CONFIG_FILE, "paymentlink");
        } 
        else if (serviceName.equals("PaymentDetail")){
            apiEndpoint = (String) PropertiesReader.getProp(CONFIG_FILE, "paymentdetail");
        }
        else if (serviceName.equals("PaymentStatus")){
            apiEndpoint = (String) PropertiesReader.getProp(CONFIG_FILE, "paymentstatus");
        }
        else if (serviceName.equals("CardToken")){
            apiEndpoint = (String) PropertiesReader.getProp(CONFIG_FILE, "token") + "?client_key=" + PropertiesReader.getProp(CONFIG_FILE, "clientKey");
        }
        else {
            apiEndpoint = (String) PropertiesReader.getProp(CONFIG_FILE, "apiendpoint");
        }
        
        url = baseUrl + apiEndpoint;

        return url;
    }

    @Override
    public HashMap<String, String> getHeaderParams() {
        HashMap<String, String> headerParams = new HashMap<>();
        String contentType = PropertiesReader.getProp(CONFIG_FILE, "content_type");
        String accept = PropertiesReader.getProp(CONFIG_FILE, "accept");
        String auth = PropertiesReader.getProp(CONFIG_FILE, "authorization");
        headerParams.put("authorization",auth);
        headerParams.put("content-type",contentType);
        headerParams.put("accept", accept);
        return headerParams;
    }
}