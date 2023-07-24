package paymentgateway.fundtransfer.inbatch;

public class FundTransferResponse {
    private String created;
    private String reference;
    private Integer total_uploaded_count;
    private Integer total_uploaded_amount;
    private String status;
    private String id;
    
    public String getCreated() {
        return created;
    }
    public void setCreated(String created) {
        this.created = created;
    }
    public String getReference() {
        return reference;
    }
    public void setReference(String reference) {
        this.reference = reference;
    }
    public Integer getTotal_uploaded_count() {
        return total_uploaded_count;
    }
    public void setTotal_uploaded_count(Integer total_uploaded_count) {
        this.total_uploaded_count = total_uploaded_count;
    }
    public Integer getTotal_uploaded_amount() {
        return total_uploaded_amount;
    }
    public void setTotal_uploaded_amount(Integer total_uploaded_amount) {
        this.total_uploaded_amount = total_uploaded_amount;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

}