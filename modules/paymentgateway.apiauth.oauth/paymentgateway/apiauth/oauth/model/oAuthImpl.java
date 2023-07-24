package paymentgateway.apiauth.oauth;

import paymentgateway.apiauth.core.oAuthDecorator;
import paymentgateway.apiauth.core.oAuth;
import paymentgateway.apiauth.core.oAuthComponent;

@Entity(name="apiauth_oauth")
@Table(name="apiauth_oauth")
public class oAuthImpl extends oAuthDecorator {

	protected String clientId;
	protected String clientSecret;
	public oAuthImpl() {
	} 

	public String getClientId() {
		return this.clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return this.clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getCredential() {
		throw new UnsupportedOperationException();
	}

	public void setCredential(String credential) {
		throw new UnsupportedOperationException();
	}

	public void generateCredential() {
		// TODO: implement this method
	}

}
