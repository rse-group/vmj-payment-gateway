module paymentgateway.fundtransfer.infofrombenef {
	requires paymentgateway.fundtransfer.core;
    exports paymentgateway.fundtransfer.infofrombenef;

	requires vmj.routing.route;
	requires vmj.hibernate.integrator;
	requires prices.auth.vmj;
	requires java.logging;
	// https://stackoverflow.com/questions/46488346/error32-13-error-cannot-access-referenceable-class-file-for-javax-naming-re/50568217
	requires java.naming;

	opens paymentgateway.fundtransfer.infofrombenef to org.hibernate.orm.core, gson;
}
