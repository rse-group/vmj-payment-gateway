package paymentgateway.config.core;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateVirtualAccountPaymentRequestBody extends CreatePaymentRequestBody {
    @NotEmpty(message = "bank must be specified")
    public String bank;

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
