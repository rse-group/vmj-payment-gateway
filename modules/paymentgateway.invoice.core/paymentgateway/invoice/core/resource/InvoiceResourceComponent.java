package paymentgateway.invoice.core;

import java.util.*;

import vmj.routing.route.VMJExchange;

public abstract class InvoiceResourceComponent implements InvoiceResource {
	public InvoiceResourceComponent()
	{
		
	}
	
	public abstract HashMap<String, Object> createTransaction(VMJExchange vmjExchange);
}