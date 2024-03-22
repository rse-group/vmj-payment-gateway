package paymentgateway.disbursement.core;

import java.util.*;

import vmj.hibernate.integrator.RepositoryUtil;
import vmj.routing.route.VMJExchange;

import paymentgateway.disbursement.core.Disbursement;

public abstract class DisbursementResourceComponent implements DisbursementResource {
    protected RepositoryUtil<Disbursement> Repository;

    public DisbursementResourceComponent() {
        this.Repository = new RepositoryUtil<Disbursement>(
                paymentgateway.disbursement.core.DisbursementComponent.class);
    }

    public abstract Disbursement createDisbursement(VMJExchange vmjExchange,int id, int userId);
    public abstract HashMap<String, Object> getDisbursement(VMJExchange vmjExchange);
    public abstract List<HashMap<String,Object>> getAllDisbursement(VMJExchange vmjExchange);
    public abstract List<HashMap<String,Object>> transformListToHashMap(List<Disbursement> List);
    public abstract List<HashMap<String,Object>> deleteDisbursement(VMJExchange vmjExchange);
    public abstract HashMap<String, Object> updateDisbursement(VMJExchange vmjExchange);
    public abstract GetAllDisbursementResponse getAllDataFromAPI(String name);
    public abstract MoneyTransferResponse sendTransaction(VMJExchange vmjExchange, String serviceName);
    public abstract String getParamsUrlEncoded(Map<String, Object> vmjExchange);
    public abstract HashMap<String, Object> getDisbursementById(int id);

}
