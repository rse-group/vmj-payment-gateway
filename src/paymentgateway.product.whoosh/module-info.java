module paymentgateway.product.whoosh {
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
    requires paymentgateway.config.midtrans;
    requires paymentgateway.config.oy;

    requires paymentgateway.payment.core;
    requires paymentgateway.payment.paymentlink;
    requires paymentgateway.payment.virtualaccount;
    requires paymentgateway.payment.ewallet;
	
	requires prices.auth.vmj;
    requires prices.auth.vmj.model;
}