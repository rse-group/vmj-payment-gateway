module paymentgateway.paymentinterface.oy_paymentrouting {
	requires paymentgateway.paymentinterface.core;
	requires paymentgateway.paymentinterface.paymentrouting;
	exports paymentgateway.paymentinterface.oy_paymentrouting;
	
	requires vmj.routing.route;
	requires vmj.hibernate.integrator;
	requires prices.auth.vmj;
	requires java.logging;
	// https://stackoverflow.com/questions/46488346/error32-13-error-cannot-access-referenceable-class-file-for-javax-naming-re/50568217
	requires java.naming;
	requires java.net.http;
	
	opens paymentgateway.paymentinterface.oy_paymentrouting to org.hibernate.orm.core, gson;
}