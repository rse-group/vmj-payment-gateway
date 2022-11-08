package paymentgateway.paymentinterface.oy_paymentlink;

import paymentgateway.paymentinterface.core.PaymentInterfaceComponent;
import paymentgateway.paymentinterface.paymentlink.PaymentInterfaceImpl;

public class OyPaymentInterfaceImpl extends PaymentInterfaceImpl {
	public OyPaymentInterfaceImpl(PaymentInterfaceComponent record, String paymentLink) {
		super(record, paymentLink);
	}
}