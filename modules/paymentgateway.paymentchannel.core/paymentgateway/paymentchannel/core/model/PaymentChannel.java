package paymentgateway.paymentchannel.core;
import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import java.util.*;

public interface PaymentChannel {
	public String getIdTransaction();
	public void setIdTransaction(String idTransaction);
	
	public int getAmount();
	public void setAmount(int amount);
	
	HashMap<String,Object> toHashMap();
}
