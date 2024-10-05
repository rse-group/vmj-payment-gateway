module paymentgateway.payment.invoice {
	requires paymentgateway.payment.core;
    exports paymentgateway.payment.invoice;

	requires vmj.routing.route;
	requires paymentgateway.config.core;
	requires vmj.hibernate.integrator;
	requires vmj.auth;
	requires java.logging;
	// https://stackoverflow.com/questions/46488346/error32-13-error-cannot-access-referenceable-class-file-for-javax-naming-re/50568217
	requires java.naming;
	requires java.net.http;
	
	// requires com.midtrans;
    // uses com.midtrans.Midtrans;
    // uses com.midtrans.httpclient.SnapApi;
    // uses com.midtrans.httpclient.error.MidtransError;

	opens paymentgateway.payment.invoice to org.hibernate.orm.core, gson;
}
