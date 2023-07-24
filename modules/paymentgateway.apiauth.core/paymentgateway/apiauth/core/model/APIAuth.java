package paymentgateway.apiauth.core;
import java.util.*;

public interface APIAuth {
	public String generateCredential();
	public String getCredential();
	public void setCredential(String credential);
}
