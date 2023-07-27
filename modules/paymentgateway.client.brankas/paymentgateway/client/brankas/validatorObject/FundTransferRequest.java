package paymentgateway.client.brankas;

class Address {
    private String line1;
    private String line2;
    private String city;
    private String province;
    private String zip_code;
    private String country;

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

class Amount {
    private String cur;
    private String num;

    public String getCur() {
        return cur;
    }

    public void setCur(String cur) {
        this.cur = cur;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}

class DestinationBankAccountDetails {
    private String account_id;
    private String bank;
    private String number;
    private String holder_name;
    private String type;
    private Address address;
    private String bank_title;
    private String country_code;
    private String external_account_id;
    private String email;

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getHolder_name() {
        return holder_name;
    }

    public void setHolder_name(String holder_name) {
        this.holder_name = holder_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getBank_title() {
        return bank_title;
    }

    public void setBank_title(String bank_title) {
        this.bank_title = bank_title;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getExternal_account_id() {
        return external_account_id;
    }

    public void setExternal_account_id(String external_account_id) {
        this.external_account_id = external_account_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

class DisbursementRequest {
    private String disbursement_id;
    private String reference_id;
    private String type;
    private SourceBankAccountDetails source_account;
    private DestinationBankAccountDetails destination_account;
    private Amount destination_amount;
    private String description;
    private Fee[] fees;
    private External external;
    private String status;
    private Timestamp created;
    private Timestamp updated;
    private String disbursement_request_id;
    private String note;
    private String merchant_txn_id;

    public String getDisbursement_id() {
        return disbursement_id;
    }

    public void setDisbursement_id(String disbursement_id) {
        this.disbursement_id = disbursement_id;
    }

    public String getReference_id() {
        return reference_id;
    }

    public void setReference_id(String reference_id) {
        this.reference_id = reference_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SourceBankAccountDetails getSource_account() {
        return source_account;
    }

    public void setSource_account(SourceBankAccountDetails source_account) {
        this.source_account = source_account;
    }

    public DestinationBankAccountDetails getDestination_account() {
        return destination_account;
    }

    public void setDestination_account(DestinationBankAccountDetails destination_account) {
        this.destination_account = destination_account;
    }

    public Amount getDestination_amount() {
        return destination_amount;
    }

    public void setDestination_amount(Amount destination_amount) {
        this.destination_amount = destination_amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Fee[] getFees() {
        return fees;
    }

    public void setFees(Fee[] fees) {
        this.fees = fees;
    }

    public External getExternal() {
        return external;
    }

    public void setExternal(External external) {
        this.external = external;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public String getDisbursement_request_id() {
        return disbursement_request_id;
    }

    public void setDisbursement_request_id(String disbursement_request_id) {
        this.disbursement_request_id = disbursement_request_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMerchant_txn_id() {
        return merchant_txn_id;
    }

    public void setMerchant_txn_id(String merchant_txn_id) {
        this.merchant_txn_id = merchant_txn_id;
    }
}

class Error {
    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

class External {
    private String reference_id;
    private String status;
    private String settlement_rail;
    private Error[] error;

    public String getReference_id() {
        return reference_id;
    }

    public void setReference_id(String reference_id) {
        this.reference_id = reference_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSettlement_rail() {
        return settlement_rail;
    }

    public void setSettlement_rail(String settlement_rail) {
        this.settlement_rail = settlement_rail;
    }

    public Error[] getError() {
        return error;
    }

    public void setError(Error[] error) {
        this.error = error;
    }
}

class Fee {
    private String name;
    private Amount amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }
}

class SourceBankAccountDetails {
    private String account_id;
    private String bank;
    private String number;
    private String holder_name;
    private String type;
    private Address address;
    private String bank_title;
    private String country_code;
    private String external_account_id;

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getHolder_name() {
        return holder_name;
    }

    public void setHolder_name(String holder_name) {
        this.holder_name = holder_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getBank_title() {
        return bank_title;
    }

    public void setBank_title(String bank_title) {
        this.bank_title = bank_title;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getExternal_account_id() {
        return external_account_id;
    }

    public void setExternal_account_id(String external_account_id) {
        this.external_account_id = external_account_id;
    }

}

class Timestamp {
    private int seconds;
    private int nanos;

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getNanos() {
        return nanos;
    }

    public void setNanos(int nanos) {
        this.nanos = nanos;
    }
}

public class FundTransferRequest {
    private String source_account_id;
    private String remark;
    private DisbursementRequest[] disbursements;

    public String getSource_account_id() {
        return source_account_id;
    }

    public void setSource_account_id(String source_account_id) {
        this.source_account_id = source_account_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public DisbursementRequest[] getDisbursements() {
        return disbursements;
    }

    public void setDisbursements(DisbursementRequest[] disbursements) {
        this.disbursements = disbursements;
    }
}