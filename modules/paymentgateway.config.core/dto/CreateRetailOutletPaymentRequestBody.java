package paymentgateway.config.core;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateRetailOutletPaymentRequestBody extends CreatePaymentRequestBody {
    @NotEmpty(message = "retail_outlet must be specified")
    @JsonProperty("retail_outlet")
    public String retailOutlet;
}
