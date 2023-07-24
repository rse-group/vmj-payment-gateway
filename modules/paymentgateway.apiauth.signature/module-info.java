module paymentgateway.apiauth.signature {
	requires paymentgateway.apiauth.core;
    exports paymentgateway.apiauth.signature;

	requires vmj.routing.route;
	requires vmj.hibernate.integrator;
	requires prices.auth.vmj;
	requires java.logging;
	// https://stackoverflow.com/questions/46488346/error32-13-error-cannot-access-referenceable-class-file-for-javax-naming-re/50568217
	requires java.naming;

	opens paymentgateway.apiauth.signature to org.hibernate.orm.core, gson;
}
