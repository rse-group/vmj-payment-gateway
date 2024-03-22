package paymentgateway.apiauth.core;

public interface APIAuth {
    public String generateCredential();
	public String getCredential();
	public void setCredential(String credential);
}
