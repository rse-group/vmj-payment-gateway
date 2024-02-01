package paymentgateway.config.oy;

import paymentgateway.config.core.ConfigDecorator;
import paymentgateway.config.core.ConfigComponent;
import java.util.*;

import vmj.routing.route.VMJExchange;

public class OyConfiguration extends ConfigDecorator{

    public OyConfiguration(ConfigComponent record) {
        super(record);
    }

    public Map<String, String> getOyBankCode(){
        Map<String, String> immutableMap = Map.of("bni", "009",
                "bca", "014",
                "mandiri", "008",
                "bri", "002",
                "permata", "013");
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

    public Map<String, Object> getOyPaymentLinkRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();

        int id = generateId();
        int amount = (int) (Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount")));
        String name = (String) vmjExchange.getRequestBodyForm("sender_name");

        requestMap.put("partner_tx_id", String.valueOf(id));
        requestMap.put("amount", amount);
        requestMap.put("sender_name", name);
        requestMap.put("id",id);
        System.out.println("Oy id:" + String.valueOf(id));
        return requestMap;
    }

    public Map<String, Object> getOyRetailOutletRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();


        int id = generateId();
        int amount = (int) (Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount")));
        String store = (String) vmjExchange.getRequestBodyForm("retailOutlet");

        requestMap.put("partner_trx_id", String.valueOf(id));
        requestMap.put("customer_id", String.valueOf(id));
        requestMap.put("amount", amount);
        requestMap.put("transaction_type", "CASH_IN");
//		requestMap.put("offline_channel",store.toUpperCase());
        requestMap.put("offline_channel","CRM");
        requestMap.put("id",id);
        return requestMap;
    }

    public Map<String, Object> getOyVirtualAccountRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();


        int id = generateId();
        int amount = (int) (Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount")));
        String bank = (String) vmjExchange.getRequestBodyForm("bank");

        requestMap.put("partner_user_id", String.valueOf(id));
        requestMap.put("bank_code", getOyBankCode().get(bank));
        requestMap.put("amount", amount);
        requestMap.put("is_open", false);
        requestMap.put("id",id);
        return requestMap;
    }

    public Map<String, Object> getOyEWalletRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();


        int id = generateId();
        String uuid = UUID.randomUUID().toString();
        int amount = (int) (Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount")));
        String ewallet = (String) vmjExchange.getRequestBodyForm("ewalletType");
        String phone = (String) vmjExchange.getRequestBodyForm("phone_number");

        requestMap.put("partner_trx_id", String.valueOf(id));
        requestMap.put("customer_id", String.valueOf(id));
        requestMap.put("amount", amount);
        requestMap.put("mobile_numer",phone);
        requestMap.put("ewallet_code", getOyEWalletCode().get(ewallet.toLowerCase()));
        requestMap.put("success_redirect_url","https://myweb.com/usertx/" + uuid);
        requestMap.put("id",id);
        return requestMap;
    }

    public Map<String, Object> getOyInvoiceRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();

        int id = generateId();
        int amount = (int) (Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount")));

        requestMap.put("partner_tx_id", String.valueOf(id));
        requestMap.put("amount", amount);

        List<Map<String, Object>> items = toListMap(vmjExchange, "invoice_items");
        requestMap.put("invoice_items",items);

        requestMap.put("id",id);
        return requestMap;
    }

    public Map<String, Object> getOyPaymentRoutingRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();

        int id = generateId();
        int amount = (int) (Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount")));

        requestMap.put("partner_trx_id", String.valueOf(id));
        requestMap.put("partner_user_id", String.valueOf(id));
        requestMap.put("need_frontend", true);

        List<Map<String,Object>> routings = toListMap(vmjExchange,"routings");
        requestMap.put("list_enable_sof", vmjExchange.getRequestBodyForm("list_enable_sof"));
        requestMap.put("list_enable_payment_method",vmjExchange.getRequestBodyForm("list_enable_payment_method"));
        requestMap.put("need_frontend",true);
        requestMap.put("receive_amount",amount);
        requestMap.put("routings",routings);

        requestMap.put("id",id);
        return requestMap;
    }
}