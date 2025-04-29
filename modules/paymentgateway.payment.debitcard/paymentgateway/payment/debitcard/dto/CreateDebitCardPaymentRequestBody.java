package paymentgateway.payment.debitcard;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import paymentgateway.payment.core.CreatePaymentRequestBody;

public class CreateDebitCardPaymentRequestBody extends CreatePaymentRequestBody {
    @NotEmpty(message = "token_id must be specified")
    @JsonProperty("token_id")
    public String tokenId;

    @NotEmpty(message = "payment_type must be specified")
    @JsonProperty("payment_type")
    public String paymentType;
}
