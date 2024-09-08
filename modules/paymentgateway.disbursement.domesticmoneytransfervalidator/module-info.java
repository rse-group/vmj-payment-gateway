module paymentgateway.disbursement.domesticmoneytransfervalidator {
    requires paymentgateway.disbursement.core;

    exports paymentgateway.disbursement.domesticmoneytransfervalidator;

    requires paymentgateway.config.core;
    requires vmj.routing.route;
}
