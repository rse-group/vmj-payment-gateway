package paymentgateway.payment.card;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;;

@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { ValidCardMonthAndYearExpirationValidator.class })
@Documented
public @interface ValidCardMonthAndYearExpiration {

    String message() default "card_exp_month and card_exp_year must be equal or greater than the current month and year";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}