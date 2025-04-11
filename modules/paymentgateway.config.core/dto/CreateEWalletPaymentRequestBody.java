package paymentgateway.config.core;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateEWalletPaymentRequestBody extends CreatePaymentRequestBody {
    @NotEmpty(message = "ewallet_type must be specified")
    @JsonProperty("ewallet_type")
    public String ewalletType;

    @NotEmpty(message = "title must be specified")
    public String title;

    @NotEmpty(message = "name must be specified")
    public String name;

    @NotEmpty(message = "email must be specified")
    @Email(message = "email must be valid email")
    public String email;

    @NotEmpty(message = "phone must be specified")
    public String phone;
}
