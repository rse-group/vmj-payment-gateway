package paymentgateway.disbursement;

import paymentgateway.disbursement.core.DisbursementService;

import java.lang.reflect.Constructor;
import java.util.logging.Logger;

public class DisbursementServiceFactory {
    private static final Logger LOGGER = Logger.getLogger(DisbursementServiceFactory.class.getName());

    public DisbursementServiceFactory() {

    }

    public static DisbursementService createDisbursementService(String fullyQualifiedName, Object... base) {
        DisbursementService record = null;
        try {
            Class<?> clz = Class.forName(fullyQualifiedName);
            Constructor<?> constructor = clz.getDeclaredConstructors()[0];
            record = (DisbursementService) constructor.newInstance(base);
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

    public static boolean checkConfig(String fqn, Object base) {
        boolean a = true;
        if (fqn.equals(
                "paymentgateway.disbursement.agent.DisbursementServiceImpl") ||
            fqn.equals(
                "paymentgateway.disbursement.international.DisbursementServiceImpl") ||
            fqn.equals(
                "paymentgateway.disbursement.special.DisbursementServiceImpl")) {
            String baseku = base.getClass().getCanonicalName();
            a = baseku.equals("paymentgateway.disbursement.core.DisbursementServiceImpl");
        }
        return a;
    }
}
