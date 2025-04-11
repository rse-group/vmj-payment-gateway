package paymentgateway.config.core;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class GetPaymentRequestBody {
	@NotNull(message = "id must be specified")
	public int id;
}