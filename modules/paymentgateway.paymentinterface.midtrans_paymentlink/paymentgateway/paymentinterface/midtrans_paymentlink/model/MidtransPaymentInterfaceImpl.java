package paymentgateway.paymentinterface.midtrans_paymentlink;

import paymentgateway.paymentinterface.core.PaymentInterfaceComponent;
import paymentgateway.paymentinterface.paymentlink.PaymentInterfaceImpl;

public class MidtransPaymentInterfaceImpl extends PaymentInterfaceImpl {
	public MidtransPaymentInterfaceImpl(PaymentInterfaceComponent record, String paymentLink) {
		super(record, paymentLink);
	}
}