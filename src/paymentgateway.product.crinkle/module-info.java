module paymentgateway.product.crinkle {
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

    requires paymentgateway.disbursement.core;
    requires paymentgateway.disbursement.moneytransfer;
    requires paymentgateway.disbursement.special;
    requires paymentgateway.disbursement.facilitatormoneytransfer;

    requires paymentgateway.payment.core;
    requires paymentgateway.payment.debitcard;
    requires paymentgateway.payment.paymentrouting;

	
	requires prices.auth.vmj;
    requires prices.auth.vmj.model;
}