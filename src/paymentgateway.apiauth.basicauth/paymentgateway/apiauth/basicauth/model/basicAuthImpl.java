package paymentgateway.apiauth.basicauth;

import paymentgateway.apiauth.core.APIAuthDecorator;
import paymentgateway.apiauth.core.APIAuth;
import paymentgateway.apiauth.core.APIAuthComponent;

import java.util.Base64;

public class basicAuthImpl extends APIAuthDecorator {

	protected String username;
	protected String password;
	public basicAuthImpl() {
	} 

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getCredential() {
		throw new UnsupportedOperationException();
	}

	public void setCredential(String credential) {
		throw new UnsupportedOperationException();
	}

	public String generateCredential() {
		String fullCredential = this.username + ":" + this.password;
		
		System.out.println(fullCredential);
		
		String base64Encoded = Base64.getEncoder().encodeToString(fullCredential.getBytes());
		
		System.out.println(base64Encoded);
		
		return base64Encoded;
	}

}
