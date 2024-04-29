package paymentgateway.product.whoosh;

import java.util.logging.Logger;

import vmj.routing.route.VMJServer;
import vmj.routing.route.Router;
import vmj.hibernate.integrator.HibernateUtil;
import org.hibernate.cfg.Configuration;

import paymentgateway.payment.PaymentResourceFactory;
import paymentgateway.payment.core.PaymentResource;

import prices.auth.vmj.model.UserResourceFactory;
import prices.auth.vmj.model.RoleResourceFactory;
import prices.auth.vmj.model.core.UserResource;
import prices.auth.vmj.model.core.RoleResource;

public class Whoosh {
	private static final Logger LOGGER = Logger.getLogger(Whoosh.class.getName());

	public static void main(String[] args) {
		        // get hostAddress and portnum from env var
        // ex:
        // AMANAH_HOST_BE --> "localhost"
        // AMANAH_PORT_BE --> 7776
        String hostAddress= getEnvVariableHostAddress("AMANAH_HOST_BE");
        int portNum = getEnvVariablePortNumber("AMANAH_PORT_BE");
        activateServer(hostAddress, portNum);

        Configuration configuration = new Configuration();
        // panggil setter setelah membuat object dari kelas Configuration
        // ex:
        // AMANAH_DB_URL --> jdbc:postgresql://localhost:5432/superorg
        // AMANAH_DB_USERNAME --> postgres
        // AMANAH_DB_PASSWORD --> postgres123
        setDBProperties("AMANAH_DB_URL", "url", configuration);
        setDBProperties("AMANAH_DB_USERNAME", "username", configuration);
        setDBProperties("AMANAH_DB_PASSWORD","password", configuration);

		configuration.addAnnotatedClass(paymentgateway.payment.core.Payment.class);
		configuration.addAnnotatedClass(paymentgateway.payment.core.PaymentComponent.class);
		configuration.addAnnotatedClass(paymentgateway.payment.core.PaymentDecorator.class);
		configuration.addAnnotatedClass(paymentgateway.payment.core.PaymentImpl.class);

		configuration.addAnnotatedClass(paymentgateway.payment.virtualaccount.VirtualAccountImpl.class);
		configuration.addAnnotatedClass(paymentgateway.payment.ewallet.EWalletImpl.class);
		configuration.addAnnotatedClass(paymentgateway.payment.paymentlink.PaymentLinkImpl.class);

		configuration.addAnnotatedClass(prices.auth.vmj.model.core.Role.class);
		configuration.addAnnotatedClass(prices.auth.vmj.model.core.RoleComponent.class);
		configuration.addAnnotatedClass(prices.auth.vmj.model.core.RoleImpl.class);

		configuration.addAnnotatedClass(prices.auth.vmj.model.core.UserRole.class);
		configuration.addAnnotatedClass(prices.auth.vmj.model.core.UserRoleComponent.class);
		configuration.addAnnotatedClass(prices.auth.vmj.model.core.UserRoleImpl.class);

		configuration.addAnnotatedClass(prices.auth.vmj.model.core.User.class);
		configuration.addAnnotatedClass(prices.auth.vmj.model.core.UserComponent.class);
		configuration.addAnnotatedClass(prices.auth.vmj.model.core.UserDecorator.class);
		configuration.addAnnotatedClass(prices.auth.vmj.model.core.UserImpl.class);
		configuration.addAnnotatedClass(prices.auth.vmj.model.passworded.UserPasswordedImpl.class);
		configuration.addAnnotatedClass(prices.auth.vmj.model.social.UserSocialImpl.class);

		configuration.buildMappings();
		HibernateUtil.buildSessionFactory(configuration);

		createObjectsAndBindEndPoints();
	}

	public static void activateServer(String hostName, int portNumber) {
		VMJServer vmjServer = VMJServer.getInstance(hostName, portNumber);
		try {
			vmjServer.startServerGeneric();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void createObjectsAndBindEndPoints() {
		LOGGER.info("== CREATING OBJECTS AND BINDING ENDPOINTS ==");

		PaymentResource payment = PaymentResourceFactory
				.createPaymentResource(
					"paymentgateway.payment.core.PaymentResourceImpl");

		PaymentResource paymentlink = PaymentResourceFactory
				.createPaymentResource(
					"paymentgateway.payment.paymentlink.PaymentResourceImpl",
					PaymentResourceFactory.createPaymentResource(
						"paymentgateway.payment.core.PaymentResourceImpl"
					));
		
		PaymentResource virtualaccount = PaymentResourceFactory
				.createPaymentResource(
					"paymentgateway.payment.virtualaccount.PaymentResourceImpl",
					PaymentResourceFactory.createPaymentResource(
						"paymentgateway.payment.core.PaymentResourceImpl"
					));

		PaymentResource ewallet = PaymentResourceFactory
				.createPaymentResource(
					"paymentgateway.payment.ewallet.PaymentResourceImpl",
					PaymentResourceFactory.createPaymentResource(
						"paymentgateway.payment.core.PaymentResourceImpl"
					));


		LOGGER.info("================================");

		UserResource userCore = UserResourceFactory
				.createUserResource("prices.auth.vmj.model.core.UserResourceImpl");
		UserResource userPassworded = UserResourceFactory
				.createUserResource("prices.auth.vmj.model.passworded.UserPasswordedResourceDecorator",
						UserResourceFactory
								.createUserResource("prices.auth.vmj.model.core.UserResourceImpl"));
		UserResource userSocial = UserResourceFactory
				.createUserResource("prices.auth.vmj.model.social.UserSocialResourceDecorator",
						userPassworded);
		RoleResource role = RoleResourceFactory
				.createRoleResource("prices.auth.vmj.model.core.RoleResourceImpl");

		LOGGER.info("Binding Payment endpoints");
		Router.route(payment);

		LOGGER.info("Binding PaymentLink endpoints");
		Router.route(paymentlink);

		LOGGER.info("Binding VirtualAccount endpoints");
		Router.route(virtualaccount);

		LOGGER.info("Binding EWalllet endpoints");
		Router.route(ewallet);

		LOGGER.info("Binding Authorization endpoints");
		Router.route(userCore);
		Router.route(userPassworded);
		Router.route(userSocial);
		Router.route(role);
	}

	public static void setDBProperties(String varname, String typeProp, Configuration configuration) {
        String varNameValue = System.getenv(varname);
        String propertyName = String.format("hibernate.connection.%s",typeProp);
        if (varNameValue != null) {
            configuration.setProperty(propertyName, varNameValue);
        } else {
            String hibernatePropertyVal = configuration.getProperty(propertyName);
            if (hibernatePropertyVal == null) {
                String error_message = String.format("Please check '%s' in your local environment variable or "
                    + "'hibernate.connection.%s' in your 'hibernate.properties' file!", varname, typeProp);
                System.out.println(error_message);
            }
        }
    }

    // if the env variable for server host is null, use localhost instead.
    public static String getEnvVariableHostAddress(String varname_host){
            String hostAddress = System.getenv(varname_host)  != null ? System.getenv(varname_host) : "localhost"; // Host
            return hostAddress;
    }

    // try if the environment variable for port number is null, use 7776 instead
    public static int getEnvVariablePortNumber(String varname_port){
            String portNum = System.getenv(varname_port)  != null? System.getenv(varname_port)  : "7776"; //PORT
            int portNumInt = Integer.parseInt(portNum);
            return portNumInt;
    }
}