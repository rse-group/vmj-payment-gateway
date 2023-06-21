package paymentgateway.disbursement.agentmoneytransfer;

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

import java.nio.charset.StandardCharsets;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import paymentgateway.disbursement.DisbursementFactory;
import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementResourceDecorator;
import paymentgateway.disbursement.core.DisbursementImpl;
import paymentgateway.disbursement.core.DisbursementResourceComponent;
import paymentgateway.disbursement.agent.*;
import paymentgateway.disbursement.core.GetAllDisbursementResponse;
import paymentgateway.disbursement.core.MoneyTransferResponse;

public class AgentMoneyTransferResourceImpl extends AgentResourceImpl {

	public AgentMoneyTransferResourceImpl(DisbursementResourceComponent record) {
		super(record);
	}
	public Disbursement createDisbursement(VMJExchange vmjExchange) {
		System.out.println("masuk agent transfer");
		MoneyTransferResponse response = super.sendTransaction(vmjExchange,"FlipAgentMoneyTransfer");
		System.out.println("1");
		String status = response.getStatus();
		System.out.println("2");
		int id = response.getId();
		System.out.println("3");
		int user_id = response.getUser_id();
		System.out.println("4");
		int agent_id = response.getAgent_id();
		System.out.println("5");
		String direction = response.getDirection();
		System.out.println("6");

		Disbursement transaction = record.createDisbursement(vmjExchange,id,user_id);
		System.out.println("7");
		Disbursement moneyTransferTransaction = DisbursementFactory.createDisbursement(
				"paymentgateway.disbursement.moneytransfer.MoneyTransferImpl",
				transaction, status);
		Repository.saveObject(moneyTransferTransaction);
		System.out.println("11b");
		Disbursement agentTransaction = DisbursementFactory.createDisbursement(
				"paymentgateway.disbursement.agent.AgentImpl",
				moneyTransferTransaction,
				agent_id,
				direction);
		Repository.saveObject(agentTransaction);
		return agentTransaction;
	}

	@Route(url = "call/agent-money-transfer")
	public HashMap<String, Object> moneyTransfer(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS"))
			return null;
		Disbursement result = this.createDisbursement(vmjExchange);
		return result.toHashMap();
	}

}


