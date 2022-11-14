module paymentgateway.paymentchannel.midtrans_debitcard {
	requires paymentgateway.paymentchannel.core;
	requires paymentgateway.paymentchannel.debitcard;
	exports paymentgateway.paymentchannel.midtrans_debitcard;
	
	requires vmj.routing.route;
	requires vmj.hibernate.integrator;
	requires prices.auth.vmj;
	requires java.logging;
	// https://stackoverflow.com/questions/46488346/error32-13-error-cannot-access-referenceable-class-file-for-javax-naming-re/50568217
	requires java.naming;
	requires java.net.http;
	
	opens paymentgateway.paymentchannel.midtrans_debitcard to org.hibernate.orm.core, gson;
}