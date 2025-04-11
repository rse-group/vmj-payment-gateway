package paymentgateway.config.core;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreatePaymentRequestBody {
	@NotEmpty(message = "vendor_name must be specified")
	@Pattern(regexp = "Flip|Midtrans|Oy|Xendit", message = "Vendor name is invalid; Valid values: Flip, Midtrans, Oy, Xendit")
	@JsonProperty("vendor_name")
	public String vendorName;
	
	@NotNull(message = "amount must be specified")
	public double amount;
}