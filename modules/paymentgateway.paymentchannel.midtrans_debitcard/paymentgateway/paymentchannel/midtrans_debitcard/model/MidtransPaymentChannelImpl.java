package paymentgateway.paymentchannel.midtrans_debitcard;

import paymentgateway.paymentchannel.core.PaymentChannelComponent;
import paymentgateway.paymentchannel.debitcard.PaymentChannelImpl;

public class MidtransPaymentChannelImpl extends PaymentChannelImpl {
	public MidtransPaymentChannelImpl(PaymentChannelComponent record, String bankCode, String directDebitUrl) {
		super(record, bankCode, directDebitUrl);
	}
}