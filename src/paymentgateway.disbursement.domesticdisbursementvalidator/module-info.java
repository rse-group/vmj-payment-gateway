module paymentgateway.disbursement.domesticdisbursementvalidator {
    requires paymentgateway.disbursement.core;

    exports paymentgateway.disbursement.domesticdisbursementvalidator;

    requires paymentgateway.config.core;
    requires vmj.routing.route;
}
