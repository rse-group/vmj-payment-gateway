package paymentgateway.config.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.CreditCardNumber;

public class CreateCreditCardPaymentRequestBody extends CreatePaymentRequestBody {
    @NotEmpty(message = "card_number must be specified")
    @CreditCardNumber()
    @JsonProperty("card_number")
    public String cardNumber;
    
    @NotEmpty(message = "card_exp_month must be specified")
    @JsonProperty("card_exp_month")
    public String cardExpMonth;
    
    @NotEmpty(message = "card_exp_year must be specified")
    @JsonProperty("card_exp_year")
    public String cardExpYear;
    
    @NotEmpty(message = "card_cvv must be specified")
    @Digits(integer = 4, fraction = 0, message = "card_cvv must only contain digits")
    @Size(min = 3, max = 4, message = "card_cvv must have a length of 3 or 4")
    @JsonProperty("card_cvv")
    public String cardCVV;
    
    @JsonProperty("token_id")
    public String tokenId;
}
