module paymentgateway.paymentinterface.midtrans_paymentlink {
	requires paymentgateway.paymentinterface.core;
	requires paymentgateway.paymentinterface.paymentlink;
	exports paymentgateway.paymentinterface.midtrans_paymentlink;
	
	requires vmj.routing.route;
	requires vmj.hibernate.integrator;
	requires prices.auth.vmj;
	requires java.logging;
	// https://stackoverflow.com/questions/46488346/error32-13-error-cannot-access-referenceable-class-file-for-javax-naming-re/50568217
	requires java.naming;
	requires java.net.http;
	
	opens paymentgateway.paymentinterface.midtrans_paymentlink to org.hibernate.orm.core, gson;
}