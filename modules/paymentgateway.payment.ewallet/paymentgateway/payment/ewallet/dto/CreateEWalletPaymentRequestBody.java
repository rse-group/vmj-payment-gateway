package paymentgateway.payment.ewallet;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import paymentgateway.payment.core.CreatePaymentRequestBody;

public class CreateEWalletPaymentRequestBody extends CreatePaymentRequestBody {
    @NotEmpty(message = "ewallet_type must be specified")
    @JsonProperty("ewallet_type")
    public String ewalletType;

    @NotEmpty(message = "title must be specified")
    public String title;

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
    
    public String cashtag;
}
