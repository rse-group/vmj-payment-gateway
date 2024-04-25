package paymentgateway.payment.virtualaccount;

import java.util.List;

public class VANumber {
	private String bank;
	private String va_number;

    public String getBank() {
        return bank;
    }
    public void setBank(String bank) {
        this.bank = bank;
    }
    public String getVa_number() {
        return va_number;
    }
    public void setVa_number(String va_number) {
        this.va_number = va_number;
    }
}