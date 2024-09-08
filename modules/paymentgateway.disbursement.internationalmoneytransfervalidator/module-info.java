module paymentgateway.disbursement.internationalmoneytransfervalidator {
    requires paymentgateway.disbursement.core;

    exports paymentgateway.disbursement.internationalmoneytransfervalidator;

    requires paymentgateway.config.core;
    requires vmj.routing.route;
}
