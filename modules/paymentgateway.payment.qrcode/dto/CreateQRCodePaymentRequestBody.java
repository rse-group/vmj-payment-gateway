package paymentgateway.payment.qrcode;

import javax.validation.constraints.NotEmpty;

import paymentgateway.payment.core.CreatePaymentRequestBody;

public class CreateQRCodePaymentRequestBody extends CreatePaymentRequestBody {
    @NotEmpty(message = "currency must be specified")
    public String currency;
}
