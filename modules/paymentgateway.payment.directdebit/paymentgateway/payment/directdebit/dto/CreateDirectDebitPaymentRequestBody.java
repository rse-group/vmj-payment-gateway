package paymentgateway.payment.directdebit;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import paymentgateway.payment.core.CreatePaymentRequestBody;

public class CreateDirectDebitPaymentRequestBody extends CreatePaymentRequestBody {
    @NotEmpty(message = "bank must be specified")
    public String bank;

    @NotEmpty(message = "name must be specified")
    public String name;

    @NotEmpty(message = "email must be specified")
    @Email(message = "email must be a valid email")
    public String email;

    @NotEmpty(message = "phone must be specified")
    public String phone;

    @JsonProperty("success_return_url")
    public String successReturnUrl;

    @JsonProperty("failure_return_url")
    public String failureReturnUrl;

    @JsonProperty("card_last_four")
    public String cardLastFour;
}
