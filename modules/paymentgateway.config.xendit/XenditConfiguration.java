package paymentgateway.config.xendit;

import paymentgateway.config.core.ConfigDecorator;
import paymentgateway.config.core.ConfigComponent;
import paymentgateway.config.core.PropertiesReader;
import paymentgateway.config.core.RequestBodyValidator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.lang.reflect.*;

import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.BadRequestException;

public class XenditConfiguration extends ConfigDecorator {
    private String CONFIG_FILE = "xendit.properties";
    private String CALLBACK_TOKEN_HEADER_NAME = "X-CALLBACK-TOKEN";

    public XenditConfiguration(ConfigComponent record) {
        super(record);
    }

    @Override
    public String getVendorName() {
        return "Xendit";
    }

    @Override
    public Map<String, Object> getCallbackDisbursementRequestBody(Map<String, Object> requestBody) {
        String webhookVerificationToken = PropertiesReader.getProp(CONFIG_FILE, "webhookVerificationToken");
        String callbackToken = (String) requestBody.get(CALLBACK_TOKEN_HEADER_NAME);
        if (!callbackToken.equals(webhookVerificationToken)) {
            throw new BadRequestException("Invalid callback token");
        }

        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> dataMap = (Map<String, Object>) requestBody.get("data");
        String id = (String) dataMap.get("reference_id");
        String status = (String) dataMap.get("status");

	    requestMap.put("id", id);
	    requestMap.put("status", status);
	    return requestMap;
    }

    @Override
    public Map<String, Object> getCallbackPaymentRequestBody(VMJExchange vmjExchange){
        String webhookVerificationToken = PropertiesReader.getProp(CONFIG_FILE, "webhookVerificationToken");
        String callbackToken = vmjExchange.getHttpExchange().getRequestHeaders().getFirst(CALLBACK_TOKEN_HEADER_NAME);
        if (!callbackToken.equals(webhookVerificationToken)) {
            throw new BadRequestException("Invalid callback token");
        }

        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> requestBody = vmjExchange.getPayload();
        Map<String, Object> dataMap = (Map<String, Object>) requestBody.get("data"); 
        String id = (String) dataMap.get("reference_id");
        String status = (String) dataMap.get("status");

	    requestMap.put("id", id);
	    requestMap.put("status", status);
	    return requestMap;
    }

    @Override
    public Map<String, Object> getDisbursementRequestBody(Map<String, Object> requestBody) {
        String id = UUID.randomUUID().toString();
        String vendor_name = RequestBodyValidator.stringRequestBodyValidator(requestBody, "vendor_name");
        String bank_code = RequestBodyValidator.stringRequestBodyValidator(requestBody, "bank_code");
        String account_number = RequestBodyValidator.stringRequestBodyValidator(requestBody, "account_number");
        String account_holder_name = RequestBodyValidator.stringRequestBodyValidator(requestBody,
                "account_holder_name");
        String currency = RequestBodyValidator.stringRequestBodyValidator(requestBody, "currency");
        double amount = RequestBodyValidator.doubleRequestBodyValidator(requestBody, "amount");

        DisbursementCurrency.validate(currency);

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("vendor_name", vendor_name);
        requestMap.put("channel_code", bank_code);
        requestMap.put("bank_code", bank_code);
        requestMap.put("account_number", account_number);
        requestMap.put("currency", currency);
        requestMap.put("amount", amount);

        Map<String, Object> channelPropertiesMap = new HashMap<>();
        channelPropertiesMap.put("account_holder_name", account_holder_name);
        channelPropertiesMap.put("account_number", account_number);
        requestMap.put("channel_properties", channelPropertiesMap);
        requestMap.put("reference_id", id);

        return requestMap;
    }

    @Override
    public String getPaymentDetailEndpoint(String configUrl, String id) {
        configUrl = configUrl.replace("[id]", id);
        return configUrl;
    }

