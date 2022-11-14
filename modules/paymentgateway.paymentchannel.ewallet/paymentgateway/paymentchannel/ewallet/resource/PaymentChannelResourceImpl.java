package paymentgateway.paymentchannel.ewallet;

import java.util.HashMap;

import paymentgateway.paymentchannel.PaymentChannelFactory;
import paymentgateway.paymentchannel.core.PaymentChannel;
import paymentgateway.paymentchannel.core.PaymentChannelResourceDecorator;
import paymentgateway.paymentchannel.core.PaymentChannelImpl;
import paymentgateway.paymentchannel.core.PaymentChannelResourceComponent;

public class PaymentChannelResourceImpl extends PaymentChannelResourceDecorator {
    public PaymentChannelResourceImpl (PaymentChannelResourceComponent record) {
        super(record);
    }

	public PaymentChannel createChannel(HashMap<String,Object> pgExchange) {
		String eWalletType = (String) pgExchange.get("eWalletType");
		
		PaymentChannel channel = record.createChannel(pgExchange);
		String eWalletUrl = sendChannelEWallet(pgExchange);
		PaymentChannel ewalletChannel =
			PaymentChannelFactory.createPaymentChannel(
					"paymentgateway.paymentchannel.ewallet.PaymentChannelImpl",
					channel,
					eWalletType,
					eWalletUrl
					);
		
		return ewalletChannel;
	}
	
	protected String sendChannelEWallet(HashMap<String,Object> pgExchange) {
		// to do implement channel as ewallet
		System.out.println("Channel send from ewallet");
		return "";
	}
}

