module paymentgateway.product.basic {
    requires vmj.auth.model;
    requires vmj.routing.route;
    requires vmj.hibernate.integrator;
    
    requires net.bytebuddy;
    requires java.xml.bind;
    requires com.sun.xml.bind;
    requires com.fasterxml.classmate;
    requires jdk.unsupported;

    requires paymentgateway.config.core;
    requires paymentgateway.config.midtrans;
    requires paymentgateway.config.oy;
    requires paymentgateway.payment.core;
    requires paymentgateway.payment.ewallet;
    requires paymentgateway.payment.paymentlink;
    requires paymentgateway.payment.retailoutlet;
    requires paymentgateway.payment.virtualaccount;
}