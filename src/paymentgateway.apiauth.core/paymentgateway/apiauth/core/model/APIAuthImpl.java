package paymentgateway.apiauth.core;
import java.util.*;

public class APIAuthImpl extends APIAuthComponent {
	protected String credential;

	public APIAuthImpl() {
	}

	public String getCredential() {
		return this.credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

	public String generateCredential() {
		return this.credential;
	}
}
