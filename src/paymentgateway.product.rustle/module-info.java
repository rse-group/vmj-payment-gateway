module paymentgateway.product.rustle {
    requires vmj.auth.model;
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
    requires paymentgateway.disbursement.moneytransfer.international;
    requires paymentgateway.disbursement.moneytransfer.internationalmoneytransfer;
    requires paymentgateway.payment.core;
    requires paymentgateway.payment.ewallet;
}