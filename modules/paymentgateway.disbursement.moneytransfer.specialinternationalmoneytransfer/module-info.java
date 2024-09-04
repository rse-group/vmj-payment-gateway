module paymentgateway.disbursement.moneytransfer.specialinternationalmoneytransfer {
	requires paymentgateway.disbursement.core;

	exports paymentgateway.disbursement.moneytransfer.specialinternationalmoneytransfer;

	requires paymentgateway.config.core;
	requires vmj.routing.route;
	// https://stackoverflow.com/questions/46488346/error32-13-error-cannot-access-referenceable-class-file-for-javax-naming-re/50568217
	requires java.naming;
	requires java.net.http;

	opens paymentgateway.disbursement.moneytransfer.specialinternationalmoneytransfer to org.hibernate.orm.core, gson;
}
