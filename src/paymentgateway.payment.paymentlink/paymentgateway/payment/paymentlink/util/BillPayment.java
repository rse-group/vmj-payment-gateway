package paymentgateway.payment.paymentlink;

public class BillPayment {
    private String id;
    private int amount;
    private int uniqueCode;
    private String status;
    private String senderBank;
    private String senderBankType;
    private ReceiverBank receiverBank;
    private String userAddress;
    private String userPhone;
    private long createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(int uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSenderBank() {
        return senderBank;
    }

    public void setSenderBank(String senderBank) {
        this.senderBank = senderBank;
    }

    public String getSenderBankType() {
        return senderBankType;
    }

    public void setSenderBankType(String senderBankType) {
        this.senderBankType = senderBankType;
    }

    public ReceiverBank getReceiverBank() {
        return receiverBank;
    }

    public void setReceiverBank(ReceiverBank receiverBank) {
        this.receiverBank = receiverBank;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}

