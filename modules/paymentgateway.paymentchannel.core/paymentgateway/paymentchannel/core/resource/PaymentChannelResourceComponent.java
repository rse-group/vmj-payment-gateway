package paymentgateway.paymentchannel.core;
import java.util.*;

import vmj.hibernate.integrator.RepositoryUtil;
import vmj.routing.route.VMJExchange;
//add other required packages

public abstract class PaymentChannelResourceComponent implements PaymentChannelResource{
	protected RepositoryUtil<PaymentChannel> PaymentChannelRepository;

    public PaymentChannelResourceComponent(){
        this.PaymentChannelRepository = new RepositoryUtil<PaymentChannel>(paymentgateway.paymentchannel.core.PaymentChannelComponent.class);
    }	
}
