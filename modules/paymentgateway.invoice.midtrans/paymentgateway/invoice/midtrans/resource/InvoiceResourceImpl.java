package paymentgateway.invoice.midtrans;

import java.util.*;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import paymentgateway.invoice.InvoiceResourceFactory;
import paymentgateway.invoice.core.InvoiceResource;
import paymentgateway.invoice.core.InvoiceResourceDecorator;
import paymentgateway.invoice.core.InvoiceResourceComponent;

import com.midtrans.Midtrans;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;

import org.json.JSONObject;

public class InvoiceResourceImpl extends InvoiceResourceDecorator {
    public InvoiceResourceImpl(InvoiceResourceComponent record) {
        super(record);
    }

    public Map<String, Object> generateTokenParameter() {
        Midtrans.serverKey = "SB-Mid-server-wDHh4lsOO2L7L91t3eOZepH0";
        Midtrans.clientKey = "SB-Mid-client-Gt9d3TDgvwzlnTi5";
 
        // Set value to true if you want Production Environment (accept real transaction).
        Midtrans.isProduction = false;

        UUID idRand = UUID.randomUUID();
        Map<String, Object> params = new HashMap<>();

        Map<String, String> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", idRand.toString());
        transactionDetails.put("gross_amount", "50000");

        params.put("transaction_details", transactionDetails);

        Map<String, String> creditCard = new HashMap<>();
        creditCard.put("secure", "true");
        params.put("credit_card", creditCard);

        return params;
    }

    @Route(url="call/invoice/midtrans/createTransaction")
    public HashMap<String, Object> createTransaction(VMJExchange vmjExchange) {
    	System.out.println("createTransaction triggered");
        HashMap<String, Object> result = new HashMap<>();
        Map<String, Object> params = generateTokenParameter();
        try {
            String apiCallResult = SnapApi.createTransactionToken(params);
            result.put("token", apiCallResult);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return result;
    }
}