package paymentgateway.disbursement;

import paymentgateway.disbursement.core.Disbursement;
import java.lang.reflect.Constructor;
import java.util.logging.Logger;

public class DisbursementFactory {
    private static final Logger LOGGER = Logger.getLogger(DisbursementFactory.class.getName());

    public DisbursementFactory() {

    }

    public static Disbursement createDisbursement(String fullyQualifiedName, Object... base) {
        Disbursement record = null;
        try {
            Class<?> clz = Class.forName(fullyQualifiedName);
            Constructor<?> constructor = clz.getDeclaredConstructors()[0];
            record = (Disbursement) constructor.newInstance(base);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            LOGGER.severe("Failed to create instance of Disbursement.");
            LOGGER.severe("Given FQN: " + fullyQualifiedName);
            LOGGER.severe("Failed to run: Check your constructor argument");
            System.exit(20);
        } catch (ClassCastException e) {
            e.printStackTrace();
            LOGGER.severe("Failed to create instance of Disbursement.");
            LOGGER.severe("Given FQN: " + fullyQualifiedName);
            LOGGER.severe("Failed to cast the object");
            System.exit(30);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            LOGGER.severe("Failed to create instance of Disbursement.");
            LOGGER.severe("Given FQN: " + fullyQualifiedName);
            LOGGER.severe("Decorator can't be applied to the object");
            System.exit(40);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.severe("Failed to create instance of Disbursement.");
            LOGGER.severe("Given FQN: " + fullyQualifiedName);
            System.exit(50);
        }
        return record;
    }

}
