package paymentgateway.apiauth.signature;

import paymentgateway.apiauth.core.signatureDecorator;
import paymentgateway.apiauth.core.signature;
import paymentgateway.apiauth.core.signatureComponent;

@Entity(name="apiauth_signature")
@Table(name="apiauth_signature")
public class signatureImpl extends signatureDecorator {

	protected String encryptionKey;
	public signatureImpl() {
	} 

	public String getEncryptionKey() {
		return this.encryptionKey;
	}

	public void setEncryptionKey(String encryptionKey) {
		this.encryptionKey = encryptionKey;
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
