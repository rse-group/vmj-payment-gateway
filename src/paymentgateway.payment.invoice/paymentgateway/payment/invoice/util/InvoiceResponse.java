package paymentgateway.payment.invoice;

public class InvoiceResponse {

    int id;
    private String payment_link_id;
    private String message;
    private String status;
    private String email_status;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public String getUrl() {
        return url;
    }

    public String getPayment_link_id() {
        return payment_link_id;
    }

    public String getStatus() {
        return status;
    }

    public String getEmail_status() {
        return email_status;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPayment_link_id(String payment_link_id) {
        this.payment_link_id = payment_link_id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEmail_status(String email_status) {
        this.email_status = email_status;
    }
}
