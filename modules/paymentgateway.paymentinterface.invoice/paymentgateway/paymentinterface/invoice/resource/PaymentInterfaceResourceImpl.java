package paymentgateway.paymentinterface.invoice;

import java.util.*;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import paymentgateway.paymentinterface.PaymentInterfaceFactory;
import paymentgateway.paymentinterface.core.PaymentInterface;
import paymentgateway.paymentinterface.core.PaymentInterfaceResourceDecorator;
import paymentgateway.paymentinterface.core.PaymentInterfaceImpl;
import paymentgateway.paymentinterface.core.PaymentInterfaceResourceComponent;

public class PaymentInterfaceResourceImpl extends PaymentInterfaceResourceDecorator {
    public PaymentInterfaceResourceImpl (PaymentInterfaceResourceComponent record) {
        super(record);
    }

	public PaymentInterface createTransaction(int amount, String idTransaction) {
		PaymentInterface transaction = record.createTransaction(amount, idTransaction);
		String transactionToken = sendTransactionInvoice(amount, idTransaction);
		PaymentInterface invoiceTransaction = PaymentInterfaceFactory.createPaymentInterface("paymentgateway.paymentinterface.invoice.PaymentInterfaceImpl", transaction, transactionToken);
		return invoiceTransaction;
	}
	
	protected String sendTransactionInvoice(int amount, String idTransaction) {
		// to do implement a call for invoice
		System.out.println("Transaction send from invoice");
		return "";
	}
}