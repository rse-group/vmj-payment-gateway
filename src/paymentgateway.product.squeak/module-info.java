module paymentgateway.product.squeak {
    requires vmj.routing.route;
    requires vmj.hibernate.integrator;
    
    requires net.bytebuddy;
    requires java.xml.bind;
    requires com.sun.xml.bind;
    requires com.fasterxml.classmate;
    requires jdk.unsupported;

    requires paymentgateway.config.core;
    requires paymentgateway.config.midtrans;
    requires paymentgateway.payment.core;
    requires paymentgateway.payment.creditcard;
    requires paymentgateway.payment.retailoutlet;
	
	requires prices.auth.vmj;
    requires prices.auth.vmj.model;
}