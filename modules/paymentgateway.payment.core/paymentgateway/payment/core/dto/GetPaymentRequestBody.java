package paymentgateway.payment.core;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import vmj.routing.route.dto.BaseRequestBody;

public class GetPaymentRequestBody extends BaseRequestBody {
	@NotNull(message = "id must be specified")
	public int id;
}