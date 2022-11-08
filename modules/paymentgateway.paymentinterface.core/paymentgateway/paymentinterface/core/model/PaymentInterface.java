package paymentgateway.paymentinterface.core;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import java.util.*;

public interface PaymentInterface {
	String getApiKey();
	void setApiKey(String apiKey);
	
	int getAmount();
	void setAmount(int amount);
	
	public String getIdTransaction();
	public void setIdTransaction(String idTransaction);
	
	String getApiEndpoint();
	void setApiEndpoint(String apiEndpoint);
	
	HashMap<String,Object> toHashMap();
}
