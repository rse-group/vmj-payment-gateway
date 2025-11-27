module paymentgateway.disbursement.exchangerate {
    requires vmj.auth.model;
    requires vmj.routing.route;
    requires vmj.hibernate.integrator;
    
    requires net.bytebuddy;
    requires java.xml.bind;
    requires com.sun.xml.bind;
    requires com.fasterxml.classmate;
    requires jdk.unsupported;

    requires paymentgateway.disbursement.core;
    requires paymentgateway.disbursement.international;
    requires paymentgateway.disbursement.internationaldisbursementvalidator;
    requires java.logging;

    exports paymentgateway.disbursement.exchangerate;
}