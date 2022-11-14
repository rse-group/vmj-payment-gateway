package paymentgateway.paymentchannel.oy_retailoutlet;

import paymentgateway.paymentchannel.core.PaymentChannelDecorator;

import java.util.HashMap;

import paymentgateway.paymentchannel.core.PaymentChannelComponent;

public class OyPaymentChannelImpl extends PaymentChannelDecorator {
	protected String idCustomer;
	protected String transaction_type;
	public OyPaymentChannelImpl(PaymentChannelComponent record, String idCustomer, String transaction_type) {
		super(record);
		this.idCustomer = idCustomer;
		this.transaction_type = transaction_type;
	}

	public String getIdCustomer() {
		return idCustomer;
	}

	public void setIdCustomer(String idCustomer) {
		this.idCustomer = idCustomer;
	}

	public String getTransaction_type() {
		return transaction_type;
	}

	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}
	
	public HashMap<String,Object> toHashMap() {
		HashMap<String,Object> retailOutletMap = record.toHashMap();
		retailOutletMap.put("idCustomer", getIdCustomer());
		retailOutletMap.put("transaction_type", getTransaction_type());
		return retailOutletMap;
	}
}