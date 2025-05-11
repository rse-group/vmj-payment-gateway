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

    public XenditConfiguration(ConfigComponent record) {
        super(record);
    }

    @Override
    public String getVendorName() {
        return "Xendit";
    }

    @Override
    public Map<String, Object> getCallbackDisbursementRequestBody(Map<String, Object> requestBody) {
        Map<String, Object> requestMap = new HashMap<>();

        // TODO: implement disbursement callback handler

        return requestMap;
    }

    @Override
    public Map<String, Object> getDisbursementRequestBody(Map<String, Object> requestBody) {
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

        String uuidString = UUID.randomUUID().toString().replace("-", "");
        int uniqueInteger = Math.abs(uuidString.hashCode()) % 100000;
        requestMap.put("reference_id", String.format("%05d", uniqueInteger));

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
    public Map<String, Object> getDisbursementResponse(String rawResponse) {
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

        String idString = (String) rawResponseMap.get("reference_id");
        String userIdString = (String) rawResponseMap.get("business_id");
        String accountNumber = (String) ((Map<String, Object>) rawResponseMap.get("channel_properties"))
                .get("account_number");
        String accountHolderName = (String) ((Map<String, Object>) rawResponseMap.get("channel_properties"))
                .get("account_holder_name");
        double amount = ((Number) rawResponseMap.get("amount")).doubleValue();

        int id = Integer.parseInt(idString);
        int userId = Integer.parseInt(userIdString.substring(0, 5).replaceAll("[^0-9]", ""));

        response.put("user_id", userId);
        response.put("id", id);
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

        int id = generateId();
        String amountStr = RequestBodyValidator.stringRequestBodyValidator(
                requestBody,
                "amount");
        double amount = Double.parseDouble(amountStr);

        String bank = RequestBodyValidator.stringRequestBodyValidator(
                requestBody,
                "bank");
        String name = RequestBodyValidator.stringRequestBodyValidator(requestBody, "name");

        String uuidString = UUID.randomUUID().toString().replace("-", "");
        int uniqueInteger = Math.abs(uuidString.hashCode()) % 100000;

        paymentMethod.put("reference_id", String.format("%05d", uniqueInteger));
        paymentMethod.put("reusability", "ONE_TIME_USE");
        paymentMethod.put("type", "VIRTUAL_ACCOUNT");

        virtualAccount.put("channel_code", bank.toUpperCase());
        channelProperties.put("customer_name", name);
        virtualAccount.put("channel_properties", channelProperties);

        paymentMethod.put("virtual_account", virtualAccount);

        requestMap.put("reference_id", String.format("%05d", uniqueInteger));
        requestMap.put("amount", amount);
        requestMap.put("currency", "IDR");
        requestMap.put("payment_method", paymentMethod);
        requestMap.put("id", id);

        return requestMap;
    }

    @Override
    public Map<String, Object> getVirtualAccountResponse(String rawResponse, int id) {
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
        Map<String, Object> virtualAccount = (Map<String, Object>) paymentMethod.get("virtual_account");
        Map<String, Object> channelProperties = (Map<String, Object>) virtualAccount.get("channel_properties");

        String vaNumber = (String) channelProperties.get("virtual_account_number");
        String Id = (String) rawResponseMap.get("reference_id");
        response.put("va_number", vaNumber);
        response.put("id", Integer.parseInt(Id));

        return response;
    }

    @Override
    public Map<String, Object> getRetailOutletRequestBody(Map<String, Object> requestBody) {
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> paymentMethod = new HashMap<String, Object>();
        Map<String, Object> overTheCounter = new HashMap<String, Object>();
        Map<String, Object> channelProperties = new HashMap<String, Object>();

        int id = generateId();
        String amountStr = RequestBodyValidator.stringRequestBodyValidator(
                requestBody,
                "amount");
        int amount = Integer.parseInt(amountStr);
        String store = RequestBodyValidator.stringRequestBodyValidator(
                requestBody,
                "retail_outlet");

        String name = RequestBodyValidator.stringRequestBodyValidator(requestBody, "name");

        String uuidString = UUID.randomUUID().toString().replace("-", "");
        int uniqueInteger = Math.abs(uuidString.hashCode()) % 100000;

        paymentMethod.put("reusability", "ONE_TIME_USE");
        paymentMethod.put("type", "OVER_THE_COUNTER");

        overTheCounter.put("channel_code", store.toUpperCase());
        channelProperties.put("customer_name", name);
        overTheCounter.put("channel_properties", channelProperties);

        paymentMethod.put("over_the_counter", overTheCounter);

        requestMap.put("reference_id", String.format("%05d", uniqueInteger));
        requestMap.put("amount", amount);
        requestMap.put("currency", "IDR");
        requestMap.put("country", "ID");
        requestMap.put("payment_method", paymentMethod);
        requestMap.put("id", id);
        return requestMap;
    }

    @Override
    public Map<String, Object> getRetailOutletResponse(String rawResponse, int id) {
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
        Map<String, Object> overTheCounter = (Map<String, Object>) paymentMethod.get("over_the_counter");
        Map<String, Object> channelProperties = (Map<String, Object>) overTheCounter.get("channel_properties");

        String retailPaymentCode = (String) channelProperties.get("payment_code");
        String Id = (String) rawResponseMap.get("reference_id");
        response.put("retail_payment_code", retailPaymentCode);
        response.put("id", Integer.parseInt(Id));

        if (retailPaymentCode == null) {
            Map<String, Object> status = (Map<String, Object>) rawResponseMap.get("status");
            String statusMessage = (String) status.get("message");
            response.put("message", statusMessage);
            return response;
        }

        response.put("retail_payment_code", retailPaymentCode);
        response.put("id", id);

        return response;
    }

    @Override
    public Map<String, Object> getQRCodeRequestBody(Map<String, Object> requestBody) {
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> paymentMethod = new HashMap<String, Object>();
        Map<String, Object> qrCode = new HashMap<String, Object>();
        Map<String, Object> channelProperties = new HashMap<String, Object>();

        int id = generateId();
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

        requestMap.put("reference_id", String.format("%05d", uniqueInteger));
        requestMap.put("amount", amount);
        requestMap.put("currency", "IDR");
        requestMap.put("country", "ID");
        requestMap.put("payment_method", paymentMethod);
        requestMap.put("id", id);
        return requestMap;
    }

    @Override
    public Map<String, Object> getQRCodeResponse(String rawResponse, int id) {
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

        String Id = (String) rawResponseMap.get("reference_id");
        response.put("qr_string", qrCodeString);
        response.put("expires_at", expiryDateString);

        response.put("id", id);
        if (qrCodeString == null) {
            Map<String, Object> status = (Map<String, Object>) rawResponseMap.get("status");
            String statusMessage = (String) status.get("message");
            response.put("message", statusMessage);
            return response;
        }

        return response;
    }
}