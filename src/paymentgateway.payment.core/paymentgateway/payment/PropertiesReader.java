package paymentgateway.payment;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import paymentgateway.payment.Constants;

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
}