package paymentgateway.paymentchannel;

import paymentgateway.paymentchannel.core.PaymentChannel;
import java.lang.reflect.Constructor;
import java.util.logging.Logger;

public class PaymentChannelFactory{
    private static final Logger LOGGER = Logger.getLogger(PaymentChannelFactory.class.getName());

    public PaymentChannelFactory()
    {

    }

    public static PaymentChannel createPaymentChannel(String fullyQualifiedName, Object ... base)
    {
        PaymentChannel record = null;
        try {
            Class<?> clz = Class.forName(fullyQualifiedName);
            Constructor<?> constructor = clz.getDeclaredConstructors()[0];
            record = (PaymentChannel) constructor.newInstance(base);
        } 
        catch (IllegalArgumentException e)
        {
            LOGGER.severe("Failed to create instance of PaymentChannel.");
            LOGGER.severe("Given FQN: " + fullyQualifiedName);
            LOGGER.severe("Failed to run: Check your constructor argument");
            System.exit(20);
        }
        catch (ClassCastException e)
        {   LOGGER.severe("Failed to create instance of PaymentChannel.");
            LOGGER.severe("Given FQN: " + fullyQualifiedName);
            LOGGER.severe("Failed to cast the object");
            System.exit(30);
        }
        catch (ClassNotFoundException e)
        {
            LOGGER.severe("Failed to create instance of PaymentChannel.");
            LOGGER.severe("Given FQN: " + fullyQualifiedName);
            LOGGER.severe("Decorator can't be applied to the object");
            System.exit(40);
        }
        catch (Exception e)
        {
            LOGGER.severe("Failed to create instance of PaymentChannel.");
            LOGGER.severe("Given FQN: " + fullyQualifiedName);
            System.exit(50);
        }
        return record;
    }

}
