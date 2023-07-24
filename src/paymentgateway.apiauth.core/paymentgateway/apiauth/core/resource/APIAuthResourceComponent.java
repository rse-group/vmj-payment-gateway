package paymentgateway.apiauth.core;
import java.util.*;

//add other required packages

public abstract class APIAuthResourceComponent implements APIAuthResource{

    public APIAuthResourceComponent(){
    }
    

	public abstract String generateCredential();
}
