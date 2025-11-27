package paymentgateway.disbursement.specifiedrecipient;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashMap;
import paymentgateway.disbursement.core.DisbursementDecorator;
import paymentgateway.disbursement.core.DisbursementComponent;

@Entity(name = "specifiedrecipient_impl")
@Table(name = "specifiedrecipient_impl")
public class SpecifiedRecipientImpl extends DisbursementDecorator {
    protected String currency;
    protected String accountHolderName;

    public SpecifiedRecipientImpl(DisbursementComponent record, String currency, String accountHolderName) {
        super(record);
        this.currency = currency;
        this.accountHolderName = accountHolderName;
    }

    public SpecifiedRecipientImpl() {
        super();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    @Override
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> specialMap = record.toHashMap();
        specialMap.put("currency", getCurrency());
        specialMap.put("account_holder_name", getAccountHolderName());
        return specialMap;
    }
}