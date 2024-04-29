module paymentgateway.product.rustle {
    requires vmj.routing.route;
    requires vmj.hibernate.integrator;
    
    requires net.bytebuddy;
    requires java.xml.bind;
    requires com.sun.xml.bind;
    requires com.fasterxml.classmate;
    requires jdk.unsupported;
    requires java.logging;

    requires paymentgateway.config.core;
    requires paymentgateway.config.flip;

    requires paymentgateway.disbursement.core;
    requires paymentgateway.disbursement.moneytransfer;
    requires paymentgateway.disbursement.agent;
    requires paymentgateway.disbursement.aggregatormoneytransfer;
    requires paymentgateway.disbursement.international;
    requires paymentgateway.disbursement.internationalmoneytransfer;
    requires paymentgateway.payment.core;
    requires paymentgateway.payment.ewallet;
    
	
	requires prices.auth.vmj;
    requires prices.auth.vmj.model;
}