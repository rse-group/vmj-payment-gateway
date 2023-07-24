package paymentgateway.apiauth.apikey;

import paymentgateway.apiauth.core.APIAuthResourceDecorator;
import paymentgateway.apiauth.core.APIAuthImpl;
import paymentgateway.apiauth.core.APIAuthResourceComponent;

public class APIKeyResourceImpl extends APIAuthResourceDecorator {
    public APIKeyResourceImpl (APIAuthResourceComponent record) {
        // to do implement the method
    }

	public String generateCredential() {
		throw new UnsupportedOperationException();
	}
}
