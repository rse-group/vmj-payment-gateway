package paymentgateway.payment.core;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import java.util.*;

public interface Payment {
//	int getId();
//	void setId(int id);

	int getIdTransaction();
	void setIdTransaction(int idTransaction);
	String getProductName();
	void setProductName(String productName);
	
	double getAmount();
	void setAmount(double amount);

	public HashMap<String,Object> toHashMap();
}
