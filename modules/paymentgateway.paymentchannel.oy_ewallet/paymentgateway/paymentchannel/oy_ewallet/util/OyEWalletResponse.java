package paymentgateway.paymentchannel.oy_ewallet;

public class OyEWalletResponse {
	private OyStatus status;
	private String ewallet_trx_status;
	private String trx_id;
	private String ref_number;
	private String customer_id;
	private String partner_trx_id;
	private int amount;
	private String ewallet_code;
	private String ewallet_url;

	public OyStatus getStatus() {
		return status;
	}

	public void setStatus(OyStatus status) {
		this.status = status;
	}

	public String getEwallet_trx_status() {
		return ewallet_trx_status;
	}

	public void setEwallet_trx_status(String ewallet_trx_status) {
		this.ewallet_trx_status = ewallet_trx_status;
	}

	public String getTrx_id() {
		return trx_id;
	}

	public void setTrx_id(String trx_id) {
		this.trx_id = trx_id;
	}

	public String getRef_number() {
		return ref_number;
	}

	public void setRef_number(String ref_number) {
		this.ref_number = ref_number;
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

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getEwallet_code() {
		return ewallet_code;
	}

	public void setEwallet_code(String ewallet_code) {
		this.ewallet_code = ewallet_code;
	}

	public String getEwallet_url() {
		return ewallet_url;
	}

	public void setEwallet_url(String ewallet_url) {
		this.ewallet_url = ewallet_url;
	}
}