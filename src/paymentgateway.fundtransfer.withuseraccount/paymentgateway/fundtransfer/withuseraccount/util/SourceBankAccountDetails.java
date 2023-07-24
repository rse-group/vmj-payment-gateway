package paymentgateway.fundtransfer.withuseraccount;

public class SourceBankAccountDetails {
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

