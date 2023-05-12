package paymentgateway.disbursement.core;

import java.util.*;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

public abstract class DisbursementResourceDecorator extends DisbursementResourceComponent {
	protected DisbursementResourceComponent record;

	public DisbursementResourceDecorator(DisbursementResourceComponent record) {
		this.record = record;
	}

	public Disbursement createDisbursement(VMJExchange vmjExchange, String userId) {
		return record.createDisbursement(vmjExchange, userId);
	}

	public void save(Disbursement disbursement){
		record.save(disbursement);
	}



	// public void sendDisbursement(String reference, String description, String
	// destinationCode,
	// String destinationHolderName, String destinationAccountNumber, String email,
	// Real amount) {
	// return record.sendDisbursement(reference, description, destinationCode,
	// destinationHolderName,
	// destinationAccountNumber, email, amount);
	// }

	// public void getDisbursementByReference(String reference) {
	// return record.getDisbursementByReference(reference);
	// }

	// public void getDisbursementByID(String id) {
	// return record.getDisbursementByID(id);
	// }
}
