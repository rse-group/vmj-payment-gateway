package paymentgateway.disbursement.core;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import java.util.*;

public interface Disbursement {

	public void setAmount(int amount);

	public int getAmount();

	public void setBankCode(String bankCode);

	public String getBankCode();

	public String getAccountNumber();

	public void setAccountNumber(String accountNumber);

	public HashMap<String, Object> toHashMap();

	// public void sendDisbursement();

	// public void getDisbursementById();
}
