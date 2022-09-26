package paymentgateway.invoice.core;

import java.util.*;

import vmj.routing.route.VMJExchange;

public interface InvoiceResource
{
	HashMap<String, Object> createTransaction(VMJExchange vmjExchange);
}