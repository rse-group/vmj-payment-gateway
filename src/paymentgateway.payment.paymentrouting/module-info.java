module paymentgateway.payment.paymentrouting {
	requires paymentgateway.payment.core;
    exports paymentgateway.payment.paymentrouting;

	requires vmj.routing.route;
	requires paymentgateway.config.core;
	requires paymentgateway.config.midtrans;
	requires paymentgateway.config.oy;
	requires vmj.hibernate.integrator;
	requires prices.auth.vmj;
	requires java.logging;
	// https://stackoverflow.com/questions/46488346/error32-13-error-cannot-access-referenceable-class-file-for-javax-naming-re/50568217
	requires java.naming;
	requires java.net.http;

	opens paymentgateway.payment.paymentrouting to org.hibernate.orm.core, gson;
}
