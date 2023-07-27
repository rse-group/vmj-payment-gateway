module paymentgateway.client.oy {
	requires paymentgateway.client.core;
	exports paymentgateway.client.oy;

	requires java.logging;
	// https://stackoverflow.com/questions/46488346/error32-13-error-cannot-access-referenceable-class-file-for-javax-naming-re/50568217
	requires java.naming;
	requires java.net.http;
	requires vmj.routing.route;
	requires paymentgateway.apiauth.apikey;

	opens paymentgateway.client.oy to gson;
}
