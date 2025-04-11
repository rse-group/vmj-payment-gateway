package paymentgateway.config.core;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetAllPaymentRequestBody {
	@NotEmpty(message = "table_name must be specified")
    @JsonProperty("table_name")
    public String tableName;
}