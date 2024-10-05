package paymentgateway.disbursement.core;

import java.util.*;

import vmj.hibernate.integrator.RepositoryUtil;
import vmj.routing.route.VMJExchange;

import paymentgateway.disbursement.core.Disbursement;

public abstract class DisbursementServiceComponent implements DisbursementService {
    protected RepositoryUtil<Disbursement> Repository;

    public DisbursementServiceComponent() {
        this.Repository = new RepositoryUtil<Disbursement>(
                paymentgateway.disbursement.core.DisbursementComponent.class);
    }

    public abstract Disbursement createDisbursement(Map<String, Object> requestBody);
    public abstract Disbursement createDisbursement(Map<String, Object> requestBody, Map<String, Object> response);
    public abstract HashMap<String, Object> getDisbursement(VMJExchange vmjExchange);
    public abstract List<HashMap<String, Object>> getAllDisbursement(VMJExchange vmjExchange);
    public abstract List<HashMap<String, Object>> deleteDisbursement(VMJExchange vmjExchange);
    public abstract HashMap<String, Object> updateDisbursement(VMJExchange vmjExchange);
    public abstract List<HashMap<String, Object>> transformListToHashMap(List<Disbursement> List);
    public abstract Map<String, Object> sendTransaction(Map<String, Object> requestBody);
    public abstract HashMap<String, Object> getDisbursementById(int id);
}
