package paymentgateway.payment.midtransimpl;

import paymentgateway.payment.core.PaymentDecorator;

import java.util.HashMap;

import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentComponent;

public class VirtualAccountImpl extends PaymentDecorator {

    protected String bankCode;
    protected boolean isOpenVA;
    protected String vaAccountNumber;

    public VirtualAccountImpl(PaymentComponent record, String bankCode, boolean isOpenVA, String vaAccountNumber) {
        super(record);
        this.bankCode = bankCode;
        this.isOpenVA = isOpenVA;
        this.vaAccountNumber = vaAccountNumber;
    }

    public String getBankCode() {
        return this.bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public boolean getIsOpenVA() {
        return this.isOpenVA;
    }

    public void setIsOpenVA(boolean isOpenVA) {
        this.isOpenVA = isOpenVA;
    }

    public String getVaAccountNumber() {
        return this.vaAccountNumber;
    }

    public void setVaAccountNumber(String vaAccountNumber) {
        this.vaAccountNumber = vaAccountNumber;
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> virtualAccountMap = record.toHashMap();
        virtualAccountMap.put("bankCode", getBankCode());
        virtualAccountMap.put("isOpenVA", getIsOpenVA());
        virtualAccountMap.put("vaAccountNumber", getVaAccountNumber());
        return virtualAccountMap;
    }
}
