package paymentgateway.payment.core;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import java.util.*;

public interface Payment {
	public String getIdTransaction();
	public void setIdTransaction(String idTransaction);
	
	public int getAmount();
	public void setAmount(int amount);
	
	public HashMap<String,Object> toHashMap();
}
