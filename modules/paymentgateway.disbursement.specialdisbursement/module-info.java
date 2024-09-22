module paymentgateway.disbursement.specialdisbursement {
    requires paymentgateway.disbursement.core;
    requires paymentgateway.disbursement.special;
    requires paymentgateway.disbursement.domesticdisbursementvalidator;
    requires paymentgateway.disbursement.internationaldisbursementvalidator;

    exports paymentgateway.disbursement.specialdisbursement;

    requires vmj.routing.route;
}
