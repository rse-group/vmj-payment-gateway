package paymentgateway.apiauth.core;

public abstract class APIAuthDecorator extends APIAuthComponent{
	protected APIAuthComponent record;
		
	public APIAuthDecorator (APIAuthComponent record) {
		this.record = record;
	}

	public APIAuthDecorator () {
	}

	public String getCredential() {
		return record.getCredential();
	}

	public String setCredential() {
		record.setCredential();
	}
	
	public String generateCredential() {
		return record.generateCredential();
	}
}

