module paymentgateway.product.flip {
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
    requires paymentgateway.disbursement.special;
    requires paymentgateway.disbursement.international;
    requires paymentgateway.disbursement.internationalmoneytransfer;
    requires paymentgateway.payment.core;
    requires paymentgateway.payment.paymentlink;
    requires paymentgateway.payment.virtualaccount;
    requires paymentgateway.payment.ewallet;
    requires paymentgateway.disbursement.agentmoneytransfer;
    requires paymentgateway.disbursement.specialmoneytransfer;
	
	requires vmj.auth;
    requires vmj.auth.model;
}