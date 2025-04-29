package paymentgateway.payment.core;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

import vmj.routing.route.dto.BaseRequestBody;

public class UpdatePaymentRequestBody extends BaseRequestBody {
	@NotNull(message = "id must be specified")
	public int id;

	@PositiveOrZero(message = "amount must not be negative")
	public double amount;
}