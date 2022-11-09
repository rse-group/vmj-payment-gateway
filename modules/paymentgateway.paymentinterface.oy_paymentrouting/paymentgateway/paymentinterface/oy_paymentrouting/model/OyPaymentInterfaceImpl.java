package paymentgateway.paymentinterface.oy_paymentrouting;

import java.util.List;

import paymentgateway.paymentinterface.core.PaymentInterfaceComponent;
import paymentgateway.paymentinterface.paymentrouting.PaymentInterfaceImpl;

import paymentgateway.paymentinterface.paymentrouting.PaymentRoutingRecipient;

public class OyPaymentInterfaceImpl extends PaymentInterfaceImpl {
	public OyPaymentInterfaceImpl(
			PaymentInterfaceComponent record, String paymentMethods,
			String sourceOfFunds, List<PaymentRoutingRecipient> paymentRoutings,
			String paymentCheckoutUrl) {
		super(record, paymentMethods, sourceOfFunds, paymentRoutings, paymentCheckoutUrl);
	}
}