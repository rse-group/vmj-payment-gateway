package paymentgateway.config.flip;

import paymentgateway.config.core.ConfigDecorator;
import paymentgateway.config.core.ConfigComponent;
import paymentgateway.config.core.PropertiesReader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.lang.reflect.*;

import vmj.routing.route.VMJExchange;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FlipConfiguration extends ConfigDecorator{

    public FlipConfiguration(ConfigComponent record) {
        super(record);
    }

    @Override
    public String getProductName(){
        return "Flip";
    }

//     public Map<String, Object> getMoneyTransferRequestBody(VMJExchange vmjExchange){
//         Map<String, Object> requestMap = new HashMap<>();

//         String number = (String) vmjExchange.getRequestBodyForm("account_number");
//         String code = (String) vmjExchange.getRequestBodyForm("bank_code");
//         String amount = (String) vmjExchange.getRequestBodyForm("amount");

//         requestMap.put("account_number", number);
//         requestMap.put("bank_code", code);
//         requestMap.put("amount", amount);
//         return vmjExchange.getPayload();
//     }

//     public Map<String, Object> getSpecialMoneyTransferRequestBody(VMJExchange vmjExchange){
//         Map<String, Object> requestMap = new HashMap<>();

//         String number = (String) vmjExchange.getRequestBodyForm("account_number");
//         String code = (String) vmjExchange.getRequestBodyForm("bank_code");
//         String amount = (String) vmjExchange.getRequestBodyForm("amount");
//         String name = (String) vmjExchange.getRequestBodyForm("sender_name");
//         String address = (String) vmjExchange.getRequestBodyForm("sender_address");
//         int country = Integer.parseInt((String) vmjExchange.getRequestBodyForm("sender_country"));
//         String job = (String) vmjExchange.getRequestBodyForm("sender_job");
//         String direction = (String) vmjExchange.getRequestBodyForm("direction");

//         requestMap.put("account_number", number);
//         requestMap.put("bank_code", code);
//         requestMap.put("amount", amount);
//         requestMap.put("sender_name", name);
//         requestMap.put("sender_address", address);
//         requestMap.put("sender_country", country);
//         requestMap.put("sender_job", job);
//         requestMap.put("direction", direction);
//         return vmjExchange.getPayload();
//     }

//     public Map<String, Object> getInternationalMoneyTransferRequestBody(VMJExchange vmjExchange){
//         Map<String, Object> requestMap = new HashMap<>();

//         String amount = (String) vmjExchange.getRequestBodyForm("amount");
//         String source = (String) vmjExchange.getRequestBodyForm("source_country");
//         String destination = (String) vmjExchange.getRequestBodyForm("destination_country");
//         String beneficiaryFullName = (String) vmjExchange.getRequestBodyForm("beneficiary_full_name");
//         String beneficiaryAccountNumber = (String) vmjExchange.getRequestBodyForm("beneficiary_account_number");
//         String beneficiaryBankId = (String) vmjExchange.getRequestBodyForm("beneficiary_bank_id");
//         String beneficiaryBankName = (String) vmjExchange.getRequestBodyForm("beneficiary_bank_name");
// //        String beneficiaryEmail = (String) vmjExchange.getRequestBodyForm("beneficiary_email");
//         String beneficiaryMsisdn = (String) vmjExchange.getRequestBodyForm("beneficiary_msisdn");
//         String beneficiaryNationality = (String) vmjExchange.getRequestBodyForm("beneficiary_nationality");
//         String beneficiaryProvince = (String) vmjExchange.getRequestBodyForm("beneficiary_province");
//         String beneficiaryCity = (String) vmjExchange.getRequestBodyForm("beneficiary_city");
// //        String beneficiaryPostalCode = (String) vmjExchange.getRequestBodyForm("beneficiary_postal_code");
//         String beneficiaryRelationship = (String) vmjExchange.getRequestBodyForm("beneficiary_relationship");
//         String beneficiarySourceOfFunds = (String) vmjExchange.getRequestBodyForm("beneficiary_source_of_funds");
//         String beneficiaryRemittancePurposes = (String) vmjExchange.getRequestBodyForm("beneficiary_remittance_purposes");
// //        String beneficiarySortCode = (String) vmjExchange.getRequestBodyForm("beneficiary_sort_code");
//         String senderName = (String) vmjExchange.getRequestBodyForm("sender_name");
//         int senderCountry = Integer.parseInt((String)vmjExchange.getRequestBodyForm("sender_country"));
//         int senderPlaceOfBirth = Integer.parseInt((String) vmjExchange.getRequestBodyForm("sender_place_of_birth"));
//         String senderDateOfBirth = (String) vmjExchange.getRequestBodyForm("sender_date_of_birth");
//         String senderAddress = (String) vmjExchange.getRequestBodyForm("sender_address");
//         String senderIdentityType = (String) vmjExchange.getRequestBodyForm("sender_identity_type");
//         String senderIdentityNumber = (String) vmjExchange.getRequestBodyForm("sender_identity_number");
//         String senderJob = (String) vmjExchange.getRequestBodyForm("sender_job");
//         String senderEmail = (String) vmjExchange.getRequestBodyForm("sender_email");
//         String senderCity = (String) vmjExchange.getRequestBodyForm("sender_city");
//         String senderPhoneNumber = (String) vmjExchange.getRequestBodyForm("sender_phone_number");
// //        String beneficiaryRegion = (String) vmjExchange.getRequestBodyForm("beneficiary_region");


//         requestMap.put("amount", amount);
//         requestMap.put("source_country", source);
//         requestMap.put("destination_country", destination);
//         requestMap.put("beneficiary_full_name", beneficiaryFullName);
//         requestMap.put("beneficiary_account_number", beneficiaryAccountNumber);
//         requestMap.put("beneficiary_bank_id", beneficiaryBankId);
//         requestMap.put("beneficiary_bank_name", beneficiaryBankId);
// //        requestMap.put("beneficiary_email", beneficiaryEmail);
//         requestMap.put("beneficiary_msisdn", beneficiaryMsisdn);
//         requestMap.put("beneficiary_nationality", beneficiaryNationality);
//         requestMap.put("beneficiary_province", beneficiaryProvince);
//         requestMap.put("beneficiary_city", beneficiaryCity);
// //        requestMap.put("beneficiary_postal_code", beneficiaryPostalCode);
//         requestMap.put("beneficiary_relationship", beneficiaryRelationship);
//         requestMap.put("beneficiary_source_of_funds", beneficiarySourceOfFunds);
//         requestMap.put("beneficiary_remittance_purposes", beneficiaryRemittancePurposes);
// //        requestMap.put("beneficiary_sort_code", beneficiarySortCode);
//         requestMap.put("sender_name", senderName);
//         requestMap.put("sender_country", senderCountry);
//         requestMap.put("sender_place_of_birth", senderPlaceOfBirth);
//         requestMap.put("sender_date_of_birth", senderDateOfBirth);
//         requestMap.put("sender_address", senderAddress);
//         requestMap.put("sender_identity_type", senderIdentityType);
//         requestMap.put("sender_identity_number", senderIdentityNumber);
//         requestMap.put("sender_job", senderJob);
//         requestMap.put("sender_email", senderEmail);
//         requestMap.put("sender_city", senderCity);
//         requestMap.put("sender_phone_number", senderPhoneNumber);
// //        requestMap.put("beneficiary_region", beneficiaryRegion);
//         requestMap.put("transaction_type", "C2C");
//         return vmjExchange.getPayload();
//     }

//     public Map<String, Object> getAgentMoneyTransferRequestBody(VMJExchange vmjExchange){
//         Map<String, Object> requestMap = new HashMap<>();

//         String id = (String) vmjExchange.getRequestBodyForm("agent_id");
//         String number = (String) vmjExchange.getRequestBodyForm("account_number");
//         String code = (String) vmjExchange.getRequestBodyForm("bank_code");
//         String amount = (String) vmjExchange.getRequestBodyForm("amount");
//         String direction = (String) vmjExchange.getRequestBodyForm("direction");

//         requestMap.put("agent_id", id);
//         requestMap.put("account_number", number);
//         requestMap.put("bank_code", code);
//         requestMap.put("amount", amount);
//         requestMap.put("direction", direction);
//         return vmjExchange.getPayload();
//     }

    // public String getParamsUrlEncoded(VMJExchange vmjExchange) {
    //     ArrayList<String> paramList = new ArrayList<>();
    //     for (Map.Entry<String, Object> entry : vmjExchange.getPayload().entrySet()) {
    //         String key = entry.getKey();
    //         Object val = entry.getValue();
    //         if (val instanceof String) {
    //             paramList.add(key + "=" + URLEncoder.encode(val.toString(), StandardCharsets.UTF_8));
    //         } else if (val instanceof Integer) {
    //             paramList.add(key + "=" + URLEncoder.encode(val.toString(), StandardCharsets.UTF_8));
    //         } else if (val instanceof Double) {
    //             int temp = ((Double) val).intValue();
    //             paramList.add(key + "=" + URLEncoder.encode(Integer.toString(temp), StandardCharsets.UTF_8));
    //         }

    //     }
    //     String encodedURL = String.join("&",paramList);
    //     return encodedURL;
    // }

    @Override
    public Map<String, Object> getMoneyTransferResponse(String rawResponse){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        
        int id = ((Double) rawResponseMap.get("id")).intValue();
        System.out.println(rawResponseMap.get("user_id"));
        int userId = ((Double) rawResponseMap.get("user_id")).intValue();
        String status = (String) rawResponseMap.get("status");
        response.put("status", status);
        response.put("user_id", userId);
        response.put("id", id);
        return response;
    }

    @Override
    public Map<String, Object> getAgentMoneyTransferResponse(String rawResponse){
        Map<String, Object> response = getMoneyTransferResponse(rawResponse);
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        int agentId = ((Double) rawResponseMap.get("agent_id")).intValue();
        String direction = (String) rawResponseMap.get("direction");
        response.put("agent_id", agentId);
        response.put("direction", direction);
        return response;
    }

    @Override
    public Map<String, Object> getSpecialMoneyTransferResponse(String rawResponse){
        Map<String, Object> response = getMoneyTransferResponse(rawResponse);
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        Map<String, Object> senderMap = (Map<String, Object>) rawResponseMap.get("sender");
        String senderName = (String) senderMap.get("sender_name");
        String senderAddress = (String) senderMap.get("sender_address");
        int senderCountry = ((Double) senderMap.get("sender_country")).intValue();;
        String senderJob = (String) senderMap.get("sender_job");

        String direction = (String) rawResponseMap.get("direction");
        response.put("direction", direction);
        response.put("name", senderName);
        response.put("address", senderAddress);
        response.put("country", senderCountry);
        response.put("job", senderJob);
        return response;
    }

    @Override
    public Map<String, Object> getInternationalMoneyTransferResponse(String rawResponse){
        Map<String, Object> response = getMoneyTransferResponse(rawResponse);
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        double exchangeRate = (double) rawResponseMap.get("exchange_rate");
        double fee = (double) rawResponseMap.get("fee");
        double amount = (double) rawResponseMap.get("amount");
        String sourceCountry = (String) rawResponseMap.get("source_country");
        String destinationCountry = (String) rawResponseMap.get("destination_country");
        String beneficiaryCurrencyCode = (String) rawResponseMap.get("beneficiary_currency_code");

        response.put("exchange_rate", exchangeRate);
        response.put("fee", fee);
        response.put("amount", amount);
        response.put("source_country", sourceCountry);
        response.put("destination_country", destinationCountry);
        response.put("beneficiary_currency_code", beneficiaryCurrencyCode);
        return response;
    }

    @Override
    public Map<String, Object> getVirtualAccountRequestBody(VMJExchange vmjExchange){
        int id = generateId();
        Map<String, Object> requestMap = new HashMap<>();
        String title = (String) vmjExchange.getRequestBodyForm("title");
        String type = (String) vmjExchange.getRequestBodyForm("type");
        int amount = Integer.parseInt((String)vmjExchange.getRequestBodyForm("amount"));
        String senderName = (String) vmjExchange.getRequestBodyForm("sender_name");
        String senderEmail = (String) vmjExchange.getRequestBodyForm("sender_email");
        String senderBank = (String) vmjExchange.getRequestBodyForm("bank");

        requestMap.put("id",id);
        requestMap.put("title", title);
        requestMap.put("type", type);
        requestMap.put("amount",amount);
        requestMap.put("sender_name",senderName);
        requestMap.put("sender_email",senderEmail);
        requestMap.put("sender_bank",senderBank);
        requestMap.put("sender_bank_type",SenderBankType.VIRTUALACCOUNT.getValue());
        requestMap.put("step", PaymentFlow.THIRD.getValue());

        return requestMap;
    }

    @Override
    public Map<String, Object> getVirtualAccountResponse(String rawResponse, int id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        String va_number = (String) rawResponseMap.get("account_number");
        // Map<String, Object> billPayment = (Map<String, Object>) rawResponseMap.get("bill_payment");
        // Map<String, Object> receiverBankAccount = (Map<String, Object>) billPayment.get("receiver_bank_account");
        // String bankCode = (String) receiverBankAccount.get("bank_code");

        response.put("va_number",va_number);
        response.put("id", id);
        return response;
    }

    @Override
    public Map<String, Object> getEWalletRequestBody(VMJExchange vmjExchange){
        int id = generateId();
        Map<String, Object> requestMap = new HashMap<>();
        String title = (String) vmjExchange.getRequestBodyForm("title");
        String type = (String) vmjExchange.getRequestBodyForm("type");
        int amount = Integer.parseInt((String)vmjExchange.getRequestBodyForm("amount"));
        String senderName = (String) vmjExchange.getRequestBodyForm("sender_name");
        String senderEmail = (String) vmjExchange.getRequestBodyForm("sender_email");
        String senderBank = (String) vmjExchange.getRequestBodyForm("payment_type");
        String senderPhoneNumber = (String) vmjExchange.getRequestBodyForm("phone_number");

        requestMap.put("id",id);
        requestMap.put("title", title);
        requestMap.put("type", type);
        requestMap.put("amount",amount);
        requestMap.put("sender_name",senderName);
        requestMap.put("sender_email",senderEmail);
        requestMap.put("sender_bank",senderBank);
        requestMap.put("is_phone_number_required",PhoneNumberRequired.TRUE.getValue());
        requestMap.put("sender_phone_number", senderPhoneNumber);
        requestMap.put("sender_bank_type",SenderBankType.EWALLET.getValue());
        requestMap.put("step", PaymentFlow.THIRD.getValue());

        return requestMap;
    }

    	// String url = (String) response.get("url");
		// String type = (String) response.get("payment_type");
		// int id = (int) response.get("id");

		// String phoneNumber = (String) vmjExchange.getRequestBodyForm("phone_number");

    @Override
    public Map<String, Object> getEWalletResponse(String rawResponse, int id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        String url = (String) rawResponseMap.get("payment_url");
        String paymentType = (String) rawResponseMap.get("bank_code");
        String phoneNumber = (String) rawResponseMap.get("user_phone");
        response.put("phone_number",phoneNumber);
        response.put("url", url);
        response.put("payment_type",paymentType);
        response.put("id", id);
        return response;
    }


    @Override
    public Map<String, Object> getPaymentLinkRequestBody(VMJExchange vmjExchange){
        int id = generateId();
        Map<String, Object> requestMap = new HashMap<>();
        String title = (String) vmjExchange.getRequestBodyForm("title");
        String type = (String) vmjExchange.getRequestBodyForm("type");
        int amount = Integer.parseInt((String)vmjExchange.getRequestBodyForm("amount"));
        String senderName = (String) vmjExchange.getRequestBodyForm("sender_name");
        
        requestMap.put("id",id);
        requestMap.put("title", title);
        requestMap.put("type", type);
        requestMap.put("sender_name", senderName);
        requestMap.put("amount",amount);
        requestMap.put("step", PaymentFlow.FIRST.getValue() );

        return requestMap;
    }

    @Override
    public Map<String, Object> getPaymentLinkResponse(String rawResponse, int id){
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> rawResponseMap = gson.fromJson(rawResponse, mapType);
        String url = (String) rawResponseMap.get("link_url");
        response.put("url", url);
        response.put("id", id);
        return response;
    }

    @Override
    public HashMap<String, String> getHeaderParams() {
        HashMap<String, String> flipHeaderParams = new HashMap<>();
        String contentType = PropertiesReader.getProp("content_type");
        String authorization = PropertiesReader.getProp("authorization");
        String cookie = PropertiesReader.getProp("cookie");
//        String xTimestamp = this.getEnvThenProp(Constants.FLIP_X_TIMESTAMP_ENV,Constants.FLIP_X_TIMESTAMP_ENV);
        flipHeaderParams.put("Content-Type",contentType);
        flipHeaderParams.put("idempotency-key", UUID.randomUUID().toString());
        flipHeaderParams.put("X-TIMESTAMP","");
        flipHeaderParams.put("Authorization",authorization);
        flipHeaderParams.put("Cookie",cookie);
        return flipHeaderParams;
    }

    // @Override
    // public Map<String, Object>  processRequestMap(VMJExchange vmjExchange, String serviceName){

    //     Map<String, Object> result = null;
    //     try {
    //         // lebih baik ini atau if else?
    //         Method m = this.getClass().getMethod("get" + serviceName + "RequestBody", VMJExchange.class);
    //         result = (Map<String, Object>) m.invoke(this, vmjExchange);
    //     }
    //     catch (IllegalArgumentException e){
    //         e.printStackTrace();
    //     }
    //     catch (InvocationTargetException e){
    //         e.printStackTrace();
    //     }
    //     catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return result;
    // }

}