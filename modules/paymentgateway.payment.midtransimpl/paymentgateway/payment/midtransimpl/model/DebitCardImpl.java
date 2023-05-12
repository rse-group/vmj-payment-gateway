package paymentgateway.payment.midtransimpl;

import paymentgateway.payment.core.PaymentDecorator;

import java.util.HashMap;

import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentComponent;

public class DebitCardImpl extends PaymentDecorator {

    protected String bankCode;
    protected String directDebitUrl;

    public DebitCardImpl(PaymentComponent record, String bankCode, String directDebitUrl) {
        super(record);
        this.bankCode = bankCode;
        this.directDebitUrl = directDebitUrl;
    }

    public String getBankCode() {
        return this.bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getDirectDebitUrl() {
        return this.directDebitUrl;
    }

    public void setDirectDebitUrl(String directDebitUrl) {
        this.directDebitUrl = directDebitUrl;
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> debitCardMap = record.toHashMap();
        debitCardMap.put("bankCode", getBankCode());
        debitCardMap.put("directDebitUrl", getDirectDebitUrl());
        return debitCardMap;
    }
}
