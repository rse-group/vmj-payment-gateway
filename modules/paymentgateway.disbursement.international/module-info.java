module paymentgateway.disbursement.international {
	requires paymentgateway.disbursement.core;

	requires paymentgateway.disbursement.moneytransfer;
	exports paymentgateway.disbursement.international;

	requires vmj.routing.route;
	requires vmj.hibernate.integrator;
	requires vmj.auth;
	requires java.logging;
	// https://stackoverflow.com/questions/46488346/error32-13-error-cannot-access-referenceable-class-file-for-javax-naming-re/50568217
	requires java.naming;
	requires java.net.http;

	opens paymentgateway.disbursement.international to org.hibernate.orm.core, gson;
}
