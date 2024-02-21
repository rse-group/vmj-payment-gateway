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
import paymentgateway.disbursement.DisbursementFactory;

public class ApprovalSystemResourceImpl extends DisbursementResourceDecorator {
	public ApprovalSystemResourceImpl(DisbursementResourceComponent record) {
		super(record);
	}

	public Disbursement createDisbursement(VMJExchange vmjExchange, String productName, String serviceName) {
		MoneyTransferResponse response = super.sendTransaction(vmjExchange, productName, serviceName);
		int id = response.getId();
		int user_id = response.getUser_id();
		String status = response.getStatus();
		int approverId = response.getApprover_id();
		String approvalStatus = response.getApprovalStatus();
		Disbursement transaction = record.createDisbursement(vmjExchange,id,user_id);
		Disbursement moneyTransferTransaction = DisbursementFactory.createDisbursement(
				"paymentgateway.disbursement.moneytransfer.MoneyTransferImpl",
				transaction, status);
		Repository.saveObject(moneyTransferTransaction);
		Disbursement approvalSystemMoneyTransferTransaction = DisbursementFactory.createDisbursement(
				"paymentgateway.disbursement.approvalsystem.ApprovalSystemImpl",
				moneyTransferTransaction,
				approverId,
				approvalStatus
				);
		Repository.saveObject(approvalSystemMoneyTransferTransaction);
		return approvalSystemMoneyTransferTransaction;
	}

	@Route(url = "call/approval-transfer/approve")
	public void approveTransfer(VMJExchange vmjExchange) {
		int id = ((Double) vmjExchange.getRequestBodyForm("id")).intValue();
		GetAllDisbursementResponse dosmmesticData = record.getAllDataFromAPI("disbursement");
		List<MoneyTransferResponse> dosmesticTransferData = dosmmesticData.getData();
		for(MoneyTransferResponse response :  dosmesticTransferData){
			if(response.getId() == id){
				response.setApprovalStatus("APPROVED");
				System.out.println(response.getStatus());
			}
		}
	}

	@Route(url = "call/approval-transfer/reject")
	public void rejectTransfer(VMJExchange vmjExchange) {
		int id = ((Double) vmjExchange.getRequestBodyForm("id")).intValue();
		GetAllDisbursementResponse dosmmesticData = record.getAllDataFromAPI("disbursement");
		List<MoneyTransferResponse> dosmesticTransferData = dosmmesticData.getData();
		for(MoneyTransferResponse response :  dosmesticTransferData){
			if(response.getId() == id){
				response.setApprovalStatus("REJECTED");
				System.out.println(response.getStatus());
			}
		}
	}

	@Route(url = "call/approval-transfer")
	public HashMap<String, Object> approvalTransfer(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("OPTIONS"))
			return null;
		String productName = (String) vmjExchange.getRequestBodyForm("product_name");
		Disbursement result = this.createDisbursement(vmjExchange, productName, "MoneyTransfer");
		return result.toHashMap();
	}
}
