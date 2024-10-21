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
		super(record);
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

	public double getExchangeRate() {
		return exchange_rate;
	}

	public String getDestinationCountry() {
		return destination_country;
	}

	public String getBeneficiaryCurrencyCode() {
		return beneficiary_currency_code;
	}

	public String getSourceCountry() {
		return source_country;
	}

	public double getFee() {
		return fee;
	}

	public double getAmountInSenderCurrency() {
		return amount_in_sender_currency;
	}

	public void setSourceCountry(String source_country) {
		this.source_country = source_country;
	}

	public void setDestinationCountry(String destination_country) {
		this.destination_country = destination_country;
	}

	public void setExchangeRate(double exchange_rate) {
		this.exchange_rate = exchange_rate;
	}

	public void setBeneficiaryCurrencyCode(String beneficiary_currency_code) {
		this.beneficiary_currency_code = beneficiary_currency_code;
	}

	public void setAmountInSenderCurrency(double amount_in_sender_currency) {
		this.amount_in_sender_currency = amount_in_sender_currency;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public HashMap<String, Object> toHashMap() {
		HashMap<String, Object> disbursementMap = record.toHashMap();
		disbursementMap.put("exchange_rate", getExchangeRate());
		disbursementMap.put("fee", getExchangeRate());
		disbursementMap.put("source_country", getSourceCountry());
		disbursementMap.put("destination_country", getDestinationCountry());
		disbursementMap.put("amount_in_sender_currency", getAmountInSenderCurrency());
		disbursementMap.put("beneficiary_currency_code", getBeneficiaryCurrencyCode());
		return disbursementMap;
	}
}
