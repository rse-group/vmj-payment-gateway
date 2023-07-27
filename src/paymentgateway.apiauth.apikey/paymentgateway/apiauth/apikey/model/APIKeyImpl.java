package paymentgateway.apiauth.apikey;

import paymentgateway.apiauth.core.APIAuthDecorator;
import paymentgateway.apiauth.core.APIAuth;
import paymentgateway.apiauth.core.APIAuthComponent;
import paymentgateway.apiauth.core.APIAuthDecorator;

public class APIKeyImpl extends APIAuthDecorator {
	protected String APIKey;
	protected String APIPassword;
	
	public APIKeyImpl() {
		
	} 

	public String getAPIKey() {
		return this.APIKey;
	}

	public void setAPIKey(String APIKey) {
		this.APIKey = APIKey;
	}
	public String getAPIPassword() {
		return this.APIPassword;
	}

	public void setAPIPassword(String APIPassword) {
		this.APIPassword = APIPassword;
	}
	public String getCredential() {
		throw new UnsupportedOperationException();
	}

	public void setCredential(String credential) {
		throw new UnsupportedOperationException();
	}

	public String generateCredential() {
		throw new UnsupportedOperationException();
	}

}
