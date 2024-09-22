module paymentgateway.product.xendit {
    requires vmj.routing.route;
    requires vmj.hibernate.integrator;

    requires net.bytebuddy;
    requires java.xml.bind;
    requires com.sun.xml.bind;
    requires com.fasterxml.classmate;
    requires jdk.unsupported;
    requires java.logging;

    requires paymentgateway.config.core;
    requires paymentgateway.config.xendit;

    requires paymentgateway.disbursement.core;

    requires vmj.auth;
    requires vmj.auth.model;
}