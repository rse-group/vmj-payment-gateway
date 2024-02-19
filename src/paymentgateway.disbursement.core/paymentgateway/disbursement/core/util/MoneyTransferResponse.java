package paymentgateway.disbursement.core;

public class MoneyTransferResponse {
	private int id;
	private int user_id;
	private int agent_id;
	//tes
	private int approver_id;

	private int amount;
	private double exchange_rate;

	private String source_country;
	private String destination_country;
	private String beneficiary_currency_code;
	private String status;
	private String reason;
	private String timestamp;
	private String bank_code;
	private String account_number;
	private String recipient_name;
	private String sender_bank;
	private String remark;
	private String receipt;
	private String time_served;
	private int bundle_id;
	private int company_id;
	private int recipient_city;
	private String created_from;
	private String direction;
	private Sender sender;
	private Beneficiary beneficiary;
	private int fee;
	

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public int getAgent_id() {
		return agent_id;
	}

	public void setAgent_id(int agent_id) {
		this.agent_id = agent_id;
	}

	public int getApprover_id(){
		return approver_id;
	}

	public void setApprover_id(int approver_id){
		this.approver_id = approver_id;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}

	public double getExchange_rate() {
		return exchange_rate;
	}

	public void setExchange_rate(double exchange_rate) {
		this.exchange_rate = exchange_rate;
	}

	public String getSource_country() {
		return source_country;
	}

	public void setSource_country(String source_country) {
		this.source_country = source_country;
	}

	public String getDestination_country() {
		return destination_country;
	}

	public void setDestination_country(String destination_country) {
		this.destination_country = destination_country;
	}

	public String getBeneficiary_currency_code() {
		return beneficiary_currency_code;
	}

	public void setBeneficiary_currency_code(String beneficiary_currency_code) {
		this.beneficiary_currency_code = beneficiary_currency_code;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}

	public String getBank_code() {
		return bank_code;
	}

	public void setAccount_number(String account_number) {
		this.account_number = account_number;
	}

	public String getAccount_number() {
		return account_number;
	}

	public void setRecipient_name(String recipient_name) {
		this.recipient_name = recipient_name;
	}

	public String getRecipient_name() {
		return recipient_name;
	}

	public void setSender_bank(String sender_bank) {
		this.sender_bank = sender_bank;
	}

	public String getSender_bank() {
		return sender_bank;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setTime_served(String time_served) {
		this.time_served = time_served;
	}

	public String getTime_served() {
		return time_served;
	}

	public void setBundle_id(int bundle_id) {
		this.bundle_id = bundle_id;
	}

	public int getBundle_id() {
		return bundle_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

	public int getCompany_id() {
		return company_id;
	}

	public void setRecipient_city(int recipient_city) {
		this.recipient_city = recipient_city;
	}

	public int getRecipient_city() {
		return recipient_city;
	}

	public void setCreated_from(String created_from) {
		this.created_from = created_from;
	}

	public String getCreated_from() {
		return created_from;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getDirection() {
		return direction;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}

	public Sender getSender() {
		return sender;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public int getFee() {
		return fee;
	}
}