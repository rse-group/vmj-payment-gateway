package paymentgateway.config.core;

import javax.validation.constraints.NotNull;

public class GetPaymentLinkByIdRequestBody {
    @NotNull(message = "id must be specified")
	public int id;
}