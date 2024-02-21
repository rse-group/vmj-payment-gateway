package paymentgateway.disbursement.aggregator;

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

public class AggregatorResourceImpl extends AgentResourceImpl {

	public AggregatorResourceImpl(DisbursementResourceComponent record) {
		super(record);
	}
	public Disbursement createDisbursement(VMJExchange vmjExchange, String productName, String serviceName) {
		System.out.println("masuk agent transfer");
		MoneyTransferResponse response = super.sendTransaction(vmjExchange, productName, serviceName);
		System.out.println("1-agg");
		Gson gson = new Gson();
		String jsonResponse = gson.toJson(response);
		
		System.out.println("----- Response -----");
		System.out.println(jsonResponse);
		
		String status = response.getStatus();
		System.out.println("2");
		int id = response.getId();
		System.out.println("id === "+ id);
		System.out.println("3");
		int user_id = response.getUser_id();
		System.out.println("4");
		String agent_id_str = (String) vmjExchange.getRequestBodyForm("agent_id");
		int agent_id = Integer.valueOf(agent_id_str);
		System.out.println("5");
		String direction = (String) vmjExchange.getRequestBodyForm("direction");
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

	@Route(url = "call/aggregator-transfer")
	public HashMap<String, Object> moneyTransfer(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS"))
			return null;
		String productName = (String) vmjExchange.getRequestBodyForm("product_name");
		Disbursement result = this.createDisbursement(vmjExchange, productName, "AgentMoneyTransfer");
		return result.toHashMap();
	}

}


