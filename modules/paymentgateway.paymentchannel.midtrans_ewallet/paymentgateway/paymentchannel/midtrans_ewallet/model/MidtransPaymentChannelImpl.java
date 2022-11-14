package paymentgateway.paymentchannel.midtrans_ewallet;

import paymentgateway.paymentchannel.ewallet.PaymentChannelImpl;
import paymentgateway.paymentchannel.core.PaymentChannelComponent;

public class MidtransPaymentChannelImpl extends PaymentChannelImpl {
	public MidtransPaymentChannelImpl(PaymentChannelComponent record, String eWalletType, String eWalletUrl) {
		super(record, eWalletType, eWalletUrl);
	}
}