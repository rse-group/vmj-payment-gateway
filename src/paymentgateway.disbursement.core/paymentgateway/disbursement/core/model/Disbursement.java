package paymentgateway.disbursement.core;

import java.util.*;

public interface Disbursement {

	public int getId();
	public void setId(int id);
	public void setAmount(double amount);

	public double getAmount();

	public String getAccountNumber();
	public String getBankCode();

	public void setBankCode(String bankCode);
	public void setAccountNumber(String accountNumber);

	public HashMap<String, Object> toHashMap();


}
