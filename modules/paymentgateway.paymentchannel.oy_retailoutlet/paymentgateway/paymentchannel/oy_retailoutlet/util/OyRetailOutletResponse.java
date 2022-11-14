package paymentgateway.paymentchannel.oy_retailoutlet;

public class OyRetailOutletResponse {
	private OyStatus status;
	private int amount;
	private int admin_fee;
	private int tax_fee;
	private int total_amount;
	private String code;
	private String timestamp;
	private String customer_id;
	private String partner_trx_id;
	private String trx_id;
	private String transacation_type;
	private String inactive_at;
	private String expired_at;

    public OyStatus getStatus() {
        return status;
    }

    public void setStatus(OyStatus status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAdmin_fee() {
        return admin_fee;
    }

    public void setAdmin_fee(int admin_fee) {
        this.admin_fee = admin_fee;
    }

    public int getTax_fee() {
        return tax_fee;
    }

    public void setTax_fee(int tax_fee) {
        this.tax_fee = tax_fee;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getPartner_trx_id() {
        return partner_trx_id;
    }

    public void setPartner_trx_id(String partner_trx_id) {
        this.partner_trx_id = partner_trx_id;
    }

    public String getTrx_id() {
        return trx_id;
    }

    public void setTrx_id(String trx_id) {
        this.trx_id = trx_id;
    }

    public String getTransacation_type() {
        return transacation_type;
    }

    public void setTransacation_type(String transacation_type) {
        this.transacation_type = transacation_type;
    }

    public String getInactive_at() {
        return inactive_at;
    }

    public void setInactive_at(String inactive_at) {
        this.inactive_at = inactive_at;
    }

    public String getExpired_at() {
        return expired_at;
    }

    public void setExpired_at(String expired_at) {
        this.expired_at = expired_at;
    }
}