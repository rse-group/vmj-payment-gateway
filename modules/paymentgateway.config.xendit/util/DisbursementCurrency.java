package paymentgateway.config.xendit;

import java.util.EnumSet;
import java.util.stream.Collectors;
import vmj.routing.route.exceptions.BadRequestException;

public enum DisbursementCurrency {
    INDONESIAN_RUPIAH("IDR"),
    PHILIPINE_PESO("PHP"),
    MALAYSIAN_RINGGIT("MYR"),
    VIETNAMESE_DONG("VND"),
    THAI_BAHT("THB");

    private final String value;

    DisbursementCurrency(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static void validate(String currencyCode) throws BadRequestException {
        for (DisbursementCurrency currency : DisbursementCurrency.values()) {
            if (currency.getValue().equals(currencyCode)) {
                return;
            }
        }
        String supportedCurrencies = EnumSet.allOf(DisbursementCurrency.class).stream()
                .map(DisbursementCurrency::getValue)
                .collect(Collectors.joining(", "));
        throw new BadRequestException("Invalid currency: " + currencyCode + ". Supported currencies are: " + supportedCurrencies);
    }
}