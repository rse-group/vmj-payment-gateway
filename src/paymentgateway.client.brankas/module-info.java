module paymentgateway.client.brankas {
	requires paymentgateway.client.core;
    exports paymentgateway.client.brankas;

	requires vmj.routing.route;
	// https://stackoverflow.com/questions/46488346/error32-13-error-cannot-access-referenceable-class-file-for-javax-naming-re/50568217
	requires java.naming;
	requires java.net.http;
	requires paymentgateway.apiauth.apikey;

	opens paymentgateway.client.brankas to gson;
}
