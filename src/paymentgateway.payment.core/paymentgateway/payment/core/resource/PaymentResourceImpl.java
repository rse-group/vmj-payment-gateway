package paymentgateway.payment.core;

import java.math.BigInteger;
import java.util.*;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import paymentgateway.payment.PaymentFactory;
import prices.auth.vmj.annotations.Restricted;
//add other required packages

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

public class PaymentResourceImpl extends PaymentResourceComponent {
	protected PaymentResourceComponent record;

	public Payment createPayment(VMJExchange vmjExchange, int id) {
		Config config = ConfigFactory.createConfig(ConfigFactory.createConfig("paymentgateway.config.core.ConfigImpl"));
		String productName = config.getProductName();
//		String id = (String) vmjExchange.getRequestBodyForm(bodyKeys.get("id"));
//		int amount = (int) vmjExchange.getRequestBodyForm(bodyKeys.get("amount"));
		double amount = Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount"));
//		String generateUUIDNo = String.format("%010d",new BigInteger(UUID.randomUUID().toString().replace("-",""),16));
//		String unique_no = generateUUIDNo.substring(0,5);
//		int id = Integer.parseInt(unique_no);
		Payment transaction = PaymentFactory.createPayment("paymentgateway.payment.core.PaymentImpl",
				id,
				productName,
				amount);
		sendTransaction();
		PaymentRepository.saveObject(transaction);
		return transaction;
	}

	private void sendTransaction() {
		// to do implement this in deltas
	}

	@Route(url = "call/payment/list")
	public List<HashMap<String,Object>> getAll(VMJExchange vmjExchange) {
		String name = (String) vmjExchange.getRequestBodyForm("table_name");
		List<Payment> paymentVariation = PaymentRepository.getAllObject(name);
		return transformListToHashMap(paymentVariation);
	}

	public List<HashMap<String,Object>> getAll(String name) {
		List<Payment> paymentVariation = PaymentRepository.getAllObject(name);
		return transformListToHashMap(paymentVariation);
	}

	public List<HashMap<String,Object>> transformListToHashMap(List<Payment> List){
		List<HashMap<String,Object>> resultList = new ArrayList<HashMap<String,Object>>();
		for(int i = 0; i < List.size(); i++) {
			resultList.add(List.get(i).toHashMap());
		}
		return resultList;
	}

	public void deletePayment(VMJExchange vmjExchange){
		System.out.println("hello");
	}
}
