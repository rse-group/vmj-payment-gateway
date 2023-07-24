package paymentgateway.fundtransfer.withuseraccount;

public class Result {
    private String success;
    private Error[] error;
    private String message;
    public String getSuccess() {
        return success;
    }
    public void setSuccess(String success) {
        this.success = success;
    }
    public Error[] getError() {
        return error;
    }
    public void setError(Error[] error) {
        this.error = error;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
