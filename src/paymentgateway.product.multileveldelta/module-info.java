module paymentgateway.product.multileveldelta {
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
    requires paymentgateway.config.xendit;
    requires paymentgateway.disbursement.core;
    requires paymentgateway.disbursement.international;
    requires paymentgateway.disbursement.internationaldisbursementvalidator;
    requires paymentgateway.disbursement.internationaldisbursement;
    requires paymentgateway.disbursement.special;
    requires paymentgateway.disbursement.domesticdisbursementvalidator;
    requires paymentgateway.disbursement.specialdisbursement;
    requires paymentgateway.disbursement.agent;
    requires paymentgateway.disbursement.agentdisbursement;
}