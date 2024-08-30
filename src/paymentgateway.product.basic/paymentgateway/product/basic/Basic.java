package paymentgateway.product.basic;

import java.util.ArrayList;

import vmj.routing.route.VMJServer;
import vmj.routing.route.Router;
import vmj.hibernate.integrator.HibernateUtil;
import org.hibernate.cfg.Configuration;

import vmj.auth.model.UserResourceFactory;
import vmj.auth.model.RoleResourceFactory;
import vmj.auth.model.core.UserResource;
import vmj.auth.model.core.RoleResource;

import paymentgateway.disbursement.DisbursementResourceFactory;
import paymentgateway.disbursement.core.DisbursementResource;
import paymentgateway.payment.PaymentResourceFactory;
import paymentgateway.payment.core.PaymentResource;

public class Basic {

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

		configuration.addAnnotatedClass(vmj.auth.model.core.Role.class);
        configuration.addAnnotatedClass(vmj.auth.model.core.RoleComponent.class);
        configuration.addAnnotatedClass(vmj.auth.model.core.RoleDecorator.class);
        configuration.addAnnotatedClass(vmj.auth.model.core.RoleImpl.class);
        configuration.addAnnotatedClass(vmj.auth.model.core.UserRole.class);
        configuration.addAnnotatedClass(vmj.auth.model.core.UserRoleComponent.class);
        configuration.addAnnotatedClass(vmj.auth.model.core.UserRoleDecorator.class);
        configuration.addAnnotatedClass(vmj.auth.model.core.UserRoleImpl.class);
        configuration.addAnnotatedClass(vmj.auth.model.core.User.class);
        configuration.addAnnotatedClass(vmj.auth.model.core.UserComponent.class);
        configuration.addAnnotatedClass(vmj.auth.model.core.UserDecorator.class);
        configuration.addAnnotatedClass(vmj.auth.model.core.UserImpl.class);
        configuration.addAnnotatedClass(vmj.auth.model.passworded.UserImpl.class);

		configuration.addAnnotatedClass(paymentgateway.disbursement.core.Disbursement.class);
		configuration.addAnnotatedClass(paymentgateway.disbursement.core.DisbursementComponent.class);
		configuration.addAnnotatedClass(paymentgateway.disbursement.core.DisbursementDecorator.class);
		configuration.addAnnotatedClass(paymentgateway.disbursement.core.DisbursementImpl.class);
		configuration.addAnnotatedClass(paymentgateway.disbursement.moneytransfer.MoneyTransferImpl.class);
		configuration.addAnnotatedClass(paymentgateway.disbursement.moneytransfer.international.InternationalImpl.class);
		configuration.addAnnotatedClass(paymentgateway.payment.core.Payment.class);
		configuration.addAnnotatedClass(paymentgateway.payment.core.PaymentComponent.class);
		configuration.addAnnotatedClass(paymentgateway.payment.core.PaymentDecorator.class);
		configuration.addAnnotatedClass(paymentgateway.payment.core.PaymentImpl.class);
		configuration.addAnnotatedClass(paymentgateway.payment.ewallet.EWalletImpl.class);
		configuration.addAnnotatedClass(paymentgateway.payment.paymentlink.PaymentLinkImpl.class);
		configuration.addAnnotatedClass(paymentgateway.payment.virtualaccount.VirtualAccountImpl.class);

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
		System.out.println("== CREATING OBJECTS AND BINDING ENDPOINTS ==");
		UserResource userResource = UserResourceFactory
            .createUserResource("vmj.auth.model.core.UserResourceImpl"
			);

		RoleResource roleResource = RoleResourceFactory
        	.createRoleResource("vmj.auth.model.core.RoleResourceImpl"
			);
        
        UserResource userPasswordedResource = UserResourceFactory
	        .createUserResource("vmj.auth.model.passworded.UserResourceImpl"
			,
		    UserResourceFactory.createUserResource("vmj.auth.model.core.UserResourceImpl"));

		DisbursementResource disbursementDisbursementResource = DisbursementResourceFactory
			.createDisbursementResource("paymentgateway.disbursement.core.DisbursementResourceImpl"
			);
		
		DisbursementResource moneytransferDisbursementResource = DisbursementResourceFactory
			.createDisbursementResource("paymentgateway.disbursement.moneytransfer.MoneyTransferResourceImpl"
			,
			disbursementDisbursementResource);
		
		DisbursementResource internationalDisbursementResource = DisbursementResourceFactory
			.createDisbursementResource("paymentgateway.disbursement.moneytransfer.international.InternationalResourceImpl"
			,
			moneytransferDisbursementResource);
		
		DisbursementResource internationalmoneytransferDisbursementResource = DisbursementResourceFactory
			.createDisbursementResource("paymentgateway.disbursement.moneytransfer.internationalmoneytransfer.InternationalMoneyTransferResourceImpl"
			,
			moneytransferDisbursementResource);
		
		PaymentResource paymentPaymentResource = PaymentResourceFactory
			.createPaymentResource("paymentgateway.payment.core.PaymentResourceImpl"
			);
		
		PaymentResource ewalletPaymentResource = PaymentResourceFactory
			.createPaymentResource("paymentgateway.payment.ewallet.PaymentResourceImpl"
			,
			paymentPaymentResource);
		
		PaymentResource paymentlinkPaymentResource = PaymentResourceFactory
			.createPaymentResource("paymentgateway.payment.paymentlink.PaymentResourceImpl"
			,
			paymentPaymentResource);
		
		PaymentResource virtualaccountPaymentResource = PaymentResourceFactory
			.createPaymentResource("paymentgateway.payment.virtualaccount.PaymentResourceImpl"
			,
			paymentPaymentResource);
		

		System.out.println("virtualaccountPaymentResource endpoints binding");
		Router.route(virtualaccountPaymentResource);
		
		System.out.println("paymentlinkPaymentResource endpoints binding");
		Router.route(paymentlinkPaymentResource);
		
		System.out.println("ewalletPaymentResource endpoints binding");
		Router.route(ewalletPaymentResource);
		
		System.out.println("paymentPaymentResource endpoints binding");
		Router.route(paymentPaymentResource);
		
		System.out.println("internationalmoneytransferDisbursementResource endpoints binding");
		Router.route(internationalmoneytransferDisbursementResource);
		
		System.out.println("internationalDisbursementResource endpoints binding");
		Router.route(internationalDisbursementResource);
		
		System.out.println("moneytransferDisbursementResource endpoints binding");
		Router.route(moneytransferDisbursementResource);
		
		System.out.println("disbursementDisbursementResource endpoints binding");
		Router.route(disbursementDisbursementResource);
		
		System.out.println("authResource endpoints binding");
		Router.route(userPasswordedResource);
		Router.route(roleResource);
		Router.route(userResource);
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
