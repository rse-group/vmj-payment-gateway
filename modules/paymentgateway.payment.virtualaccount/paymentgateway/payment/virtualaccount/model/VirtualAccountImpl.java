package paymentgateway.payment.virtualaccount;

import paymentgateway.payment.core.PaymentDecorator;

import java.util.HashMap;

import paymentgateway.payment.core.PaymentComponent;

import javax.persistence.Entity;
import javax.persistence.Table;
@Entity(name = "virtualaccount_impl")
@Table(name = "virtualaccount_impl")
public class VirtualAccountImpl extends PaymentDecorator {

	protected String bankCode;
	protected String vaAccountNumber;
	public VirtualAccountImpl(PaymentComponent record, String bankCode, String vaAccountNumber) {
		super(record);
		this.bankCode = bankCode;
		this.vaAccountNumber = vaAccountNumber;
	}
	public VirtualAccountImpl(){
		super();
	}

	public String getBankCode() {
		return this.bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
//	public boolean getIsOpenVA() {
//		return this.isOpenVA;
//	}
//
//	public void setIsOpenVA(boolean isOpenVA) {
//		this.isOpenVA = isOpenVA;
//	}
	public String getVaAccountNumber() {
		return this.vaAccountNumber;
	}

	public void setVaAccountNumber(String vaAccountNumber) {
		this.vaAccountNumber = vaAccountNumber;
	}

	public HashMap<String,Object> toHashMap() {
		HashMap<String,Object> virtualAccountMap = record.toHashMap();
		virtualAccountMap.put("bankCode", getBankCode());
//		virtualAccountMap.put("isOpenVA", getIsOpenVA());
		virtualAccountMap.put("vaAccountNumber", getVaAccountNumber());
		return virtualAccountMap;
	}
}
