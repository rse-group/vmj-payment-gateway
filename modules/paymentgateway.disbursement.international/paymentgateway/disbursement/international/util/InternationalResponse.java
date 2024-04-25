package paymentgateway.disbursement.international;

public class InternationalResponse {
	private int id;
	private int user_id;
	private String status;
	private double amount;
	private String company_id;
	private double exchange_rate;
	private String source_country;
	private String destination_country;
	private double beneficiary_amount;
	private String beneficiary_currency_code;
	private String receipt;
	private String transaction_type;
	private Beneficiary beneficiary;
	private Sender sender;
	private double fee;

	public int getId() {
		return id;
	}

	public String getReceipt() {
		return receipt;
	}

	public String getStatus() {
		return status;
	}

	public double getAmount() {
		return amount;
	}

	public Beneficiary getBeneficiary() {
		return beneficiary;
	}

	public double getBeneficiary_amount() {
		return beneficiary_amount;
	}

	public double getExchange_rate() {
		return exchange_rate;
	}

	public double getFee() {
		return fee;
	}

	public String getCompany_id() {
		return company_id;
	}

	public String getSource_country() {
		return source_country;
	}

	public String getBeneficiary_currency_code() {
		return beneficiary_currency_code;
	}

	public String getDestination_country() {
		return destination_country;
	}

	public String getTransaction_type() {
		return transaction_type;
	}

	public int getUser_id() {
		return user_id;
	}

	public Sender getSender() {
		return sender;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setBeneficiary_amount(double beneficiary_amount) {
		this.beneficiary_amount = beneficiary_amount;
	}

	public void setBeneficiary_currency_code(String beneficiary_currency_code) {
		this.beneficiary_currency_code = beneficiary_currency_code;
	}

	public void setDestination_country(String destination_country) {
		this.destination_country = destination_country;
	}

	public void setBeneficiary(Beneficiary beneficiary) {
		this.beneficiary = beneficiary;
	}

	public void setSource_country(String source_country) {
		this.source_country = source_country;
	}

	public void setExchange_rate(double exchange_rate) {
		this.exchange_rate = exchange_rate;
	}

	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}