package paymentgateway.config.flip;

import paymentgateway.config.core.ConfigDecorator;
import paymentgateway.config.core.ConfigComponent;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import vmj.routing.route.VMJExchange;

public class FlipConfiguration extends ConfigDecorator{

    public FlipConfiguration(ConfigComponent record) {
        super(record);
    }

    public Map<String, Object> getFlipMoneyTransferRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();

        String number = (String) vmjExchange.getRequestBodyForm("account_number");
        String code = (String) vmjExchange.getRequestBodyForm("bank_code");
        String amount = (String) vmjExchange.getRequestBodyForm("amount");

        requestMap.put("account_number", number);
        requestMap.put("bank_code", code);
        requestMap.put("amount", amount);
        return vmjExchange.getPayload();
    }

    public Map<String, Object> getFlipSpecialMoneyTransferRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();

        String number = (String) vmjExchange.getRequestBodyForm("account_number");
        String code = (String) vmjExchange.getRequestBodyForm("bank_code");
        String amount = (String) vmjExchange.getRequestBodyForm("amount");
        String name = (String) vmjExchange.getRequestBodyForm("sender_name");
        String address = (String) vmjExchange.getRequestBodyForm("sender_address");
        int country = Integer.parseInt((String) vmjExchange.getRequestBodyForm("sender_country"));
        String job = (String) vmjExchange.getRequestBodyForm("sender_job");
        String direction = (String) vmjExchange.getRequestBodyForm("direction");

        requestMap.put("account_number", number);
        requestMap.put("bank_code", code);
        requestMap.put("amount", amount);
        requestMap.put("sender_name", name);
        requestMap.put("sender_address", address);
        requestMap.put("sender_country", country);
        requestMap.put("sender_job", job);
        requestMap.put("direction", direction);
        return vmjExchange.getPayload();
    }

    public Map<String, Object> getFlipInternationalMoneyTransferRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();

        String amount = (String) vmjExchange.getRequestBodyForm("amount");
        String source = (String) vmjExchange.getRequestBodyForm("source_country");
        String destination = (String) vmjExchange.getRequestBodyForm("destination_country");
        String beneficiaryFullName = (String) vmjExchange.getRequestBodyForm("beneficiary_full_name");
        String beneficiaryAccountNumber = (String) vmjExchange.getRequestBodyForm("beneficiary_account_number");
        String beneficiaryBankId = (String) vmjExchange.getRequestBodyForm("beneficiary_bank_id");
        String beneficiaryBankName = (String) vmjExchange.getRequestBodyForm("beneficiary_bank_name");
//        String beneficiaryEmail = (String) vmjExchange.getRequestBodyForm("beneficiary_email");
        String beneficiaryMsisdn = (String) vmjExchange.getRequestBodyForm("beneficiary_msisdn");
        String beneficiaryNationality = (String) vmjExchange.getRequestBodyForm("beneficiary_nationality");
        String beneficiaryProvince = (String) vmjExchange.getRequestBodyForm("beneficiary_province");
        String beneficiaryCity = (String) vmjExchange.getRequestBodyForm("beneficiary_city");
//        String beneficiaryPostalCode = (String) vmjExchange.getRequestBodyForm("beneficiary_postal_code");
        String beneficiaryRelationship = (String) vmjExchange.getRequestBodyForm("beneficiary_relationship");
        String beneficiarySourceOfFunds = (String) vmjExchange.getRequestBodyForm("beneficiary_source_of_funds");
        String beneficiaryRemittancePurposes = (String) vmjExchange.getRequestBodyForm("beneficiary_remittance_purposes");
//        String beneficiarySortCode = (String) vmjExchange.getRequestBodyForm("beneficiary_sort_code");
        String senderName = (String) vmjExchange.getRequestBodyForm("sender_name");
        int senderCountry = Integer.parseInt((String)vmjExchange.getRequestBodyForm("sender_country"));
        int senderPlaceOfBirth = Integer.parseInt((String) vmjExchange.getRequestBodyForm("sender_place_of_birth"));
        String senderDateOfBirth = (String) vmjExchange.getRequestBodyForm("sender_date_of_birth");
        String senderAddress = (String) vmjExchange.getRequestBodyForm("sender_address");
        String senderIdentityType = (String) vmjExchange.getRequestBodyForm("sender_identity_type");
        String senderIdentityNumber = (String) vmjExchange.getRequestBodyForm("sender_identity_number");
        String senderJob = (String) vmjExchange.getRequestBodyForm("sender_job");
        String senderEmail = (String) vmjExchange.getRequestBodyForm("sender_email");
        String senderCity = (String) vmjExchange.getRequestBodyForm("sender_city");
        String senderPhoneNumber = (String) vmjExchange.getRequestBodyForm("sender_phone_number");
