package paymentgateway.disbursement.fixedcurrency;

import java.util.*;

import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementService;
import paymentgateway.disbursement.core.DisbursementServiceDecorator;
import paymentgateway.disbursement.core.DisbursementServiceComponent;
import paymentgateway.disbursement.DisbursementServiceFactory;

public class DisbursementServiceImpl extends DisbursementServiceDecorator {
    private static DisbursementService RESOURCE;
    private final String[] DELTA_MODULES = {
		"paymentgateway.disbursement.special.DisbursementServiceImpl",
		"paymentgateway.disbursement.domesticdisbursementvalidator.DisbursementServiceImpl",
		"paymentgateway.disbursement.internationaldisbursementvalidator.DisbursementServiceImpl"
	};

    public DisbursementServiceImpl(DisbursementServiceComponent record) {
		super(record);
		RESOURCE = DisbursementServiceFactory.createDisbursementService(
			"paymentgateway.disbursement.core.DisbursementServiceImpl");

		for (int i = 0; i < DELTA_MODULES.length; i++) {
			RESOURCE = DisbursementServiceFactory.createDisbursementService(
				DELTA_MODULES[i],
				RESOURCE
			);
		}

		record = (DisbursementServiceComponent) RESOURCE;
	}

	public int callback(Map<String, Object> requestBody) {
		return RESOURCE.callback(requestBody);
	}

	public Disbursement createDisbursement(Map<String, Object> requestBody) {
		return RESOURCE.createDisbursement(requestBody);
	}

	public Disbursement createDisbursement(Map<String, Object> requestBody, Map<String, Object> response) {
		return RESOURCE.createDisbursement(requestBody, response);
	}

	public HashMap<String, Object> getDisbursement(Map<String, Object> requestBody) {
		return RESOURCE.getDisbursement(requestBody);
	}

	public List<HashMap<String, Object>> getAllDisbursement(Map<String, Object> requestBody) {
		return RESOURCE.getAllDisbursement(requestBody);
	}

	public List<HashMap<String, Object>> deleteDisbursement(Map<String, Object> requestBody) {
		return RESOURCE.deleteDisbursement(requestBody);
	}

	public HashMap<String, Object> updateDisbursement(Map<String, Object> requestBody) {
		return RESOURCE.updateDisbursement(requestBody);
	}

	public List<HashMap<String, Object>> transformListToHashMap(List<Disbursement> List) {
		return RESOURCE.transformListToHashMap(List);
	}

	public Map<String, Object> sendTransaction(Map<String, Object> requestBody) {
		return RESOURCE.sendTransaction(requestBody);
	}

	public HashMap<String, Object> getDisbursementById(int id) {
		return RESOURCE.getDisbursementById(id);
	}
}