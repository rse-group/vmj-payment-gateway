package paymentgateway.payment.core;

import java.math.BigInteger;
import java.util.*;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import javax.persistence.OneToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.CascadeType;

@MappedSuperclass
public abstract class PaymentDecorator extends PaymentComponent{

	@OneToOne(cascade=CascadeType.REMOVE, optional=true, orphanRemoval = true)
	protected PaymentComponent record;
		
	public PaymentDecorator (PaymentComponent record) {
		String generateUUIDNo = String.format("%010d",new BigInteger(UUID.randomUUID().toString().replace("-",""),16));
		String unique_no = generateUUIDNo.substring(0,5);
		this.record = record;
		this.idTransaction = Integer.parseInt(unique_no);
	}

	public PaymentDecorator () {
	}

//	public int getId(){
//		return record.getId();
//	}
//
//	public void setId(int id){
//		record.setId(id);
//	}
	public int getIdTransaction() {
		return record.getIdTransaction();
	}
	public void setIdTransaction(int idTransaction) {
		record.setIdTransaction(idTransaction);
	}
	public double getAmount() {
		return record.getAmount();
	}
	public void setAmount(double amount) {
		record.setAmount(amount);
	}

	public String getProductName(){
		return record.getProductName();
	}
	public void setProductName(String productName){
		record.setProductName(productName);
	}
}

