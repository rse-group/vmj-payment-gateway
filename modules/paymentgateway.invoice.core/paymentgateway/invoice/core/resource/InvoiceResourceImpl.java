package paymentgateway.invoice.core;

import java.util.*;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

public class InvoiceResourceImpl extends InvoiceResourceComponent {
	@Route(url="call/invoice/createTransaction")
	public HashMap<String,Object> createTransaction(VMJExchange vmjExchange) {
		HashMap<String, Object> result = new HashMap<>();
		System.out.println("Create Transaction From Base");
		return result;
	}
	public static HashMap<String, Object> createTransactionAPI() {
		HashMap<String, Object> result = new HashMap<>();
		System.out.println("Create Transaction API From Base");
		return result;
	}
}