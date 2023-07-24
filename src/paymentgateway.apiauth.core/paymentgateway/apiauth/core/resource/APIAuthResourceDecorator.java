package paymentgateway.apiauth.core;
import java.util.*;

public abstract class APIAuthResourceDecorator extends APIAuthResourceComponent{
	protected APIAuthResourceComponent record;

	public String generateCredential() {
		return "Credential";
	}
}
