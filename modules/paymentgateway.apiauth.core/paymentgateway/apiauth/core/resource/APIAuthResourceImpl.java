package paymentgateway.apiauth.core;
import java.util.*;
//add other required packages

public class APIAuthResourceImpl extends APIAuthResourceComponent{
	protected APIAuthResourceComponent record;

	public String generateCredential() {
		return "Credential";
	}
}
