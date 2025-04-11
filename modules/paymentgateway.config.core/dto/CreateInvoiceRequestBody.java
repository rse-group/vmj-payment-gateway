package paymentgateway.config.core;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateInvoiceRequestBody extends CreatePaymentRequestBody {    
    @NotNull(message = "quantity must be specified")
    public int quantity;

    @NotNull(message = "price_per_item must be specified")
    @JsonProperty("price_per_item")
    public int pricePerItem;
}
