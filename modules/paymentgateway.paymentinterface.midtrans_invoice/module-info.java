module paymentgateway.paymentinterface.midtrans_invoice {
	requires paymentgateway.paymentinterface.core;
	requires paymentgateway.paymentinterface.invoice;
	exports paymentgateway.paymentinterface.midtrans_invoice;
	
	requires vmj.routing.route;
	requires vmj.hibernate.integrator;
	requires prices.auth.vmj;
	requires java.logging;
	// https://stackoverflow.com/questions/46488346/error32-13-error-cannot-access-referenceable-class-file-for-javax-naming-re/50568217
	requires java.naming;
	
	requires com.midtrans;
    uses com.midtrans.Midtrans;
    uses com.midtrans.httpclient.SnapApi;
    uses com.midtrans.httpclient.error.MidtransError;
    
    opens paymentgateway.paymentinterface.midtrans_invoice to org.hibernate.orm.core, gson;
}