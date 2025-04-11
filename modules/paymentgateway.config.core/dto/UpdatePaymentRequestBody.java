package paymentgateway.config.core;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UpdatePaymentRequestBody {
	@NotNull(message = "id must be specified")
	public int id;
}