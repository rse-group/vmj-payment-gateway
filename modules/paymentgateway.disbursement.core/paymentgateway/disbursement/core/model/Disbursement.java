package paymentgateway.disbursement.core;

import java.util.*;

public interface Disbursement {
	public UUID getId();
	public void setId(UUID id);

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
	
	public String getVendorName();
	public void setVendorName(String vendorName);

	public String getVendorGeneratedId();
	public void setVendorGeneratedId(String vendorGeneratedId);
}
