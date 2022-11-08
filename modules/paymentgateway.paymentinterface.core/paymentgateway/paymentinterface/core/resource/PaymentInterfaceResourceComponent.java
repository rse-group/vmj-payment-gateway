package paymentgateway.paymentinterface.core;
import java.util.*;

import vmj.hibernate.integrator.RepositoryUtil;
import vmj.routing.route.VMJExchange;
//add other required packages

public abstract class PaymentInterfaceResourceComponent implements PaymentInterfaceResource{
	protected RepositoryUtil<PaymentInterface> PaymentInterfaceRepository;

    public PaymentInterfaceResourceComponent(){
        this.PaymentInterfaceRepository = new RepositoryUtil<PaymentInterface>(paymentgateway.paymentinterface.core.PaymentInterfaceComponent.class);
    }	
}
