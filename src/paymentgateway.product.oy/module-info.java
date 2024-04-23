module paymentgateway.product.oy {
    requires vmj.routing.route;
    requires vmj.hibernate.integrator;
    
    requires net.bytebuddy;
    requires java.xml.bind;
    requires com.sun.xml.bind;
    requires com.fasterxml.classmate;
    requires jdk.unsupported;

    requires paymentgateway.config.core;
    requires paymentgateway.config.oy;
    requires paymentgateway.payment.core;
    requires paymentgateway.payment.invoice;
    requires paymentgateway.payment.paymentlink;
    requires paymentgateway.payment.paymentrouting;
    requires paymentgateway.payment.virtualaccount;
    requires paymentgateway.payment.ewallet;
    requires paymentgateway.payment.retailoutlet;
	
	requires prices.auth.vmj;
    requires prices.auth.vmj.model;
}