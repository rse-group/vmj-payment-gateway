package paymentgateway.disbursement.core.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import paymentgateway.disbursement.Constants;

public class PropertiesReader {
    private String fileName = Constants.DEFAULT_DISBURSEMENT_FILE;

    public PropertiesReader(String fileName) {
        this.fileName = fileName;
    }

    public PropertiesReader() {}

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
                } catch (IOException e) {}
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


    public String getFlipBaseUrlTest() {
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

