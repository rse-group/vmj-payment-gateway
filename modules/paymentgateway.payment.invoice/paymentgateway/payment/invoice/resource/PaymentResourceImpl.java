package paymentgateway.payment.invoice;

import com.google.gson.Gson;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import paymentgateway.payment.PaymentFactory;
import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentResourceDecorator;
import paymentgateway.payment.core.PaymentImpl;
import paymentgateway.payment.core.PaymentResourceComponent;

import com.midtrans.Midtrans;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;

public class PaymentResourceImpl extends PaymentResourceDecorator {
	
	protected String apiKey;
    public PaymentResourceImpl (PaymentResourceComponent record) {
    	super(record);
		this.apiKey = "SB-Mid-server-NVYFqUidEQUTaozWjW77fFWW";
    }

	public Payment createPayment(HashMap<String,Object> vmjExchange) {
		Payment transaction = record.createPayment(vmjExchange);
		String transactionToken = sendTransaction(vmjExchange);
		Payment invoiceTransaction = PaymentFactory.createPayment(
				"paymentgateway.payment.invoice.PaymentImpl", transaction, transactionToken);
		return invoiceTransaction;
	}
	
	protected String sendTransaction(HashMap<String,Object> vmjExchange) {
		String idTransaction = (String) vmjExchange.get("idTransaction");
		int amount = (int) vmjExchange.get("amount");
		
		Gson gson = new Gson();
		Map<String,Object> transactionDetails = new HashMap<String,Object>();
		transactionDetails.put("order_id", idTransaction);
		transactionDetails.put("gross_amount", amount);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("transaction_details", transactionDetails);
		
		String token = "";
		Midtrans.serverKey = apiKey;
		Midtrans.isProduction = false;
		try {
			String apiCallResult = SnapApi.createTransactionToken(params);
			token = token + apiCallResult;
		} catch (Exception e) {
			System.out.println(e);
		}
		return token;
	}
	
	@Route(url="test/call/invoice")
	public HashMap<String,Object> testInvoice(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) return null;
		
		int amount = ((Double) vmjExchange.getRequestBodyForm("amount")).intValue();
		String idTransaction = (String) vmjExchange.getRequestBodyForm("idTransaction");
		
		HashMap<String,Object> testExchange = new HashMap<>();
		testExchange.put("idTransaction", idTransaction);
		testExchange.put("amount", amount);
		Payment result = this.createPayment(testExchange);
		return result.toHashMap();
	}
}

