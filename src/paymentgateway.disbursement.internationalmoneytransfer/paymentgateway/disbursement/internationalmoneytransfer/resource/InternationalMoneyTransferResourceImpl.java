package paymentgateway.disbursement.internationalmoneytransfer;

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

import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.DisbursementFactory;
import paymentgateway.disbursement.core.DisbursementImpl;
import paymentgateway.disbursement.core.DisbursementResourceComponent;
import paymentgateway.disbursement.international.InternationalResourceImpl;
import paymentgateway.disbursement.core.MoneyTransferResponse;
import paymentgateway.disbursement.core.Sender;

public class InternationalMoneyTransferResourceImpl extends InternationalResourceImpl {

	public InternationalMoneyTransferResourceImpl(DisbursementResourceComponent record) {
		super(record);
	}

	public Disbursement createDisbursement(VMJExchange vmjExchange, String productName, String serviceName) {
		MoneyTransferResponse response = super.sendTransaction(vmjExchange, serviceName);
		int id = response.getId();
		int user_id = response.getUser_id();
		String status = response.getStatus();
		double exachange_rate = response.getExchange_rate();
		double fee = response.getFee();
		String source_country = response.getSource_country();
		String destination_country = response.getDestination_country();
		double amount_in_sender_currency = response.getAmount();
		String beneficiary_currency_code =  response.getBeneficiary_currency_code();
		Disbursement transaction = record.createDisbursement(vmjExchange,id,user_id);
		Disbursement moneyTransferTransaction = DisbursementFactory.createDisbursement(
				"paymentgateway.disbursement.moneytransfer.MoneyTransferImpl",
				transaction, status);
		Repository.saveObject(moneyTransferTransaction);
		Disbursement internationalMoneyTransferTransaction = DisbursementFactory.createDisbursement(
				"paymentgateway.disbursement.international.InternationalImpl",
				moneyTransferTransaction,
				exachange_rate,
				fee,
				source_country,
				destination_country,
				amount_in_sender_currency,
				beneficiary_currency_code);
		Repository.saveObject(internationalMoneyTransferTransaction);
		return internationalMoneyTransferTransaction;
	}

	@Route(url = "call/international-money-transfer")
	public HashMap<String, Object> moneyTransfer(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS"))
			return null;
		String productName = (String) vmjExchange.getRequestBodyForm("product_name");
		Disbursement result = this.createDisbursement(vmjExchange, productName, "InternationalMoneyTransfer");
		return result.toHashMap();
	}
}

