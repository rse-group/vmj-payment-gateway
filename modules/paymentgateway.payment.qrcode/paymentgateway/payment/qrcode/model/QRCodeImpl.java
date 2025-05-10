package paymentgateway.payment.qrcode;

import paymentgateway.payment.core.PaymentDecorator;

import java.util.HashMap;

import paymentgateway.payment.core.PaymentComponent;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "qrcode_impl")
@Table(name = "qrcode_impl")
public class QRCodeImpl extends PaymentDecorator {

    protected String qrCodeString;

    protected String channelCode;
    protected String expiryDateString;

    public QRCodeImpl(PaymentComponent record, String qrCodeString, String channelCode, String expiryDateString) {
        super(record);
        this.qrCodeString = qrCodeString;
        this.channelCode = channelCode;
        this.expiryDateString = expiryDateString;
    }

    public QRCodeImpl() {
        super();
    }

    public String getQRCodeString() {
        return this.qrCodeString;
    }

    public String getChannelCode() {
        return this.channelCode;
    }

    public String getExpiryDateString() {
        return this.expiryDateString;
    }

    public void setQRCodeString(String qrCodeString) {
        this.qrCodeString = qrCodeString;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public void setExpiryDate(String expiryDateString) {
        this.expiryDateString = expiryDateString;
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> qrCodeMap = record.toHashMap();
        qrCodeMap.put("qrCodeString", getQRCodeString());
        qrCodeMap.put("channelCode", getChannelCode());
        qrCodeMap.put("expiryDateString", getExpiryDateString());

        return qrCodeMap;
    }
}
