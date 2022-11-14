package paymentgateway.paymentchannel.core;
import java.util.*;

public abstract class PaymentChannelResourceDecorator extends PaymentChannelResourceComponent{
	protected PaymentChannelResourceComponent record;

    public PaymentChannelResourceDecorator(PaymentChannelResourceComponent record) {
        this.record = record;
    }

    public PaymentChannel createChannel(HashMap<String,Object> pgExchange) {
    	return record.createChannel(pgExchange);
    }
}
