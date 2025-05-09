package paymentgateway.payment.core;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

import vmj.routing.route.dto.BaseRequestBody;

public class UpdatePaymentRequestBody extends BaseRequestBody {
	@NotEmpty(message = "id must be specified")
	public String id;

	@NotNull(message = "amount must be specified")
	@PositiveOrZero(message = "amount must not be negative")
	public Double amount;
}