package paymentgateway.apiauth.basicauth;

import paymentgateway.apiauth.core.APIAuthDecorator;
import paymentgateway.apiauth.core.APIAuthImpl;
import paymentgateway.apiauth.core.APIAuthResourceComponent;
import paymentgateway.apiauth.core.APIAuthResourceDecorator;

public class basicAuthResourceImpl extends APIAuthResourceDecorator {
	
	
    public basicAuthResourceImpl (APIAuthResourceComponent record) {
        // to do implement the method
    }

	public String generateCredential() {
		throw new UnsupportedOperationException();
	}
}
