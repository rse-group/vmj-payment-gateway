module paymentgateway.disbursement.agentdisbursement {
    requires vmj.auth.model;
    requires vmj.routing.route;
    requires vmj.hibernate.integrator;
    
    requires net.bytebuddy;
    requires java.xml.bind;
    requires com.sun.xml.bind;
    requires com.fasterxml.classmate;
    requires jdk.unsupported;

    requires paymentgateway.disbursement.core;
    requires paymentgateway.disbursement.agent;
    requires paymentgateway.disbursement.domesticdisbursementvalidator;
    requires java.logging;

    exports paymentgateway.disbursement.agentdisbursement;
}