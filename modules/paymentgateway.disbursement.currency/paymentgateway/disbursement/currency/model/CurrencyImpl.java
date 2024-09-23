package paymentgateway.disbursement.currency;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Date;
import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementDecorator;
import paymentgateway.disbursement.core.DisbursementComponent;

@Entity(name = "currency_impl")
@Table(name = "currency_impl")
public class CurrencyImpl extends DisbursementDecorator {
    protected String senderCurrencyCode;
    protected String destinationCurrencyCode;

    public CurrencyImpl(DisbursementComponent record, String senderCurrencyCode, String destinationCurrencyCode) {
        super(record);
        this.senderCurrencyCode = senderCurrencyCode;
        this.destinationCurrencyCode = destinationCurrencyCode;
    }

    public CurrencyImpl() {
        super();
    }

    public String getSenderCurrencyCode() {
        return senderCurrencyCode;
    }

    public void setSenderCurrencyCode(String senderCurrencyCode) {
        this.senderCurrencyCode = senderCurrencyCode;
    }

    public String getDestinationCurrencyCode() {
        return destinationCurrencyCode;
    }

    public void setDestinationCurrencyCode(String destinationCurrencyCode) {
        this.destinationCurrencyCode = destinationCurrencyCode;
    }

    @Override
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> specialMap = record.toHashMap();
        specialMap.put("sender_currency_code", getSenderCurrencyCode());
        specialMap.put("destination_currency_code", getDestinationCurrencyCode());
        return specialMap;
    }
}