//        String beneficiaryRegion = (String) vmjExchange.getRequestBodyForm("beneficiary_region");


        requestMap.put("amount", amount);
        requestMap.put("source_country", source);
        requestMap.put("destination_country", destination);
        requestMap.put("beneficiary_full_name", beneficiaryFullName);
        requestMap.put("beneficiary_account_number", beneficiaryAccountNumber);
        requestMap.put("beneficiary_bank_id", beneficiaryBankId);
        requestMap.put("beneficiary_bank_name", beneficiaryBankId);
//        requestMap.put("beneficiary_email", beneficiaryEmail);
        requestMap.put("beneficiary_msisdn", beneficiaryMsisdn);
        requestMap.put("beneficiary_nationality", beneficiaryNationality);
        requestMap.put("beneficiary_province", beneficiaryProvince);
        requestMap.put("beneficiary_city", beneficiaryCity);
//        requestMap.put("beneficiary_postal_code", beneficiaryPostalCode);
        requestMap.put("beneficiary_relationship", beneficiaryRelationship);
        requestMap.put("beneficiary_source_of_funds", beneficiarySourceOfFunds);
        requestMap.put("beneficiary_remittance_purposes", beneficiaryRemittancePurposes);
//        requestMap.put("beneficiary_sort_code", beneficiarySortCode);
        requestMap.put("sender_name", senderName);
        requestMap.put("sender_country", senderCountry);
        requestMap.put("sender_place_of_birth", senderPlaceOfBirth);
        requestMap.put("sender_date_of_birth", senderDateOfBirth);
        requestMap.put("sender_address", senderAddress);
        requestMap.put("sender_identity_type", senderIdentityType);
        requestMap.put("sender_identity_number", senderIdentityNumber);
        requestMap.put("sender_job", senderJob);
        requestMap.put("sender_email", senderEmail);
        requestMap.put("sender_city", senderCity);
        requestMap.put("sender_phone_number", senderPhoneNumber);
//        requestMap.put("beneficiary_region", beneficiaryRegion);
        requestMap.put("transaction_type", "C2C");
        return vmjExchange.getPayload();
    }

    public Map<String, Object> getFlipAgentMoneyTransferRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();

        String id = (String) vmjExchange.getRequestBodyForm("agent_id");
        String number = (String) vmjExchange.getRequestBodyForm("account_number");
        String code = (String) vmjExchange.getRequestBodyForm("bank_code");
        String amount = (String) vmjExchange.getRequestBodyForm("amount");
        String direction = (String) vmjExchange.getRequestBodyForm("direction");

        requestMap.put("agent_id", id);
        requestMap.put("account_number", number);
        requestMap.put("bank_code", code);
        requestMap.put("amount", amount);
        requestMap.put("direction", direction);
        return vmjExchange.getPayload();
    }

    public Map<String, Object> getFlipApprovalTransferRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();

        String number = (String) vmjExchange.getRequestBodyForm("account_number");
        String code = (String) vmjExchange.getRequestBodyForm("bank_code");
        String amount = (String) vmjExchange.getRequestBodyForm("amount");
        String approverId = (String) vmjExchange.getRequestBodyForm("approver_id");

        requestMap.put("account_number", number);
        requestMap.put("bank_code", code);
        requestMap.put("amount", amount);
        requestMap.put("approver_id",approverId);
        return vmjExchange.getPayload();
    }

    public String getParamsUrlEncoded(VMJExchange vmjExchange) {
        ArrayList<String> paramList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : vmjExchange.getPayload().entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue();
            if (val instanceof String) {
                paramList.add(key + "=" + URLEncoder.encode(val.toString(), StandardCharsets.UTF_8));
            } else if (val instanceof Integer) {
                paramList.add(key + "=" + URLEncoder.encode(val.toString(), StandardCharsets.UTF_8));
            } else if (val instanceof Double) {
                int temp = ((Double) val).intValue();
                paramList.add(key + "=" + URLEncoder.encode(Integer.toString(temp), StandardCharsets.UTF_8));
            }

        }
        String encodedURL = String.join("&",paramList);
        return encodedURL;
    }

}