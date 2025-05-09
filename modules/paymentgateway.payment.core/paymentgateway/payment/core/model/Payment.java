package paymentgateway.payment.core;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import java.util.*;

public interface Payment {
	UUID getIdTransaction();
	void setIdTransaction(UUID idTransaction);
	String getVendorName();
	void setVendorName(String vendorName);
	
	double getAmount();
	void setAmount(double amount);

	String getStatus();
	void setStatus(String status);

	Date getCreatedAt();

	public HashMap<String,Object> toHashMap();
}
