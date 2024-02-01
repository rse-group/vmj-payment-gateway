package paymentgateway.disbursement.core;

import java.util.List;
public class GetAllDisbursementResponse {

    private int total_page;
    private int data_per_page;
    private int page;

    private List<MoneyTransferResponse> data;

    public List<MoneyTransferResponse> getData() {
        return data;
    }

    public void setData(List<MoneyTransferResponse> data) {
        this.data = data;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public int getData_per_page() {
        return data_per_page;
    }

    public void setData_per_page(int data_per_page) {
        this.data_per_page = data_per_page;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
