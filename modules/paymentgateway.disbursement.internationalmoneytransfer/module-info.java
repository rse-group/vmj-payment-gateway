module paymentgateway.disbursement.internationalmoneytransfer {
    requires paymentgateway.disbursement.core;

    exports paymentgateway.disbursement.internationalmoneytransfer;

    requires vmj.routing.route;
}
