package paymentgateway.disbursement.moneytransfer.international;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Beneficiary {

	private String id_number;
	private String id_expiration_date;
	private String full_name;
	private String bank_account_number;
	private String bank;
	private String email;
	private String msisdn;
	private String nationality;
	private String country;
	private String province;
	private String city;
	private String address;
	private String postal_code;
	private String relationship;
	private String source_of_funds;
	private String remittance_prpose;
	private String iban;
	private String swift_bic_code;
	private String sort_code;
	private String ifs_code;
	private String bsb_number;
	private String branch_number;
	private String document_reference_number;
	private String registration_number;

	public String getId_number() {
		return id_number;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public String getAddress() {
		return address;
	}

	public String getBank_account_number() {
		return bank_account_number;
	}

	public String getFull_name() {
		return full_name;
	}

	public String getId_expiration_date() {
		return id_expiration_date;
	}

	public String getEmail() {
		return email;
	}

	public String getBank() {
		return bank;
	}

	public String getBranch_number() {
		return branch_number;
	}

	public String getBsb_number() {
		return bsb_number;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public String getDocument_reference_number() {
		return document_reference_number;
	}

	public String getIban() {
		return iban;
	}

	public String getIfs_code() {
		return ifs_code;
	}

	public String getNationality() {
		return nationality;
	}

	public String getPostal_code() {
		return postal_code;
	}

	public String getProvince() {
		return province;
	}

	public String getRegistration_number() {
		return registration_number;
	}

	public String getRelationship() {
		return relationship;
	}

	public String getRemittance_prpose() {
		return remittance_prpose;
	}

	public String getSort_code() {
		return sort_code;
	}

	public String getSource_of_funds() {
		return source_of_funds;
	}

	public String getSwift_bic_code() {
		return swift_bic_code;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

	public void setBank_account_number(String bank_account_number) {
		this.bank_account_number = bank_account_number;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public void setId_expiration_date(String id_expiration_date) {
		this.id_expiration_date = id_expiration_date;
	}

	public void setId_number(String id_number) {
		this.id_number = id_number;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setBranch_number(String branch_number) {
		this.branch_number = branch_number;
	}

	public void setBsb_number(String bsb_number) {
		this.bsb_number = bsb_number;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setDocument_reference_number(String document_reference_number) {
		this.document_reference_number = document_reference_number;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public void setIfs_code(String ifs_code) {
		this.ifs_code = ifs_code;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setRegistration_number(String registration_number) {
		this.registration_number = registration_number;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public void setRemittance_prpose(String remittance_prpose) {
		this.remittance_prpose = remittance_prpose;
	}

	public void setSort_code(String sort_code) {
		this.sort_code = sort_code;
	}

	public void setSource_of_funds(String source_of_funds) {
		this.source_of_funds = source_of_funds;
	}

	public void setSwift_bic_code(String swift_bic_code) {
		this.swift_bic_code = swift_bic_code;
	}
}