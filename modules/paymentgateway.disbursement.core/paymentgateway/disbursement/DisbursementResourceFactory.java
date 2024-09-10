package paymentgateway.disbursement;

import paymentgateway.disbursement.core.DisbursementResource;

import java.lang.reflect.Constructor;
import java.util.logging.Logger;

public class DisbursementResourceFactory {
    private static final Logger LOGGER = Logger.getLogger(DisbursementResourceFactory.class.getName());

    public DisbursementResourceFactory() {

    }

    public static DisbursementResource createDisbursementResource(String fullyQualifiedName, Object... base) {
        DisbursementResource record = null;
        try {
            Class<?> clz = Class.forName(fullyQualifiedName);
            Constructor<?> constructor = clz.getDeclaredConstructors()[0];
            record = (DisbursementResource) constructor.newInstance(base);
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
                "paymentgateway.disbursement.agent.DisbursementResourceImpl") ||
            fqn.equals(
                "paymentgateway.disbursement.international.DisbursementResourceImpl") ||
            fqn.equals(
                "paymentgateway.disbursement.special.DisbursementResourceImpl")) {
            String baseku = base.getClass().getCanonicalName();
            a = baseku.equals("paymentgateway.disbursement.core.DisbursementResourceService");
        }
        return a;
    }
}
