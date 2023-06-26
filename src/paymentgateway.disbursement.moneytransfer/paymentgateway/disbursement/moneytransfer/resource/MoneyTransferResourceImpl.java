package paymentgateway.disbursement.moneytransfer;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.util.*;
import vmj.hibernate.integrator.RepositoryUtil;
import java.nio.charset.StandardCharsets;

import paymentgateway.disbursement.core.*;
import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementImpl;
import paymentgateway.disbursement.core.DisbursementResourceComponent;
import paymentgateway.disbursement.core.DisbursementResourceDecorator;
import paymentgateway.disbursement.core.GetAllDisbursementResponse;
import paymentgateway.disbursement.core.MoneyTransferResponse;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;


import paymentgateway.disbursement.DisbursementFactory;

public class MoneyTransferResourceImpl extends DisbursementResourceDecorator {
	RepositoryUtil<MoneyTransferImpl> moneyTransferRepository;
	public MoneyTransferResourceImpl(DisbursementResourceComponent record) {
		super(record);
		this.moneyTransferRepository = new RepositoryUtil<MoneyTransferImpl>(paymentgateway.disbursement.moneytransfer.MoneyTransferImpl.class);
	}

	public Disbursement createDisbursement(VMJExchange vmjExchange, String productName, String serviceName) {
		System.out.println("1");
		MoneyTransferResponse response = record.sendTransaction(vmjExchange, productName, serviceName);
		System.out.println("2");
		int id = response.getId();
		int userId = response.getUser_id();
		String status = response.getStatus();
		System.out.println("status: " + status);
		Disbursement transaction = record.createDisbursement(vmjExchange,id,userId);
		System.out.println("3");
		Disbursement moneyTransferTransaction = DisbursementFactory.createDisbursement(
				"paymentgateway.disbursement.moneytransfer.MoneyTransferImpl",
				transaction, status);
		System.out.println("4");
		Repository.saveObject(moneyTransferTransaction);
		return moneyTransferTransaction;
	}

	@Route(url = "call/money-transfer/update-status")
	public void updateStatus(VMJExchange vmjExchange){
		GetAllDisbursementResponse dosmmesticData = record.getAllDataFromAPI("disbursement");
		GetAllDisbursementResponse internationalData = record.getAllDataFromAPI("international-disbursement");
		List<MoneyTransferResponse> dosmesticTransferData = dosmmesticData.getData();
		List<MoneyTransferResponse> internationalTransferData = internationalData.getData();
		List<MoneyTransferImpl> moneyTransfers = getPendingStatus();

		if(moneyTransfers.size() != 0){
			for(MoneyTransferResponse response :  dosmesticTransferData){
				int id = response.getId();
				try {
					MoneyTransferImpl moneyTransfer = getPendingMoneyTransferById(moneyTransfers,id);
					if(moneyTransfer == null){
						continue;
					}
					moneyTransfer.setStatus(response.getStatus());
					moneyTransferRepository.updateObject(moneyTransfer);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for(MoneyTransferResponse response :  internationalTransferData){
				int id = response.getId();
				try {
					MoneyTransferImpl moneyTransfer = getPendingMoneyTransferById(moneyTransfers,id);
					if(moneyTransfer == null){
						continue;
					}
					moneyTransfer.setStatus(response.getStatus());
					moneyTransferRepository.updateObject(moneyTransfer);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	public MoneyTransferImpl getPendingMoneyTransferById(List<MoneyTransferImpl> moneyTransfers, int id){
		for(MoneyTransferImpl moneyTransfer : moneyTransfers){
			if(moneyTransfer.getId() == id){
				return moneyTransfer;
			}
		}
		return null;
	}

	public List<MoneyTransferImpl> getPendingStatus(){
		List<MoneyTransferImpl> result = new ArrayList<>();
		List<MoneyTransferImpl> moneyTransfers = moneyTransferRepository.getAllObject("moneytransfer_impl");
		for(MoneyTransferImpl moneyTransfer : moneyTransfers){
			if(moneyTransfer.getStatus() == "PENDING"){
				result.add(moneyTransfer);
			}
		}
		return result;
	}


	@Route(url = "call/money-transfer")
	public HashMap<String, Object> moneyTransfer(VMJExchange vmjExchange) {
		 if (vmjExchange.getHttpMethod().equals("OPTIONS"))
		 return null;
		System.out.println("routing method");
		String productName = (String) vmjExchange.getRequestBodyForm("product_name");
		Disbursement result = this.createDisbursement(vmjExchange, productName, "MoneyTransfer");
		return result.toHashMap();
	}


}


