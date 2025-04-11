package paymentgateway.config.core;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreatePaymentRoutingPaymentRequestBody extends CreatePaymentRequestBody {
    @NotEmpty(message = "recipient_account must be specified")
    @JsonProperty("recipient_account")
    public String recipientAccount;
    
    @NotEmpty(message = "recipient_bank must be specified")
    @JsonProperty("recipient_bank")
    public String recipientBank;
    
    @NotNull(message = "recipient_amount must be specified")
    @JsonProperty("recipient_amount")
    public int recipientAmount;
    
    @NotEmpty(message = "recipient_email must be specified")
    @Email(message = "email must be valid email")
    @JsonProperty("recipient_email")
    public String recipientEmail;
    
    @NotEmpty(message = "recipient_note must be specified")
    @JsonProperty("recipient_note")
    public String recipientNote;
}
