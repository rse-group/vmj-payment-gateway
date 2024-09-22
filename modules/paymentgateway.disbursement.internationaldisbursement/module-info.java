module paymentgateway.disbursement.internationaldisbursement {
    requires paymentgateway.disbursement.core;
    requires paymentgateway.disbursement.international;
    requires paymentgateway.disbursement.internationaldisbursementvalidator;

    exports paymentgateway.disbursement.internationaldisbursement;

    requires vmj.routing.route;
}
