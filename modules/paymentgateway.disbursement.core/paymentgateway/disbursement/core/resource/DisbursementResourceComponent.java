package paymentgateway.disbursement.core;

import java.util.*;

import vmj.hibernate.integrator.RepositoryUtil;
import vmj.routing.route.VMJExchange;

import paymentgateway.disbursement.core.Disbursement;

public abstract class DisbursementResourceComponent implements DisbursementResource {

    public DisbursementResourceComponent() { }
    
    public abstract int callback(VMJExchange vmjExchange);
    public abstract HashMap<String, Object> getDisbursement(VMJExchange vmjExchange);
    public abstract List<HashMap<String, Object>> getAllDisbursement(VMJExchange vmjExchange);
    public abstract List<HashMap<String, Object>> deleteDisbursement(VMJExchange vmjExchange);
    public abstract HashMap<String, Object> updateDisbursement(VMJExchange vmjExchange);
    public abstract HashMap<String, Object> moneyTransfer(VMJExchange vmjExchange);
}