package paymentgateway.disbursement.approvalsystem;

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

public class ApprovalSystemResourceImpl extends DisbursementResourceDecorator {
	RepositoryUtil<MoneyTransferImpl> moneyTransferRepository;
	public ApprovalSystemResourceImpl(DisbursementResourceComponent record) {
		super(record);
	}

	public Disbursement createDisbursement(VMJExchange vmjExchange, String productName, String serviceName) {
		MoneyTransferResponse response = super.sendTransaction(vmjExchange, productName, serviceName);
		int id = response.getId();
		int user_id = response.getUser_id();
		String status = response.getStatus();
		String approvedId = response.getApprover_id();
		Disbursement transaction = record.createDisbursement(vmjExchange,id,user_id);
		Disbursement moneyTransferTransaction = DisbursementFactory.createDisbursement(
				"paymentgateway.disbursement.moneytransfer.MoneyTransferImpl",
				transaction, status);
		Repository.saveObject(moneyTransferTransaction);
		Disbursement approvalSystemMoneyTransferTransaction = DisbursementFactory.createDisbursement(
				"paymentgateway.disbursement.approvalsystem.ApprovalSystemImpl",
				moneyTransferTransaction,
				approverId
				);
		Repository.saveObject(approvalSystemMoneyTransferTransaction);
		return approvalSystemMoneyTransferTransaction;
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

	@Route(url = "call/approval-transfer/approve")
	public void approveTransfer(int transactionId) {
		List<ApprovalSystemImpl> approvalTransfer = approvalSystemRepository.getAllObject("approvalsystem_impl");
		for(ApprovalSystemImpl approvalTransfer : approvalTransfer){
			if(approvalTransfer.getId() == transactionId){
				approvalTransfer.setStatus("");
			}
		}
	}

	@Route(url = "call/approval-transfer/reject")
	public void rejectTransfer(int transactionId) {
		List<ApprovalSystemImpl> approvalTransfer = approvalSystemRepository.getAllObject("approvalsystem_impl");
		for(ApprovalSystemImpl approvalTransfer : approvalTransfer){
			if(approvalTransfer.getId() == transactionId){
				approvalTransfer.setStatus("");
			}
		}
	}


	@Route(url = "call/approval-transfer")
	public HashMap<String, Object> approvalTransfer(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS"))
			return null;
		String productName = (String) vmjExchange.getRequestBodyForm("product_name");
		Disbursement result = this.createDisbursement(vmjExchange, productName, "ApprovalTransfer");
		return result.toHashMap();
	}
}
