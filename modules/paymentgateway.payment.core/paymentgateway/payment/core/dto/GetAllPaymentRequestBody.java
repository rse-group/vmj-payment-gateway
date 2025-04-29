package paymentgateway.payment.core;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

import vmj.routing.route.dto.BaseRequestBody;

public class GetAllPaymentRequestBody extends BaseRequestBody {
    @NotEmpty(message = "table_name must be specified")
    @JsonProperty("table_name")
    public String tableName;
}