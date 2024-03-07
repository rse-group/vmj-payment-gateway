package paymentgateway.config.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import paymentgateway.config.Constants;

public class PropertiesReader {
    private String fileName = Constants.DEFAULT_PRODUCT_FILE;

    public PropertiesReader(String fileName) {
        this.fileName = fileName;
    }

    public PropertiesReader() {
    }

    private Properties loadProperties() {
        FileInputStream input = null;
        Properties prop = new Properties();

        try {
            input = new FileInputStream(this.fileName);
            // load a properties file
            prop.load(input);
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return prop;
    }

    public String getProp(String propName) {
        Properties prop = this.loadProperties();
        return prop.getProperty(propName);
    }

    public String getPropOrDefault(String propName, String defaultValue) {
        String result = this.getProp(propName);
        return result != null ? result : defaultValue;
    }

    public String getEnvThenProp(String envName, String propName) {
        if (System.getenv(envName) != null) {
            return System.getenv(envName);
        }
        Properties prop = this.loadProperties();
        return prop.getProperty(propName);
    }

    public String getMidtransApiKey() {
        return this.getEnvThenProp(Constants.MIDTRANS_APIKEY_ENV, Constants.MIDTRANS_APIKEY_PROP);
    }

    public String getMidtransApiEndpoint() {
        return this.getEnvThenProp(Constants.MIDTRANS_APIENDPOINT_ENV, Constants.MIDTRANS_APIENDPOINT_PROP);
    }

//    public String getMidtransApikey(){
//        return this.getEnvThenProp(Constants.MIDTRANS_APIKEY_ENV, Constants.MIDTRANS_APIKEY_PROP);
//    }
    public String getMidtransBaseUrl() {
        return this.getEnvThenProp(Constants.MIDTRANS_BASE_URL_ENV, Constants.MIDTRANS_BASE_URL_PROP);
    }

    public String getMidtransPaymentLink(){
        return this.getEnvThenProp(Constants.MIDTRANS_PAYMENTLINK_ENV,Constants.MIDTRANS_PAYMENTLINK_PROP);
    }

    public String getMidtransEWallet(){
        return this.getEnvThenProp(Constants.MIDTRANS_APIENDPOINT_ENV,Constants.MIDTRANS_APIENDPOINT_PROP);
    }

    public String getMidtransVirtualAccount(){
        return this.getEnvThenProp(Constants.MIDTRANS_APIENDPOINT_ENV,Constants.MIDTRANS_APIENDPOINT_PROP);
    }

    public String getMidtransRetailOutlet(){
        return this.getEnvThenProp(Constants.MIDTRANS_APIENDPOINT_ENV,Constants.MIDTRANS_APIENDPOINT_PROP);
    }

    public String getMidtransDebitCard(){
        return this.getEnvThenProp(Constants.MIDTRANS_APIENDPOINT_ENV,Constants.MIDTRANS_APIENDPOINT_PROP);
    }
    public String getMidtransCreditCard(){
        return this.getEnvThenProp(Constants.MIDTRANS_APIENDPOINT_ENV,Constants.MIDTRANS_APIENDPOINT_PROP);
    }

    public String getOyBaseUrl() {
        return this.getEnvThenProp(Constants.OY_BASE_URL_ENV, Constants.OY_BASE_URL_PROP);
    }

    public String getOyPaymentLink(){
        return this.getEnvThenProp(Constants.OY_PAYMENTLINK_ENV,Constants.OY_PAYMENTLINK_PROP);
    }

    public String getOyEWallet(){
        return this.getEnvThenProp(Constants.OY_EWALLET_ENV,Constants.OY_EWALLET_PROP);
    }

    public String getOyVirtualAccount(){
        return this.getEnvThenProp(Constants.OY_VIRTUAL_ACCOUNT_ENV,Constants.OY_VIRTUAL_ACCOUNT_PROP);
    }

    public String getOyPaymentRouting(){
        return this.getEnvThenProp(Constants.OY_PAYMENT_ROUTING_ENV,Constants.OY_PAYMENT_ROUTING_PROP);
    }

    public String getOyRetailOutlet(){
        return this.getEnvThenProp(Constants.OY_RETAIL_OUTLET_ENV,Constants.OY_RETAIL_OUTLET_PROP);
    }

    public String getOyInvoice(){
        return this.getEnvThenProp(Constants.OY_INVOICE_ENV,Constants.OY_INVOICE_PROP);
    }


    public String getFlipBaseUrl() {
        return this.getEnvThenProp(Constants.FLIP_BASE_URL_TEST_ENV, Constants.FLIP_BASE_URL_TEST_PROP);
    }

    public String getFlipMoneyTransfer() {
        return this.getEnvThenProp(Constants.FLIP_MONEY_TRANSFER_ENV, Constants.FLIP_MONEY_TRANSFER_PROP);
    }

    public String getFlipSpecialMoneyTransfer() {
        return this.getEnvThenProp(Constants.FLIP_SPECIAL_MONEY_TRANSFER_ENV, Constants.FLIP_SPECIAL_MONEY_TRANSFER_PROP);
    }

    public String getFlipAgentMoneyTransfer() {
        return this.getEnvThenProp(Constants.FLIP_AGENT_MONEY_TRANSFER_ENV, Constants.FLIP_AGENT_MONEY_TRANSFER_PROP);
    }

    public String getFlipInternationalMoneyTransfer() {
        return this.getEnvThenProp(Constants.FLIP_INTERNATIONAL_MONEY_TRANSFER_ENV, Constants.FLIP_INTERNATIONAL_MONEY_TRANSFER_PROP);
    }

    public String getFlipInvoice(){
        return this.getEnvThenProp(Constants.FLIP_ACCEPT_PAYMENT_ENV, Constants.FLIP_ACCEPT_PAYMENT_PROP);
    }


    public HashMap<String, String> getMidtransHeaderParams() {
        HashMap<String, String> headerParams = new HashMap<>();
        String contentType = this.getEnvThenProp(Constants.MIDTRANS_CONTENT_TYPE_ENV,Constants.MIDTRANS_CONTENT_TYPE_PROP);
        String accept = this.getEnvThenProp(Constants.MIDTRANS_ACCEPT_ENV,Constants.MIDTRANS_ACCEPT_PROP);
        String auth = this.getEnvThenProp(Constants.MIDTRANS_APIKEY_ENV, Constants.MIDTRANS_APIKEY_PROP);
        headerParams.put("authorization",auth);
        headerParams.put("content-type",contentType);
        headerParams.put("accept", accept);
        return headerParams;
    }

    public HashMap<String, String> getOyHeaderParams() {
        HashMap<String, String> headerParams = new HashMap<>();
        String contentType = this.getEnvThenProp(Constants.OY_CONTENT_TYPE_ENV,Constants.OY_CONTENT_TYPE_PROP);
        String username = this.getEnvThenProp(Constants.OY_API_USERNAME_ENV,Constants.OY_API_USERNAME_PROP);
        String apikey = this.getEnvThenProp(Constants.OY_APIKEY_ENV, Constants.OY_APIKEY_PROP);
        headerParams.put("x-oy-username",username);
        headerParams.put("content-type",contentType);
        headerParams.put("x-api-key", apikey);
        return headerParams;
    }

    public HashMap<String, String> getXenditHeaderParams() {
        HashMap<String, String> headerParams = new HashMap<>();
        String contentType = this.getEnvThenProp(Constants.OY_CONTENT_TYPE_ENV,Constants.OY_CONTENT_TYPE_PROP);
        String username = this.getEnvThenProp(Constants.OY_API_USERNAME_ENV,Constants.OY_API_USERNAME_PROP);
        String apikey = this.getEnvThenProp(Constants.OY_APIKEY_ENV, Constants.OY_APIKEY_PROP);
        headerParams.put("x-oy-username",username);
        headerParams.put("content-type",contentType);
        headerParams.put("x-api-key", apikey);
        return headerParams;
    }

    public HashMap<String, String> getFlipHeaderParams() {
        HashMap<String, String> flipHeaderParams = new HashMap<>();
        String contentType = this.getEnvThenProp(Constants.FLIP_CONTENT_TYPE_ENV,Constants.FLIP_CONTENT_TYPE_PROP);
        String authorization = this.getEnvThenProp(Constants.FLIP_AUTHORIZATION_ENV,Constants.FLIP_AUTHORIZATION_PROP);
        String cookie = this.getEnvThenProp(Constants.FLIP_COOKIE_ENV,Constants.FLIP_COOKIE_PROP);
//        String xTimestamp = this.getEnvThenProp(Constants.FLIP_X_TIMESTAMP_ENV,Constants.FLIP_X_TIMESTAMP_ENV);
        flipHeaderParams.put("Content-Type",contentType);
        flipHeaderParams.put("idempotency-key", UUID.randomUUID().toString());
        flipHeaderParams.put("X-TIMESTAMP","");
        flipHeaderParams.put("Authorization",authorization);
        flipHeaderParams.put("Cookie",cookie);
        return flipHeaderParams;
    }
}