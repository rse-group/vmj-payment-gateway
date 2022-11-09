package paymentgateway.paymentinterface.core;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import java.util.*;

public interface PaymentInterface {
	// since implementation of API varies types of auth required,
	// it will be handled on API's delta
	//String getApiKey();
	//void setApiKey(String apiKey);
	
	//String getApiEndpoint();
	//void setApiEndpoint(String apiEndpoint);
	
	int getAmount();
	void setAmount(int amount);
	
	public String getIdTransaction();
	public void setIdTransaction(String idTransaction);
	
	HashMap<String,Object> toHashMap();
}
