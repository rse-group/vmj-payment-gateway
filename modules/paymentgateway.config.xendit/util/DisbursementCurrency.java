package paymentgateway.config.xendit;

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
}