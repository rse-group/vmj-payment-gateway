package paymentgateway.payment.card;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidCardMonthAndYearExpirationValidator
        implements ConstraintValidator<ValidCardMonthAndYearExpiration, CreateCardPaymentRequestBody> {

    @Override
    public void initialize(ValidCardMonthAndYearExpiration constraintAnnotation) {
    }

    @Override
    public boolean isValid(CreateCardPaymentRequestBody requestBody, ConstraintValidatorContext context) {
        if (requestBody == null) {
            return false;
        }

        String cardMonthAndYearExpiration = String.format("%s/%s", requestBody.cardExpMonth, requestBody.cardExpYear);

        // References:
        // https://stackoverflow.com/a/41170465
        // https://stackoverflow.com/a/32812687
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/yyyy");
            return !YearMonth.parse(cardMonthAndYearExpiration, fmt).isBefore(YearMonth.now());
        } catch (DateTimeParseException e) {
            System.out.println(e.getMessage());
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    String.format("invalid card_exp_month and card_exp_year combination", e.getParsedString()))
                    .addConstraintViolation();
            return false;
        }
    }
}