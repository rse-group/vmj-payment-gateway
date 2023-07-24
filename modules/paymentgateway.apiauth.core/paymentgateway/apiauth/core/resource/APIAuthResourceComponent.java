package paymentgateway.apiauth.core;
import java.util.*;

public abstract class APIAuthResourceComponent implements APIAuthResource{

    public APIAuthResourceComponent(){}

	public abstract String generateCredential();
}
