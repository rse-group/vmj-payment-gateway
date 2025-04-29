package paymentgateway.payment.core;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonProperty;

import vmj.routing.route.dto.BaseRequestBody;

public class CreatePaymentRequestBody extends BaseRequestBody {
	@NotEmpty(message = "vendor_name must be specified")
	@Pattern(regexp = "Flip|Midtrans|Oy|Xendit", message = "Vendor name is invalid; Valid values: Flip, Midtrans, Oy, Xendit")
	@JsonProperty("vendor_name")
	public String vendorName;

	@PositiveOrZero(message = "amount must not be negative")
	public double amount;
}