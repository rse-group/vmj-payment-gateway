module paymentgateway.invoice.core {
	exports paymentgateway.invoice;
	exports paymentgateway.invoice.core;
    
    requires vmj.routing.route;
    // https://stackoverflow.com/questions/46488346/error32-13-error-cannot-access-referenceable-class-file-for-javax-naming-re/50568217
    requires java.naming;
    requires vmj.hibernate.integrator;
    requires java.logging;
    
    requires prices.auth.vmj;
}