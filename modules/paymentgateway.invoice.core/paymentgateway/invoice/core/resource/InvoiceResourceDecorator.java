package paymentgateway.invoice.core;

import java.util.*;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

public abstract class InvoiceResourceDecorator extends InvoiceResourceComponent {
	protected InvoiceResourceComponent record;
	
	public InvoiceResourceDecorator(InvoiceResourceComponent record) {
		this.record = record;
	}
	
	public HashMap<String,Object> createTransaction(VMJExchange vmjExchange) {
		return record.createTransaction(vmjExchange);
	}
}