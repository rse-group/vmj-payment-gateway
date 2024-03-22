package paymentgateway.apiauth.core;

public abstract class APIAuthComponent implements APIAuth{
	protected String credential;
	
	public APIAuthComponent() {

	} 
	
	public String getCredential() {
		return this.credential;
	}
	
	public void setCredential(String credential) {
		this.credential = credential;
	}
 
	public abstract String generateCredential();
}
