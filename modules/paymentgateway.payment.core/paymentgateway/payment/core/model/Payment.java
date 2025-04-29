package paymentgateway.payment.core;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import java.util.*;

public interface Payment {
	int getIdTransaction();
	void setIdTransaction(int idTransaction);
	String getVendorName();
	void setVendorName(String vendorName);
	
	double getAmount();
	void setAmount(double amount);

	Date getCreatedAt();

	public HashMap<String,Object> toHashMap();
}
