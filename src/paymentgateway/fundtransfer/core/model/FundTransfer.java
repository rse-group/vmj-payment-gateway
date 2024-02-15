package paymentgateway.fundtransfer.core;

import java.util.*;

public interface FundTransfer {

	public int getId();
	public void setId(int id);
	public void setAmount(double amount);

	public double getAmount();

	public String getAccountNumber();
	public String getBankCode();

	public void setBankCode(String bankCode);
	public void setAccountNumber(String accountNumber);

	public void sendTransfer();
	public void getTransferByReference();
	public void getTransferById();

	public HashMap<String, Object> toHashMap();


}
