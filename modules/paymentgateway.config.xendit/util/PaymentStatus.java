package paymentgateway.config.xendit;


public enum PaymentStatus {
    FAILED("FAILED"),
	SUCCEEDED("SUCCEEDED");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    public String getStatus() {
        return value;
    }
}
