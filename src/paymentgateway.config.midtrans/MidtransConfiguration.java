package paymentgateway.config.midtrans;

import paymentgateway.config.core.ConfigDecorator;
import paymentgateway.config.core.ConfigComponent;
import java.util.*;

import vmj.routing.route.VMJExchange;

public class MidtransConfiguration extends ConfigDecorator{

    public MidtransConfiguration(ConfigComponent record) {
        super(record);
    }


    public Map<String, Object> getMidtransPaymentLinkRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> customer_details = new HashMap<String, Object>();
        Map<String, Object> transaction_details = new HashMap<String, Object>();

        int id = generateId();
        double amount = Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount"));


        transaction_details.put("order_id", String.valueOf(id));
        transaction_details.put("gross_amount", amount);
        requestMap.put("transaction_details", transaction_details);

        String name = (String) vmjExchange.getRequestBodyForm("sender_name");
        String[] arr = name.split(" ", 2);
        if(arr.length > 1){
            customer_details.put("first_name", arr[0]);
            customer_details.put("last_name", arr[1]);
        } else{
            customer_details.put("first_name", arr[0]);
        }
        requestMap.put("costumer_details", customer_details);
        requestMap.put("id",id);
        System.out.println("Midtrans id:" + String.valueOf(id));
        return requestMap;
    }

    public Map<String, Object> getMidtransRetailOutletRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> cstore = new HashMap<String, Object>();
        Map<String, Object> transaction_details = new HashMap<String, Object>();

        int id = generateId();
        double amount = Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount"));
        String store = (String) vmjExchange.getRequestBodyForm("retailOutlet");


        transaction_details.put("order_id", String.valueOf(id));
        transaction_details.put("gross_amount", amount);

        cstore.put("store", store);

        requestMap.put("payment_type", "cstore");
        requestMap.put("transaction_details", transaction_details);
        requestMap.put("cstore", cstore);
        requestMap.put("id",id);
        return requestMap;
    }

    public Map<String, Object> getMidtransVirtualAccountRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> bank_transfer = new HashMap<String, Object>();
        Map<String, Object> transaction_details = new HashMap<String, Object>();

        int id = generateId();
        double amount = Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount"));
        String bank = (String) vmjExchange.getRequestBodyForm("bank");


        transaction_details.put("order_id", String.valueOf(id));
        transaction_details.put("gross_amount", amount);

        bank_transfer.put("bank", bank);

        requestMap.put("payment_type", "bank_transfer");
        requestMap.put("transaction_details", transaction_details);
        requestMap.put("bank_transfer", bank_transfer);
        requestMap.put("id",id);
        return requestMap;
    }

    public Map<String, Object> getMidtransEWalletRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> customer_details = new HashMap<String, Object>();
        Map<String, Object> transaction_details = new HashMap<String, Object>();

        int id = generateId();
        double amount = Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount"));
        String ewallet = (String) vmjExchange.getRequestBodyForm("ewalletType");
        String phone = (String) vmjExchange.getRequestBodyForm("phone_number");


        transaction_details.put("order_id", String.valueOf(id));
        transaction_details.put("gross_amount", amount);

        customer_details.put("phone", phone);

        requestMap.put("payment_type", ewallet);
        requestMap.put("transaction_details", transaction_details);
        requestMap.put("customer_details", customer_details);
        requestMap.put("id",id);
        return requestMap;
    }

    public Map<String, Object> getMidtransDebitCardRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> item_details = new HashMap<>();
        Map<String, Object> transaction_details = new HashMap<String, Object>();

        int id = generateId();
        double amount = Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount"));
        String type = (String) vmjExchange.getRequestBodyForm("payment_type");


        transaction_details.put("order_id", String.valueOf(id));
        transaction_details.put("gross_amount", amount);

        item_details.put("name","test item");
        item_details.put("quantity",1);
        item_details.put("price",amount);
        requestMap.put("item_details",item_details);

        requestMap.put("payment_type", type);
        requestMap.put("transaction_details", transaction_details);
        requestMap.put("id",id);
        return requestMap;
    }

    public Map<String, Object> getMidtransCreditCardRequestBody(VMJExchange vmjExchange){
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> credit_card = new HashMap<>();
        Map<String, Object> transaction_details = new HashMap<String, Object>();

        int id = generateId();
        double amount = Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount"));
        String token = (String) vmjExchange.getRequestBodyForm("token_id");


        transaction_details.put("order_id", String.valueOf(id));
        transaction_details.put("gross_amount", amount);

        credit_card.put("token_id",token);
        requestMap.put("credit_card",credit_card);

        requestMap.put("payment_type", "credit_card");
        requestMap.put("transaction_details", transaction_details);
        requestMap.put("id",id);
        return requestMap;
    }
}