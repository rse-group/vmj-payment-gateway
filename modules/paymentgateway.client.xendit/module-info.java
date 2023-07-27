module paymentgateway.client.xendit {
	requires paymentgateway.client.core;
    exports paymentgateway.client.xendit;

	requires vmj.routing.route;
	requires java.logging;
	// https://stackoverflow.com/questions/46488346/error32-13-error-cannot-access-referenceable-class-file-for-javax-naming-re/50568217
	requires java.naming;
	requires java.net.http;
	requires paymentgateway.apiauth.basicauth;

	opens paymentgateway.client.xendit to org.hibernate.orm.core, gson;
}
