package paymentgateway.disbursement.approvaltransfer;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import paymentgateway.disbursement.core.DisbursementResourceComponent;
import paymentgateway.disbursement.approval.ApprovalResourceImpl;

public class ApprovalTransferResourceImpl extends ApprovalResourceImpl {

	public ApprovalTransferResourceImpl(DisbursementResourceComponent record) {
		super(record);
	}
}

