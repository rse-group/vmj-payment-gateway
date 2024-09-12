module paymentgateway.disbursement.internationalmoneytransfer {
    requires paymentgateway.disbursement.core;

    exports paymentgateway.disbursement.internationalmoneytransfer;

    requires vmj.routing.route;

	requires paymentgateway.config.core;

    requires java.naming;
	requires java.net.http;
	requires java.logging;
}
