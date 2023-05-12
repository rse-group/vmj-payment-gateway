module paymentgateway.disbursement.scheduledtransfer {
	requires paymentgateway.disbursement.core;
	requires paymentgateway.disbursement.moneytransfer;
	requires paymentgateway.disbursement.scheduled;
    exports paymentgateway.disbursement.scheduledtransfer;

	requires vmj.routing.route;
	requires vmj.hibernate.integrator;
	requires prices.auth.vmj;
	requires java.logging;
	// https://stackoverflow.com/questions/46488346/error32-13-error-cannot-access-referenceable-class-file-for-javax-naming-re/50568217
	requires java.naming;
	requires java.net.http;
	
	opens paymentgateway.disbursement.scheduledtransfer to org.hibernate.orm.core, gson;
}
