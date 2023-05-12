package paymentgateway.disbursement.core;

import java.util.*;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import paymentgateway.disbursement.DisbursementFactory;
import prices.auth.vmj.annotations.Restricted;
//add other required packages

public class DisbursementResourceImpl extends DisbursementResourceComponent {
	protected DisbursementResourceComponent record;

	public Disbursement createDisbursement(VMJExchange vmjExchange, String userId) {
		System.out.println("111");
		String id = UUID.randomUUID().toString();
		System.out.println("222");
//		String user_id = (String) vmjExchange.get("user_id");
		String bank_code = (String) vmjExchange.getRequestBodyForm("bank_code");
		String account_number = (String) vmjExchange.getRequestBodyForm("account_number");
		System.out.println("account_number: " + account_number);
		System.out.println("1");
		int amount = ((Double) vmjExchange.getRequestBodyForm("amount")).intValue();
		System.out.println(amount);
		System.out.println("2");

		Disbursement disbursement = DisbursementFactory.createDisbursement(
				"paymentgateway.disbursement.core.DisbursementImpl",
				id,
				userId,
				account_number,
				amount,
				bank_code);
		save(disbursement);
		return disbursement;
	}

	public void save(Disbursement disbursement){
		System.out.println("save 1");
		Repository.saveObject(disbursement);
		System.out.println("save 2");;
	}


	public void getDisbursementByID(String id) {
		// TODO: implement this method
	}
}
