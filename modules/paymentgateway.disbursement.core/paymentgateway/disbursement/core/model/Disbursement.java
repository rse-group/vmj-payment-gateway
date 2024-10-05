package paymentgateway.disbursement.core;

import java.util.*;

public interface Disbursement {
	public int getId();
	public void setId(int id);

	public int getUserId();
	public void setUserId(int userId);

	public double getAmount();
	public void setAmount(double amount);

	public String getAccountNumber();
	public void setAccountNumber(String accountNumber);
	
	public String getBankCode();
	public void setBankCode(String bankCode);

	public String getStatus();
	public void setStatus(String status);

	public HashMap<String, Object> toHashMap();
}
