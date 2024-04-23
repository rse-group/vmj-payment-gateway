package paymentgateway.disbursement.core;

import com.google.gson.Gson;
import java.util.*;
import java.util.logging.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import paymentgateway.disbursement.DisbursementFactory;
import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

public class DisbursementResourceImpl extends DisbursementResourceComponent {
	protected DisbursementResourceComponent record;
	private static final Logger LOGGER = Logger.getLogger(DisbursementResourceImpl.class.getName());

	public Disbursement createDisbursement(VMJExchange vmjExchange,int id, int userId) {
		String bank_code = "";
		String account_number = "";
		try{
			bank_code = (String) vmjExchange.getRequestBodyForm("bank_code");
			account_number = (String) vmjExchange.getRequestBodyForm("account_number");
		}catch (Exception e){
			bank_code = (String) vmjExchange.getRequestBodyForm("beneficiary_bank_name");
			account_number = (String) vmjExchange.getRequestBodyForm("beneficiary_account_number");
		}
		double amount = Double.parseDouble((String) vmjExchange.getRequestBodyForm("amount"));

		Disbursement disbursement = DisbursementFactory.createDisbursement(
				"paymentgateway.disbursement.core.DisbursementImpl",
				id,
				userId,
				account_number,
				amount,
				bank_code);
		Repository.saveObject(disbursement);
		return disbursement;
	}



	public HashMap<String, Object> sendTransaction(VMJExchange vmjExchange, String serviceName) {
		HashMap<String, Object> responseObj = null;
		return responseObj;
	}
	
	@Route(url="call/disbursement/detail")
	public HashMap<String, Object> getDisbursement(VMJExchange vmjExchange){
		int id = ((Double) vmjExchange.getRequestBodyForm("id")).intValue();
		Disbursement disbursementImpl = Repository.getObject(id);
		HashMap<String,Object> disbursementDataMap = disbursementImpl.toHashMap();
		return disbursementDataMap;
	}

	public HashMap<String, Object> getDisbursementById(int id){
		List<HashMap<String,Object>> disbursementList = getAllDisbursement("moneytransfer_impl");
		for (HashMap<String,Object> disbursement : disbursementList){
			int record_id = ((Double) disbursement.get("record_id")).intValue();
			if(record_id == id){
				return disbursement;
			}

		}

		return null;
	}

	@Route(url="call/disbursement/list")
	public List<HashMap<String,Object>> getAllDisbursement(VMJExchange vmjExchange){
		String table = (String) vmjExchange.getRequestBodyForm("table_name");
		List<Disbursement> List = Repository.getAllObject(table);
		return transformListToHashMap(List);
	}

	public List<HashMap<String,Object>> getAllDisbursement(String tableName){
		List<Disbursement> List = Repository.getAllObject(tableName);
		return transformListToHashMap(List);
	}

	public String getParamsUrlEncoded(VMJExchange vmjExchange) {
		ArrayList<String> paramList = new ArrayList<>();
		for (Map.Entry<String, Object> entry : vmjExchange.getPayload().entrySet()) {
			String key = entry.getKey();
			Object val = entry.getValue();
			if (val instanceof String) {
				paramList.add(key + "=" + URLEncoder.encode(val.toString(), StandardCharsets.UTF_8));
			} else if (val instanceof Integer) {
				paramList.add(key + "=" + URLEncoder.encode(val.toString(), StandardCharsets.UTF_8));
			} else if (val instanceof Double) {
				int temp = ((Double) val).intValue();
				paramList.add(key + "=" + URLEncoder.encode(Integer.toString(temp), StandardCharsets.UTF_8));
			}

		}
		String encodedURL = String.join("&",paramList);
		return encodedURL;
	}

	public List<HashMap<String,Object>> transformListToHashMap(List<Disbursement> List){
		List<HashMap<String,Object>> resultList = new ArrayList<HashMap<String,Object>>();
		for(int i = 0; i < List.size(); i++) {
			resultList.add(List.get(i).toHashMap());
		}
		return resultList;
	}

	@Route(url="call/disbursement/delete")
	public List<HashMap<String,Object>> deleteDisbursement(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) {
			return null;
		}
		int id = ((Double) vmjExchange.getRequestBodyForm("id")).intValue();
		Disbursement disbursement = Repository.getObject(id);
		Repository.deleteObject(id);
		return getAllDisbursement(vmjExchange);
	}

	@Route(url="call/disbursement/update")
	public HashMap<String, Object> updateDisbursement(VMJExchange vmjExchange){
		if (vmjExchange.getHttpMethod().equals("OPTIONS")) {
			return null;
		}
		int id = ((Double) vmjExchange.getRequestBodyForm("id")).intValue();
		Disbursement disbursement = Repository.getObject(id);
		try{
			disbursement.setAmount((Double) vmjExchange.getRequestBodyForm("amount"));
			disbursement.setAccountNumber((String) vmjExchange.getRequestBodyForm("account_number"));
			disbursement.setBankCode((String) vmjExchange.getRequestBodyForm("bank_code"));

		}
		catch (Exception e){
			e.printStackTrace();
		}

		Repository.updateObject(disbursement);
		return disbursement.toHashMap();
	}

}
