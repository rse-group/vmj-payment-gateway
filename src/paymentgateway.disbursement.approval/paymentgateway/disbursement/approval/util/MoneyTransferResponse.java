package paymentgateway.disbursement.approval;

public class MoneyTransferResponse {
	private String id;
	private String user_id;
	private int amount;
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
	private String sender;
	private int fee;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
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

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSender() {
		return sender;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public int getFee() {
		return fee;
	}
}