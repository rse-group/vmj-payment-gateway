package paymentgateway.payment.paymentlink;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonProperty;

import paymentgateway.payment.core.CreatePaymentRequestBody;

public class CreatePaymentLinkRequestBody extends CreatePaymentRequestBody {
    @NotEmpty(message = "title must be specified")
    public String title;

    @NotEmpty(message = "email must be specified")
    @Email(message = "email must be a valid email")
    public String email;

    @NotEmpty(message = "sender_name must be specified")
    @JsonProperty("sender_name")
    public String senderName;
}
