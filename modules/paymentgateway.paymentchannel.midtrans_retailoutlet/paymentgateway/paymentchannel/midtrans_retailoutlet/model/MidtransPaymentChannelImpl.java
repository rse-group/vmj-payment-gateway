package paymentgateway.paymentchannel.midtrans_retailoutlet;

import paymentgateway.paymentchannel.core.PaymentChannelDecorator;
import paymentgateway.paymentchannel.core.PaymentChannelComponent;

public class MidtransPaymentChannelImpl extends PaymentChannelDecorator {
	public MidtransPaymentChannelImpl(PaymentChannelComponent record) {
		super(record);
	}
}