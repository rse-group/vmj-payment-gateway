package paymentgateway.paymentinterface.paymentlink;

import java.util.*;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import paymentgateway.paymentinterface.PaymentInterfaceFactory;
import paymentgateway.paymentinterface.core.PaymentInterface;
import paymentgateway.paymentinterface.core.PaymentInterfaceResourceDecorator;
import paymentgateway.paymentinterface.core.PaymentInterfaceImpl;
import paymentgateway.paymentinterface.core.PaymentInterfaceResourceComponent;

public class PaymentInterfaceResourceImpl extends PaymentInterfaceResourceDecorator {
    public PaymentInterfaceResourceImpl (PaymentInterfaceResourceComponent record) {
        super(record);
    }

	public PaymentInterface createTransaction(int amount, String idTransaction) {
		PaymentInterface transaction = record.createTransaction(amount, idTransaction);
		String paymentLink = sendTransactionPaymentLink(amount, idTransaction);
		PaymentInterface paymentLinkTransaction = PaymentInterfaceFactory.createPaymentInterface("paymentgateway.paymentinterface.paymentlink.PaymentInterfaceImpl", transaction, paymentLink);
		return paymentLinkTransaction;
	}
	
	protected String sendTransactionPaymentLink(int amount, String idTransaction) {
		// to do implement transaction as paymentlink,
		System.out.println("Transaction send from payment link");
		return "";
	}
	
	//@Route(url="test/call/paymentlink")
	//public HashMap<String,Object> paymentLinkEndpoint(VMJExchange vmjExchange) {
	//	if (vmjExchange.getHttpMethod().equals("OPTIONS")) return null;
	//	PaymentInterface result = this.createTransaction("api-key", 58000, "id-transaction-123", "api-endpoint-paymentlink");
	//	return result.toHashMap();
	//}
}
