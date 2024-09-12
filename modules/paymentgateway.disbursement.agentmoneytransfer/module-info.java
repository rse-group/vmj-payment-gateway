module paymentgateway.disbursement.agentmoneytransfer {
    requires paymentgateway.disbursement.core;

    exports paymentgateway.disbursement.agentmoneytransfer;

    requires vmj.routing.route;
    

	requires paymentgateway.config.core;

    requires java.naming;
	requires java.net.http;
	requires java.logging;
}
