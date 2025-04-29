package paymentgateway.payment.retailoutlet;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import paymentgateway.payment.core.CreatePaymentRequestBody;

public class CreateRetailOutletPaymentRequestBody extends CreatePaymentRequestBody {
    @NotEmpty(message = "retail_outlet must be specified")
    @JsonProperty("retail_outlet")
    public String retailOutlet;
}
