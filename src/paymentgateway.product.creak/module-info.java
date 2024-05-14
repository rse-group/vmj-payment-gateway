module paymentgateway.product.creak {
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
    requires paymentgateway.payment.virtualaccount;
}