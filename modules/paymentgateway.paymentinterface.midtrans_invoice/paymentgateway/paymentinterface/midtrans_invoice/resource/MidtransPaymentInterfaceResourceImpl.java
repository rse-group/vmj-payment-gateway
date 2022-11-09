package paymentgateway.paymentinterface.midtrans_invoice;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import paymentgateway.paymentinterface.core.PaymentInterface;
import paymentgateway.paymentinterface.core.PaymentInterfaceResourceComponent;
import paymentgateway.paymentinterface.invoice.PaymentInterfaceResourceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.*;

import com.google.gson.Gson;

import com.midtrans.Midtrans;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;

public class MidtransPaymentInterfaceResourceImpl extends PaymentInterfaceResourceImpl {
	protected String apiKey;
	public MidtransPaymentInterfaceResourceImpl(PaymentInterfaceResourceComponent record) {
		super(record);
		this.apiKey = "SB-Mid-server-NVYFqUidEQUTaozWjW77fFWW";
	}
	
	@Override
	protected String sendTransactionInvoice(int amount, String idTransaction) {
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
	
	@Route(url="test/call/midtrans/invoice")
	public HashMap<String,Object> midtransInvoice(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) return null;
		int randomId = Math.abs((new Random()).nextInt());
		PaymentInterface result = this.createTransaction(56000, "test-midtrans-invoice-" + randomId);
		return result.toHashMap();
	}
}