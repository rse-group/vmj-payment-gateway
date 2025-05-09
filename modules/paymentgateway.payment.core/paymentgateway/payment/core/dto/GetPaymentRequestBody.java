package paymentgateway.payment.core;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import vmj.routing.route.dto.BaseRequestBody;

public class GetPaymentRequestBody extends BaseRequestBody {
	@NotEmpty(message = "id must be specified")
	public String id;
}