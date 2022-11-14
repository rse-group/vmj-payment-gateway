package paymentgateway.paymentchannel.retailoutlet;

import paymentgateway.paymentchannel.PaymentChannelFactory;
import paymentgateway.paymentchannel.core.PaymentChannel;
import paymentgateway.paymentchannel.core.PaymentChannelResourceDecorator;
import paymentgateway.paymentchannel.core.PaymentChannelResourceComponent;

import java.util.HashMap;

public class PaymentChannelResourceImpl extends PaymentChannelResourceDecorator {
    public PaymentChannelResourceImpl (PaymentChannelResourceComponent record) {
        super(record);
    }

	public PaymentChannel createChannel(HashMap<String,Object> pgExchange) {
		String retailOutlet = (String) pgExchange.get("retailOutlet");
		
		PaymentChannel channel = record.createChannel(pgExchange);
		String retailPaymentCode = sendChannelRetailOutlet(pgExchange);
		PaymentChannel retailOutletChannel =
				PaymentChannelFactory.createPaymentChannel(
						"paymentgateway.paymentchannel.retailoutlet.PaymentChannelImpl",
						channel,
						retailOutlet,
						retailPaymentCode
						);
		
		return retailOutletChannel;
	}
	
	protected String sendChannelRetailOutlet(HashMap<String,Object> pgExchange) {
		// to do implement channel as retail outlet
		System.out.println("Channel send from retail outlet");
		return "";
	}
}

