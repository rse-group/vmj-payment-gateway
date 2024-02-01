package paymentgateway.payment.midtransimpl;

import paymentgateway.payment.core.PaymentDecorator;

import java.util.HashMap;

import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentComponent;

public class CreditCardImpl extends PaymentDecorator {

    protected String idToken;
    protected String creditCardUrl;

    public CreditCardImpl(PaymentComponent record, String idToken, String creditCardUrl) {
        super(record);
        this.idToken = idToken;
        this.creditCardUrl = creditCardUrl;
    }

    public String getIdToken() {
        return this.idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getCreditCardUrl() {
        return this.creditCardUrl;
    }

    public void setCreditCardUrl(String creditCardUrl) {
        this.creditCardUrl = creditCardUrl;
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> creditCardMap = record.toHashMap();
        creditCardMap.put("idToken", getIdToken());
        creditCardMap.put("creditCardUrl", getCreditCardUrl());
        return creditCardMap;
    }
}