    @Override
    public String getProductEnv(String serviceName) {
        return record.getProductEnv(CONFIG_FILE, serviceName);
    }

    @Override
    public Map<String, Object> getDisbursementResponse(String rawResponse, String id) {
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);

        if (rawResponseMap.containsKey("error_code")) {
            String message = (String) rawResponseMap.get("message");
            response.put("message", message);
            return response;
        }

        String referenceId = (String) rawResponseMap.get("reference_id");
        String userIdString = (String) rawResponseMap.get("business_id");
        String accountNumber = (String) ((Map<String, Object>) rawResponseMap.get("channel_properties"))
                .get("account_number");
        String accountHolderName = (String) ((Map<String, Object>) rawResponseMap.get("channel_properties"))
                .get("account_holder_name");
        double amount = ((Number) rawResponseMap.get("amount")).doubleValue();
        int userId = Integer.parseInt(userIdString.substring(0, 5).replaceAll("[^0-9]", ""));
        String vendorGeneratedId = (String) rawResponseMap.get("id");

        response.put("user_id", userId);
        response.put("id", referenceId);
        response.put("vendor_generated_id", vendorGeneratedId);
        response.put("amount", amount);
        response.put("account_number", accountNumber);
        response.put("account_holder_name", accountHolderName);
        response.put("status", rawResponseMap.get("status"));
        response.put("bank_code", rawResponseMap.get("channel_code"));
        response.put("currency", rawResponseMap.get("currency"));

