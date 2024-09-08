module paymentgateway.disbursement.agentmoneytransfer {
    requires paymentgateway.disbursement.core;

    exports paymentgateway.disbursement.agentmoneytransfer;

    requires vmj.routing.route;
}
