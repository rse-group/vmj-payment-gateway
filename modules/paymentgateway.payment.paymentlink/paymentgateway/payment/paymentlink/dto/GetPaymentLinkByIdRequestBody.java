package paymentgateway.payment.paymentlink;

import javax.validation.constraints.NotEmpty;

import paymentgateway.payment.core.CreatePaymentRequestBody;

public class GetPaymentLinkByIdRequestBody {
    @NotEmpty(message = "id must be specified")
    public String id;
}