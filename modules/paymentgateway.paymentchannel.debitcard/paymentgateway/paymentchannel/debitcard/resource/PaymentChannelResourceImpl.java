package paymentgateway.paymentchannel.debitcard;

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
		String bankCode = (String) pgExchange.get("bankCode");
		
		PaymentChannel channel = record.createChannel(pgExchange);
		String directDebitUrl = sendChannelDebitCard(pgExchange);
		PaymentChannel directDebitChannel = 
				PaymentChannelFactory.createPaymentChannel(
						"paymentgateway.paymentchannel.debitcard.PaymentChannelImpl",
						channel,
						bankCode,
						directDebitUrl
						);
		
		return directDebitChannel;
	}
	
	protected String sendChannelDebitCard(HashMap<String,Object> pgExchange) {
		// to do implement channel as debit card
		System.out.println("Channel send from debit card");
		return "";
	}
}

