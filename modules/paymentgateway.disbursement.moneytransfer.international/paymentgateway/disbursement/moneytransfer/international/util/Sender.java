package paymentgateway.disbursement.moneytransfer.international;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Sender {

	private String name;
	private String place_of_birth;
	private String date_of_birth;
	private String address;
	private String identity_type;
	private String identity_number;
	private String country;
	private String job;
	private String email;
	private String city;
	private String phone_number;

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

	public String getDate_of_birth() {
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

	public void setDate_of_birth(String date_of_birth) {
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