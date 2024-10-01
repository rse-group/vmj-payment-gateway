package paymentgateway.config.xendit;

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

//    public static void validate(String currency) throws BadRequestException {
//    	if(!EnumUtils.isValidEnum(DisbursementCurrency.class, currency)) {
//            throw new BadRequestException("Invalid currency: " + currency);
//        }
//    }
}