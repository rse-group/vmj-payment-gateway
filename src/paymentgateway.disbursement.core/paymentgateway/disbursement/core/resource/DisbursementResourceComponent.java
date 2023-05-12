package paymentgateway.disbursement.core;

import java.util.*;

import vmj.hibernate.integrator.RepositoryUtil;
import vmj.routing.route.VMJExchange;
//add other required packages

import paymentgateway.disbursement.core.Disbursement;

public abstract class DisbursementResourceComponent implements DisbursementResource {
    protected RepositoryUtil<Disbursement> Repository;

    public DisbursementResourceComponent() {
        this.Repository = new RepositoryUtil<Disbursement>(
                paymentgateway.disbursement.core.DisbursementComponent.class);
    }

    public abstract Disbursement createDisbursement(VMJExchange vmjExchange, String userId);
//    public abstract void save(Disbursement disbursement){
//
//    };

    public void save(Disbursement disbursement){
        System.out.println("save 1 di component");
        Repository.saveObject(disbursement);
        System.out.println("save 2 di component");
    }

    // public abstract HashMap<String, Object> update(VMJExchange vmjExchange);

    // public abstract HashMap<String, Object> get(VMJExchange vmjExchange);

    // public abstract List<HashMap<String, Object>> getAll(VMJExchange
    // vmjExchange);

    // public abstract List<HashMap<String, Object>> transformListToHashMap(List<>
    // List);

    // public abstract List<HashMap<String, Object>> delete(VMJExchange
    // vmjExchange);

    // public abstract void sendDisbursement(String reference, String description,
    // String destinationCode,
    // String destinationHolderName, String destinationAccountNumber, String email,
    // Real amount);

    // public abstract void getDisbursementByReference(String reference);

    // public abstract void getDisbursementByID(String id);
}
