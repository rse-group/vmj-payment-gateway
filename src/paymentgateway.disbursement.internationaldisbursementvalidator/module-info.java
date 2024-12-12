module paymentgateway.disbursement.internationaldisbursementvalidator {
    requires paymentgateway.disbursement.core;

    exports paymentgateway.disbursement.internationaldisbursementvalidator;

    requires paymentgateway.config.core;
    requires vmj.routing.route;
}
