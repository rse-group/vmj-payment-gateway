package paymentgateway.disbursement.specialmoneytransfer;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import paymentgateway.disbursement.DisbursementFactory;
import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementResourceDecorator;
import paymentgateway.disbursement.core.DisbursementImpl;
import paymentgateway.disbursement.core.DisbursementResourceComponent;
import paymentgateway.disbursement.DisbursementFactory;
import paymentgateway.disbursement.core.MoneyTransferResponse;
import paymentgateway.disbursement.special.SpecialResourceImpl;

public class SpecialMoneyTransferResourceImpl extends SpecialResourceImpl {

	public SpecialMoneyTransferResourceImpl(DisbursementResourceComponent record) {
		super(record);
	}

	public Disbursement createDisbursement(VMJExchange vmjExchange, String productName, String serviceName) {
		MoneyTransferResponse response = super.sendTransaction(vmjExchange,productName, serviceName);
		String status = response.getStatus();
		int id = response.getId();
		int userId = response.getUser_id();
		int sender_country = response.getSender().getSender_country();
		String sender_name = response.getSender().getSender_name();
		String sender_address = response.getSender().getSender_address();
		String sender_job = response.getSender().getSender_job();
		String direction = response.getDirection();

		Disbursement transaction = record.createDisbursement(vmjExchange,id,userId);
		Disbursement moneyTransferTransaction = DisbursementFactory.createDisbursement(
				"paymentgateway.disbursement.moneytransfer.MoneyTransferImpl",
				transaction, status);
		Repository.saveObject(moneyTransferTransaction);
		Disbursement approvalTransaction = DisbursementFactory.createDisbursement(
				"paymentgateway.disbursement.special.SpecialImpl",
				moneyTransferTransaction,
				sender_country,
				sender_name,
				sender_address,
				sender_job,
				direction);
		Repository.saveObject(approvalTransaction);
		return approvalTransaction;
	}

	@Route(url = "call/special-money-transfer")
	public HashMap<String, Object> specialMoneyTransfer(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS"))
			return null;
		String productName = (String) vmjExchange.getRequestBodyForm("product_name");
		Disbursement result = this.createDisbursement(vmjExchange, productName, "SpecialMoneyTransfer");
		return result.toHashMap();
	}
}


