package paymentgateway.payment.paymentlink;

import javax.validation.constraints.NotNull;

import paymentgateway.payment.core.CreatePaymentRequestBody;

public class GetPaymentLinkByIdRequestBody {
    @NotNull(message = "id must be specified")
    public int id;
}