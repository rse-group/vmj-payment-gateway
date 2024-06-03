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
    requires paymentgateway.disbursement.moneytransfer.agent;
    requires paymentgateway.disbursement.moneytransfer.special;
    requires paymentgateway.disbursement.moneytransfer.international;
    requires paymentgateway.disbursement.moneytransfer.internationalmoneytransfer;
    requires paymentgateway.payment.core;
    requires paymentgateway.payment.paymentlink;
    requires paymentgateway.payment.virtualaccount;
    requires paymentgateway.payment.ewallet;
    requires paymentgateway.disbursement.moneytransfer.agentmoneytransfer;
    requires paymentgateway.disbursement.moneytransfer.specialmoneytransfer;

    requires vmj.auth;
    requires vmj.auth.model;
}