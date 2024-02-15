package paymentgateway.fundtransfer.core;

import java.util.*;
import java.math.BigInteger;

import javax.persistence.OneToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.CascadeType;

@MappedSuperclass
public abstract class FundTransferDecorator extends FundTransferComponent {
    //https://stackoverflow.com/questions/9116630/one-to-one-delete-on-cascade
    @OneToOne(cascade=CascadeType.REMOVE, optional=true)
    protected FundTransferComponent record;

    public FundTransferDecorator(FundTransferComponent record) {
        String generateUUIDNo = String.format("%010d",new BigInteger(UUID.randomUUID().toString().replace("-",""),16));
        String unique_no = generateUUIDNo.substring(0,5);
        this.id = Integer.parseInt(unique_no);
        this.record = record;
    }

    public FundTransferDecorator() {
    }

    public int getId() {
        return record.getId();
    }
    public void setId(int id){
        record.setId(id);
    }
    public int getUserId() {

        return record.getUserId();
    }
    public void setUserId(int userId) {

        record.setUserId(userId);
    }
    public String getAccountNumber() {

        return record.getAccountNumber();
    }
    public void setAccountNumber(String accountNumber) {

        record.setAccountNumber(accountNumber);
    }
    public String getBankCode() {

        return record.getBankCode();
    }
    public void setBankCode(String bankCode) {

        record.setBankCode(bankCode);
    }
    public double getAmount() {

        return record.getAmount();
    }
    public void setAmount(double amount) {

        record.setAmount(amount);
    }

}