        return response;
    }

    @Override
    public HashMap<String, String> getHeaderParams() {
        HashMap<String, String> xenditHeaderParams = new HashMap<>();
        String contentType = PropertiesReader.getProp(CONFIG_FILE, "content_type");
        String authorization = PropertiesReader.getProp(CONFIG_FILE, "authorization");
        xenditHeaderParams.put("Content-Type", contentType);
        xenditHeaderParams.put("idempotency-key", UUID.randomUUID().toString());
        xenditHeaderParams.put("X-TIMESTAMP", "");
        xenditHeaderParams.put("Authorization", authorization);
        return xenditHeaderParams;
    }

    @Override
    public Map<String, Object> getVirtualAccountRequestBody(Map<String, Object> requestBody) {
        // Reference:
        // https://developers.xendit.co/api-reference/payments-api/#virtual-account-creation
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> paymentMethod = new HashMap<String, Object>();
        Map<String, Object> virtualAccount = new HashMap<String, Object>();
        Map<String, Object> channelProperties = new HashMap<String, Object>();

        String id = UUID.randomUUID().toString();
        Double amountDouble = (Double) requestBody.get("amount");
        int amount = amountDouble.intValue();
        
        String bank = RequestBodyValidator.stringRequestBodyValidator(
                requestBody,
                "bank");
        String name = RequestBodyValidator.stringRequestBodyValidator(requestBody, "name");
        
        paymentMethod.put("reference_id", id);
        paymentMethod.put("reusability", "ONE_TIME_USE");
        paymentMethod.put("type", "VIRTUAL_ACCOUNT");

        virtualAccount.put("channel_code", bank.toUpperCase());
        channelProperties.put("customer_name", name);
        virtualAccount.put("channel_properties", channelProperties);

        paymentMethod.put("virtual_account", virtualAccount);
        
        requestMap.put("reference_id", id);
        requestMap.put("amount", amount);
        requestMap.put("currency", "IDR");
        requestMap.put("payment_method", paymentMethod);
        requestMap.put("id", id);

        return requestMap;
    }

    @Override
    public Map<String, Object> getVirtualAccountResponse(String rawResponse, String id) {
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);

        if (rawResponseMap.containsKey("error_code")) {
            String message = (String) rawResponseMap.get("message");
            throw new BadRequestException(message);
        }

        Map<String, Object> paymentMethod = (Map<String, Object>) rawResponseMap.get("payment_method");
        Map<String, Object> virtualAccount = (Map<String, Object>) paymentMethod.get("virtual_account");
        Map<String, Object> channelProperties = (Map<String, Object>) virtualAccount.get("channel_properties");

        String vaNumber = (String) channelProperties.get("virtual_account_number");
        String referenceId = (String) rawResponseMap.get("reference_id");
        String status = (String) rawResponseMap.get("status");
        String vendorGeneratedId = (String) rawResponseMap.get("id");
        
        response.put("status", status);
        response.put("va_number", vaNumber);
        response.put("vendor_generated_id", vendorGeneratedId);
        response.put("id", referenceId);
        
        return response;
    }

    @Override
    public Map<String, Object> getRetailOutletRequestBody(Map<String, Object> requestBody) {
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> paymentMethod = new HashMap<String, Object>();
        Map<String, Object> overTheCounter = new HashMap<String, Object>();
        Map<String, Object> channelProperties = new HashMap<String, Object>();

        String id = UUID.randomUUID().toString();
        Double amountDouble = (Double) requestBody.get("amount");
        int amount = amountDouble.intValue();
        String store = RequestBodyValidator.stringRequestBodyValidator(
                requestBody,
                "retail_outlet");

        String name = RequestBodyValidator.stringRequestBodyValidator(requestBody, "name");

        paymentMethod.put("reusability", "ONE_TIME_USE");
        paymentMethod.put("type", "OVER_THE_COUNTER");

        overTheCounter.put("channel_code", store.toUpperCase());
        channelProperties.put("customer_name", name);
        overTheCounter.put("channel_properties", channelProperties);

        paymentMethod.put("over_the_counter", overTheCounter);
        
        requestMap.put("reference_id", id);
        requestMap.put("amount", amount);
        requestMap.put("currency", "IDR");
        requestMap.put("country", "ID");
        requestMap.put("payment_method", paymentMethod);
        requestMap.put("id", id);
        return requestMap;
    }

    @Override
    public Map<String, Object> getRetailOutletResponse(String rawResponse, String id) {
		Map<String, Object> response = new HashMap<>();
	    Gson gson = new Gson();
	    Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
	    Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
	    
        if (rawResponseMap.containsKey("error_code")) {
            String message = (String) rawResponseMap.get("message");
            response.put("message", message);
            return response;
        }

        Map<String, Object> paymentMethod = (Map<String, Object>) rawResponseMap.get("payment_method");
        Map<String, Object> overTheCounter = (Map<String, Object>) paymentMethod.get("over_the_counter");
        Map<String, Object> channelProperties = (Map<String, Object>) overTheCounter.get("channel_properties");

        String retailPaymentCode = (String) channelProperties.get("payment_code");
        String referenceId = (String) rawResponseMap.get("reference_id");
        
        if (retailPaymentCode == null) {
            Map<String, Object> status = (Map<String, Object>) rawResponseMap.get("status");
            String statusMessage = (String) status.get("message");
            response.put("message", statusMessage);
            return response;
        }

        String status = (String) rawResponseMap.get("status");
        String vendorGeneratedId = (String) rawResponseMap.get("id");
        
        response.put("status", status);
        response.put("retail_payment_code", retailPaymentCode);
        response.put("vendor_generated_id", vendorGeneratedId);
        response.put("id", id);

        return response;
    }

    @Override
    public Map<String, Object> getQRCodeRequestBody(Map<String, Object> requestBody) {
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> paymentMethod = new HashMap<String, Object>();
        Map<String, Object> qrCode = new HashMap<String, Object>();
        Map<String, Object> channelProperties = new HashMap<String, Object>();

        String id = UUID.randomUUID().toString();
        // String amountStr = RequestBodyValidator.stringRequestBodyValidator(
        // requestBody,
        // "amount");
        int amount = ((Double) requestBody.get("amount")).intValue();

        String uuidString = UUID.randomUUID().toString().replace("-", "");
        int uniqueInteger = Math.abs(uuidString.hashCode()) % 100000;

        paymentMethod.put("reusability", "ONE_TIME_USE");
        paymentMethod.put("type", "QR_CODE");

        // Does qr code needs its body?

        paymentMethod.put("qr_code", qrCode);

        requestMap.put("reference_id", id);
        requestMap.put("amount", amount);
        requestMap.put("currency", "IDR");
        requestMap.put("country", "ID");
        requestMap.put("payment_method", paymentMethod);
        requestMap.put("id", id);
        return requestMap;
    }

    @Override
    public Map<String, Object> getQRCodeResponse(String rawResponse, String id) {
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);

        if (rawResponseMap.containsKey("error_code")) {
            String message = (String) rawResponseMap.get("message");
            response.put("message", message);
            return response;
        }

        Map<String, Object> paymentMethod = (Map<String, Object>) rawResponseMap.get("payment_method");
        Map<String, Object> qrCode = (Map<String, Object>) paymentMethod.get("qr_code");

        String channelCode = (String) qrCode.get("channel_code");
        response.put("channel_code", channelCode);

        Map<String, Object> channelProperties = (Map<String, Object>) qrCode.get("channel_properties");

        String qrCodeString = (String) channelProperties.get("qr_string");
        String expiryDateString = (String) channelProperties.get("expires_at");

        // String referenceId = (String) rawResponseMap.get("reference_id");
        String status = (String) rawResponseMap.get("status");
        String vendorGeneratedId = (String) rawResponseMap.get("id");

        response.put("qr_code_string", qrCodeString);
        response.put("expires_at", expiryDateString);
        response.put("vendor_generated_id", vendorGeneratedId);
        response.put("status", status);
        response.put("id", id);
        if (qrCodeString == null) {
            Map<String, Object> statusMap = (Map<String, Object>) rawResponseMap.get("status");
            String statusMessage = (String) statusMap.get("message");
            response.put("message", statusMessage);
            return response;
        }

        return response;
    }

    @Override
    public Map<String, Object> getEWalletRequestBody(Map<String, Object> requestBody) {
        // Note: currently this supports the following ewallet:
        // DANA, OVO, LINKAJA, ASTRAPAY, JENIUSPAY, SHOPEEPAY, SAKUKU
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> paymentMethod = new HashMap<String, Object>();
        Map<String, Object> ewalletDetailsMap = new HashMap<String, Object>();
        Map<String, Object> channelProperties = new HashMap<String, Object>();
        Map<String, Object> customerDetailsMap = new HashMap<String, Object>();
        Map<String, Object> individualDetailsMap = new HashMap<String, Object>();

        String id = UUID.randomUUID().toString();
        Double amountDouble = (Double) requestBody.get("amount");
        int amount = amountDouble.intValue();
        
        String ewalletType = RequestBodyValidator.stringRequestBodyValidator(requestBody, "ewallet_type");
        String name = RequestBodyValidator.stringRequestBodyValidator(requestBody, "name");
        String email = RequestBodyValidator.stringRequestBodyValidator(requestBody, "email");
        String phone = RequestBodyValidator.stringRequestBodyValidator(requestBody, "phone");
        
        paymentMethod.put("reusability", "ONE_TIME_USE");
        paymentMethod.put("type", "EWALLET");
        
        ewalletDetailsMap.put("channel_code", ewalletType);
        if (ewalletType.equals("DANA") || ewalletType.equals("LINKAJA") || ewalletType.equals("SHOPEEPAY") || ewalletType.equals("ASTRAPAY")) {
            // required for DANA, LINKAJA, SHOPEEPAY, ASTRAPAY if reusability is ONE_TIME_USE
            String successReturnUrl = RequestBodyValidator.stringRequestBodyValidator(requestBody, "success_return_url");
            channelProperties.put("success_return_url", successReturnUrl);
        }
        if (ewalletType.equals("ASTRAPAY")) {
            // required for ASTRAPAY
            String failureReturnUrl = RequestBodyValidator.stringRequestBodyValidator(requestBody, "failure_return_url");
            channelProperties.put("failure_return_url", failureReturnUrl);
        }
        if (ewalletType.equals("JENIUSPAY")) {
            // required for JENIUSPAY if reusability is ONE_TIME_USE
            String cashtag = RequestBodyValidator.stringRequestBodyValidator(requestBody, "cashtag");
            channelProperties.put("cashtag", cashtag);
        }
        if (ewalletType.equals("OVO")) {
            // required for OVO if reusability is ONE_TIME_USE
            channelProperties.put("mobile_number", phone);
        }
        ewalletDetailsMap.put("channel_properties", channelProperties);
        
        paymentMethod.put("ewallet", ewalletDetailsMap);
                
        customerDetailsMap.put("reference_id", id);
        customerDetailsMap.put("type", "INDIVIDUAL");
        individualDetailsMap.put("given_names", name);
        customerDetailsMap.put("individual_detail", individualDetailsMap);
        customerDetailsMap.put("email", email);
        customerDetailsMap.put("mobile_number", phone);
        
        requestMap.put("reference_id", id);
        requestMap.put("amount", amount);
        requestMap.put("currency", "IDR"); // currently handling only for ID ewallet
        requestMap.put("payment_method", paymentMethod);
        if (ewalletType.equals("SHOPEEPAY")) {
            // required for SHOPEEPAY
            // (currently handling only for ID ewallet)
            requestMap.put("country", "ID");
        }
        requestMap.put("customer", customerDetailsMap);
        requestMap.put("id", id);

        return requestMap;
    }

    @Override
    public Map<String, Object> getEWalletResponse(String rawResponse, String id) {
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);

        if (rawResponseMap.containsKey("error_code")) {
        	String errorMessage = (String) rawResponseMap.get("message");
            throw new BadRequestException(errorMessage);
        }
        
        Map<String, Object> paymentMethod = (Map<String, Object>) rawResponseMap.get("payment_method");
        Map<String, Object> ewalletDetailsMap = (Map<String, Object>) paymentMethod.get("ewallet");
        String paymentType = (String) ewalletDetailsMap.get("channel_code");
        String status = (String) rawResponseMap.get("status");
        String vendorGeneratedId = (String) rawResponseMap.get("id");

        String referenceId = (String) rawResponseMap.get("reference_id");
        response.put("status", status);
        response.put("url", "");
        response.put("payment_type", paymentType);
        response.put("vendor_generated_id", vendorGeneratedId);
        response.put("id", referenceId);
        
        return response;
    }

    @Override
    public Map<String, Object> getDirectDebitRequestBody(Map<String, Object> requestBody){
        // Note: currently this supports the following direct debit:
        // MANDIRI, BRI
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> paymentMethod = new HashMap<String, Object>();
        Map<String, Object> directDebitMap = new HashMap<String, Object>();
        Map<String, Object> customerDetailsMap = new HashMap<String, Object>();
        Map<String, Object> individualDetailsMap = new HashMap<String, Object>();

        String id = UUID.randomUUID().toString();
        Double amountDouble = (Double) requestBody.get("amount");
        int amount = amountDouble.intValue();
        String bank = RequestBodyValidator.stringRequestBodyValidator(
            requestBody,
            "bank"
        );
        
        String name = RequestBodyValidator.stringRequestBodyValidator(requestBody, "name");
        String email = RequestBodyValidator.stringRequestBodyValidator(requestBody, "email");
        String phone = RequestBodyValidator.stringRequestBodyValidator(requestBody, "phone");

        paymentMethod.put("reusability", "ONE_TIME_USE");
        paymentMethod.put("type", "DIRECT_DEBIT");
        
        directDebitMap.put("channel_code", bank);
        Map<String, Object> channelProperties = handleDirectDebitChannelProperties(requestBody, bank, phone);
        directDebitMap.put("channel_properties", channelProperties);

        customerDetailsMap.put("reference_id", id);
        customerDetailsMap.put("type", "INDIVIDUAL");
        individualDetailsMap.put("given_names", name);
        customerDetailsMap.put("individual_detail", individualDetailsMap);
        customerDetailsMap.put("email", email);
        customerDetailsMap.put("mobile_number", phone);
        
        paymentMethod.put("direct_debit", directDebitMap);
        
        requestMap.put("reference_id", id);
        requestMap.put("amount", amount);
        requestMap.put("currency", "IDR");
        requestMap.put("payment_method", paymentMethod);
        requestMap.put("customer", customerDetailsMap);
        requestMap.put("id", id);
        return requestMap;
    }

    @Override
    public Map<String, Object> getDirectDebitResponse(String rawResponse, String id){
		Map<String, Object> response = new HashMap<>();
	    Gson gson = new Gson();
	    Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
	    Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
	    
        if (rawResponseMap.containsKey("error_code")) {
        	String message = (String) rawResponseMap.get("message");
            throw new BadRequestException(message);
        }
        
        Map<String, Object> paymentMethodMap = (Map<String, Object>) rawResponseMap.get("payment_method");
        Map<String, Object> directDebitMap = (Map<String, Object>) paymentMethodMap.get("direct_debit");
        Map<String, Object> channelProperties = (Map<String, Object>) directDebitMap.get("channel_properties");

        String paymentType = (String) channelProperties.get("channel_code");
        String status = (String) rawResponseMap.get("status");

        List<Map<String, Object>> actionsArray = (List<Map<String, Object>>) rawResponseMap.get("actions");

        if (actionsArray == null) {
        	Map<String, Object> statusMap = (Map<String, Object>) rawResponseMap.get("status");
        	String statusMessage = (String) statusMap.get("message");
        	response.put("message", statusMessage);
            return response;
        }

        String directDebitUrl = null;

        for (Map<String, Object> actionMap : actionsArray) {
            String action = (String) actionMap.get("action");
            if (action.equals("AUTH")) {
                directDebitUrl = (String) actionMap.get("url");
            }
        }

        if (directDebitUrl == null) {
        	Map<String, Object> statusMap = (Map<String, Object>) rawResponseMap.get("status");
        	String statusMessage = (String) statusMap.get("message");
        	response.put("message", statusMessage);
            return response;
        }

        String referenceId = (String) rawResponseMap.get("reference_id");
        String vendorGeneratedId = (String) rawResponseMap.get("id");
        
        response.put("payment_type", paymentType);
        response.put("status", status);
        response.put("direct_debit_url", directDebitUrl);
        response.put("vendor_generated_id", vendorGeneratedId);
        response.put("id", referenceId);
        
        return response;
    }

    private Map<String, Object> handleDirectDebitChannelProperties(Map<String, Object> requestBody, String bank, String phone) {
        Map<String, Object> channelProperties = new HashMap<String, Object>();
        if (bank.equals("MANDIRI")) {
            // required for MANDIRI
            String successReturnUrl = RequestBodyValidator.stringRequestBodyValidator(requestBody, "success_return_url");
            String failureReturnUrl = RequestBodyValidator.stringRequestBodyValidator(requestBody, "failure_return_url");
            channelProperties.put("success_return_url", successReturnUrl);
            channelProperties.put("failure_return_url", failureReturnUrl);
        }
        if (bank.equals("BRI")) {
            // required for BRI
            String cardLastFour = RequestBodyValidator.stringRequestBodyValidator(requestBody, "card_last_four");
            channelProperties.put("mobile_number", phone);
            channelProperties.put("card_last_four", cardLastFour);
        }
        return channelProperties;
    }
}