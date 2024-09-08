module paymentgateway.disbursement.specialmoneytransfer {
    requires paymentgateway.disbursement.core;

    exports paymentgateway.disbursement.specialmoneytransfer;

    requires vmj.routing.route;
    requires java.naming;
	requires java.net.http;
	requires java.logging;
}
