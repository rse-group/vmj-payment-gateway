module paymentgateway.disbursement.agentdisbursement {
	requires paymentgateway.disbursement.core;
	requires paymentgateway.disbursement.agent;
	requires paymentgateway.disbursement.domesticdisbursementvalidator;

	exports paymentgateway.disbursement.agentdisbursement;

	requires vmj.routing.route;
}
