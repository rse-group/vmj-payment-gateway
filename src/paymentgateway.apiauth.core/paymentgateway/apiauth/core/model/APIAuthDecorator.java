package paymentgateway.apiauth.core;

import java.util.*;

//add other required packages

public abstract class APIAuthDecorator extends APIAuthComponent{
		
	public APIAuthDecorator () {
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
