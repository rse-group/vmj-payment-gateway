package paymentgateway.config.core;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

public class UpdatePaymentRequestBody {
	@NotNull(message = "id must be specified")
	public int id;

	@PositiveOrZero(message = "amount must not be negative")
	public double amount;
}