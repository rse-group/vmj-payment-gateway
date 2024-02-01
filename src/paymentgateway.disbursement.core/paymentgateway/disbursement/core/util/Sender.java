package paymentgateway.disbursement.core;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Sender {
	//from international transfer
	private String name;
	private String place_of_birth;
	private Date date_of_birth;
	private String address;
	private String identity_type;
	private String identity_number;
	private String country;
	private String job;
	private String email;
	private String city;
	private String phone_number;
	//from special transfer
	private int sender_country;
	private String sender_name;
	private String sender_address;
	private String sender_job;
	private int sender_place_of_birth;
	private String sender_date_of_birth;
	private String sender_identity_type;
	private String sender_identity_number;



	// ================================== special transfer

	public String getSender_address() {
		return sender_address;
	}

	public String getSender_name() {
		return sender_name;
	}

	public int getSender_country() {
		return sender_country;
	}

	public String getSender_job() {
		return sender_job;
	}

	public void setSender_country(int sender_country) {
		this.sender_country = sender_country;
	}

	public void setSender_address(String sender_address) {
		this.sender_address = sender_address;
	}

	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}

	public void setSender_job(String sender_job) {
		this.sender_job = sender_job;
	}

	// ================================== end ========

	public String getCountry() {
		return country;
	}

	public String getCity() {
		return city;
	}

	public String getEmail() {
		return email;
	}

	public String getAddress() {
		return address;
	}

	public Date getDate_of_birth() {
		return date_of_birth;
	}

	public String getIdentity_number() {
		return identity_number;
	}

	public String getIdentity_type() {
		return identity_type;
	}

	public String getJob() {
		return job;
	}

	public String getName() {
		return name;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public String getPlace_of_birth() {
		return place_of_birth;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setDate_of_birth(Date date_of_birth) {
		this.date_of_birth = date_of_birth;
	}

	public void setIdentity_number(String identity_number) {
		this.identity_number = identity_number;
	}

	public void setIdentity_type(String identity_type) {
		this.identity_type = identity_type;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public void setPlace_of_birth(String place_of_birth) {
		this.place_of_birth = place_of_birth;
	}
}