package paymentgateway.payment.virtualaccount;

import java.util.List;

public class VirtualAccountResponse {

	private int idDB;
	private String id;
	private String status_code;
	private String status_message;
	private String transaction_id;
	private String order_id;
	private String merchant_id;
	private String gross_amount;
	private String currency;
	private String payment_type;
	private String transaction_time;
	private String transaction_status;

	private String va_number;
	private List<VANumber> va_numbers;
	private String virtual_account_number;

	private String permata_va_number;
	private String fraud_status;

	public String getId() {
		return id;
	}


	public void setVa_number(String va_number) {
		this.va_number = va_number;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVirtual_account_number() {
		if (va_numbers == null && va_number == null){
			return permata_va_number;
		} else if (va_numbers == null && permata_va_number == null) {
			return va_number;
		}
		return (va_numbers.get(0)).getVa_number();
	}


	public String getPermata_va_number() {
		return permata_va_number;
	}

	public void setPermata_va_number(String permata_va_number) {
		this.permata_va_number = permata_va_number;
	}

	public int getIdDB(){
		return idDB;
	}

	public void setIdDB(int id) {
		this.idDB = id;
	}

	public String getStatus_code() {
		return status_code;
	}
	public void setStatus_code(String status_code) {
		this.status_code = status_code;
	}
	public String getStatus_message() {
		return status_message;
	}
	public void setStatus_message(String status_message) {
		this.status_message = status_message;
	}
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getMerchant_id() {
		return merchant_id;
	}
	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}
	public String getGross_amount() {
		return gross_amount;
	}
	public void setGross_amount(String gross_amount) {
		this.gross_amount = gross_amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getPayment_type() {
		return payment_type;
	}
	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}
	public String getTransaction_time() {
		return transaction_time;
	}
	public void setTransaction_time(String transaction_time) {
		this.transaction_time = transaction_time;
	}
	public String getTransaction_status() {
		return transaction_status;
	}
	public void setTransaction_status(String transaction_status) {
		this.transaction_status = transaction_status;
	}
	public List<VANumber> getVa_numbers() {
		return va_numbers;
	}
	public void setVa_numbers(List<VANumber> va_numbers) {
		this.va_numbers = va_numbers;
	}
	public String getFraud_status() {
		return fraud_status;
	}
	public void setFraud_status(String fraud_status) {
		this.fraud_status = fraud_status;
	}
}
