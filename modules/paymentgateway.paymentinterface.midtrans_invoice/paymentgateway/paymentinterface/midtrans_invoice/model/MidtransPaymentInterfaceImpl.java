package paymentgateway.paymentinterface.midtrans_invoice;

import paymentgateway.paymentinterface.core.PaymentInterfaceComponent;
import paymentgateway.paymentinterface.invoice.PaymentInterfaceImpl;

public class MidtransPaymentInterfaceImpl extends PaymentInterfaceImpl {
	public MidtransPaymentInterfaceImpl(PaymentInterfaceComponent record, String transactionToken) {
		super(record, transactionToken);
	}
}