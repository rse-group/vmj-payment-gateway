module paymentgateway.product.flip {
    requires vmj.routing.route;
    requires vmj.hibernate.integrator;
    
    requires net.bytebuddy;
    requires java.xml.bind;
    requires com.sun.xml.bind;
    requires com.fasterxml.classmate;
    requires jdk.unsupported;

    requires paymentgateway.config.core;
    requires paymentgateway.config.flip;

    requires paymentgateway.disbursement.core;
    requires paymentgateway.disbursement.moneytransfer;
    requires paymentgateway.disbursement.special;
    requires paymentgateway.disbursement.international;
    requires paymentgateway.disbursement.agent;
    requires paymentgateway.disbursement.internationalmoneytransfer;
    requires paymentgateway.disbursement.specialmoneytransfer;
    requires paymentgateway.disbursement.agentmoneytransfer;
    requires paymentgateway.disbursement.aggregator;
	
	requires prices.auth.vmj;
    requires prices.auth.vmj.model;
}