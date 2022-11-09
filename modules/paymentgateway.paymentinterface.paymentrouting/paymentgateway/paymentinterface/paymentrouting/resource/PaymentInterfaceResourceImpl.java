package paymentgateway.paymentinterface.paymentrouting;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import java.util.List;
import java.util.*;

import paymentgateway.paymentinterface.PaymentInterfaceFactory;
import paymentgateway.paymentinterface.core.PaymentInterface;
import paymentgateway.paymentinterface.core.PaymentInterfaceImpl;
import paymentgateway.paymentinterface.core.PaymentInterfaceResourceDecorator;
import paymentgateway.paymentinterface.core.PaymentInterfaceResourceComponent;

public class PaymentInterfaceResourceImpl extends PaymentInterfaceResourceDecorator {
	protected String paymentMethods;
	protected String sourceOfFunds;
	protected List<PaymentRoutingRecipient> paymentRoutings;
	public PaymentInterfaceResourceImpl(PaymentInterfaceResourceComponent record) {
		super(record);
		this.paymentMethods = "";
		this.sourceOfFunds = "";
		this.paymentRoutings = new ArrayList<PaymentRoutingRecipient>();
	}
    public PaymentInterfaceResourceImpl (
    		PaymentInterfaceResourceComponent record,
    		String paymentMethods, String sourceOfFunds,
    		List<PaymentRoutingRecipient> paymentRoutingRecipient) {
        super(record);
        this.paymentMethods = paymentMethods;
        this.sourceOfFunds = sourceOfFunds;
        this.paymentRoutings = paymentRoutingRecipient;
    }
    
    public String getPaymentMethods() {
    	return this.paymentMethods;
    }
    
    public void setPaymentMethods(String paymentMethods) {
    	this.paymentMethods = paymentMethods;
    }
    
    public String getSourceOfFunds() {
    	return this.sourceOfFunds;
    }
    
    public void setSourceOfFunds(String sourceOfFunds) {
    	this.sourceOfFunds = sourceOfFunds;
    }
    
    public List<PaymentRoutingRecipient> getPaymentRoutings() {
    	return this.paymentRoutings;
    }
    
    public void setPaymentRoutings(List<PaymentRoutingRecipient> paymentRoutings) {
    	this.paymentRoutings = paymentRoutings;
    }
    
	public PaymentInterface createTransaction(int amount, String idTransaction) {
		PaymentInterface transaction = record.createTransaction(amount, idTransaction);
		String paymentCheckoutUrl = sendTransactionPaymentRouting(amount, idTransaction);
		PaymentInterface paymentRoutingTransaction =
				PaymentInterfaceFactory.createPaymentInterface(
						"paymentgateway.paymentinterface.paymentrouting.PaymentInterfaceImpl",
						transaction,
						getPaymentMethods(),
						getSourceOfFunds(),
						getPaymentRoutings(),
						paymentCheckoutUrl
						);
		return paymentRoutingTransaction;
	}
	
	protected String sendTransactionPaymentRouting(int amount, String idTransaction) {
		// to do implement transaction as payment routing
		System.out.println("Transaction send from payment routing");
		return "";
	}
}

