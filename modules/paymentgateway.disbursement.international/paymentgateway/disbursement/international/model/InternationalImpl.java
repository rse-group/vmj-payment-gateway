package paymentgateway.disbursement.international;

import paymentgateway.disbursement.core.DisbursementDecorator;

import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.Table;

import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementComponent;

@Entity(name = "international_impl")
@Table(name = "international_impl")
public class InternationalImpl extends DisbursementDecorator {
	private double exchange_rate;
	private double fee;
	private String source_country;
	private String destination_country;
	private double amount_in_sender_currency;
	private String beneficiary_currency_code;

	public InternationalImpl(DisbursementComponent record, double exchange_rate, double fee, String source_country,
			String destination_country, double amount_in_sender_currency, String beneficiary_currency_code) {
		super(record, InternationalImpl.class.getName());
		this.exchange_rate = exchange_rate;
		this.fee = fee;
		this.source_country = source_country;
		this.destination_country = destination_country;
		this.amount_in_sender_currency = amount_in_sender_currency;
		this.beneficiary_currency_code = beneficiary_currency_code;
	}

	public InternationalImpl() {
		super();
	}

	public double getExchange_rate() {
		return exchange_rate;
	}

	public String getDestination_country() {
		return destination_country;
	}

	public String getBeneficiary_currency_code() {
		return beneficiary_currency_code;
	}

	public String getSource_country() {
		return source_country;
	}

	public double getFee() {
		return fee;
	}

	public double getAmount_in_sender_currency() {
		return amount_in_sender_currency;
	}

	public void setSource_country(String source_country) {
		this.source_country = source_country;
	}

	public void setDestination_country(String destination_country) {
		this.destination_country = destination_country;
	}

	public void setExchange_rate(double exchange_rate) {
		this.exchange_rate = exchange_rate;
	}

	public void setBeneficiary_currency_code(String beneficiary_currency_code) {
		this.beneficiary_currency_code = beneficiary_currency_code;
	}

	public void setAmount_in_sender_currency(double amount_in_sender_currency) {
		this.amount_in_sender_currency = amount_in_sender_currency;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public HashMap<String, Object> toHashMap() {
		HashMap<String, Object> moneyTransferMap = record.toHashMap();
		moneyTransferMap.put("exchange_rate", getExchange_rate());
		moneyTransferMap.put("fee", getExchange_rate());
		moneyTransferMap.put("source_country", getSource_country());
		moneyTransferMap.put("destination_country", getDestination_country());
		moneyTransferMap.put("amount_in_sender_currency", getAmount_in_sender_currency());
		moneyTransferMap.put("beneficiary_currency_code", getBeneficiary_currency_code());
		return moneyTransferMap;
	}
}
