package paymentgateway.disbursement;

import paymentgateway.disbursement.core.DisbursementResource;
import java.lang.reflect.Constructor;
import java.util.logging.Logger;

public class DisbursementResourceFactory {
    private static final Logger LOGGER = Logger.getLogger(DisbursementResourceFactory.class.getName());

    public DisbursementResourceFactory() {

    }

    public static DisbursementResource createDisbursementResource(String fullyQualifiedName, Object... base) {
        DisbursementResource resource = null;
        try {
            Class<?> clz = Class.forName(fullyQualifiedName);
            Constructor<?> constructor = clz.getDeclaredConstructors()[0];
            resource = (DisbursementResource) constructor.newInstance(base);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            LOGGER.severe("Failed to create instance of DisbursementResource.");
            LOGGER.severe("Given FQN: " + fullyQualifiedName);
            LOGGER.severe("Failed to run: Check your constructor argument");
            System.exit(20);
        } catch (ClassCastException e) {
            e.printStackTrace();
            LOGGER.severe("Failed to create instance of DisbursementResource.");
            LOGGER.severe("Given FQN: " + fullyQualifiedName);
            LOGGER.severe("Failed to cast the object");
            System.exit(30);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            LOGGER.severe("Failed to create instance of DisbursementResource.");
            LOGGER.severe("Given FQN: " + fullyQualifiedName);
            LOGGER.severe("Decorator can't be applied to the object");
            System.exit(40);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.severe("Failed to create instance of DisbursementResource.");
            LOGGER.severe("Given FQN: " + fullyQualifiedName);
            System.exit(50);
        }
        return resource;
    }

    public static boolean checkConfig(String fqn, Object base) {
        boolean isValid = true;
        if (fqn.equals(
                "paymentgateway.disbursement.resource.impl.DisbursementResourceImpl") ||
            fqn.equals(
                "paymentgateway.disbursement.resource.special.DisbursementResourceImpl")) {
            String baseName = base.getClass().getCanonicalName();
            isValid = baseName.equals("paymentgateway.disbursement.core.DisbursementResourceImpl");
        }
        return isValid;
    }
}
