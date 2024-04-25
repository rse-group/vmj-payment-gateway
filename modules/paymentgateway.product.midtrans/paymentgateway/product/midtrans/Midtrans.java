package paymentgateway.product.midtrans;

import paymentgateway.payment.ewallet.EWalletImpl;
import paymentgateway.payment.paymentlink.PaymentLinkImpl;
import paymentgateway.payment.retailoutlet.RetailOutletImpl;
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


public class Midtrans {

	public static void main(String[] args) {
		activateServer("localhost", 7776);
		Configuration configuration = new Configuration();
		configuration.addAnnotatedClass(paymentgateway.payment.core.Payment.class);
		configuration.addAnnotatedClass(paymentgateway.payment.core.PaymentComponent.class);
		configuration.addAnnotatedClass(paymentgateway.payment.core.PaymentDecorator.class);
		configuration.addAnnotatedClass(paymentgateway.payment.core.PaymentImpl.class);
		configuration.addAnnotatedClass(paymentgateway.payment.invoice.PaymentImpl.class);
		configuration.addAnnotatedClass(paymentgateway.payment.paymentrouting.PaymentImpl.class);
		configuration.addAnnotatedClass(paymentgateway.payment.paymentlink.PaymentLinkImpl.class);
		configuration.addAnnotatedClass(paymentgateway.payment.virtualaccount.VirtualAccountImpl.class);
		configuration.addAnnotatedClass(paymentgateway.payment.ewallet.EWalletImpl.class);
		configuration.addAnnotatedClass(paymentgateway.payment.debitcard.PaymentImpl.class);
		configuration.addAnnotatedClass(paymentgateway.payment.creditcard.PaymentImpl.class);
		configuration.addAnnotatedClass(paymentgateway.payment.retailoutlet.RetailOutletImpl.class);
		
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
		System.out.println("== CREATING OBJECTS AND BINDING ENDPOINTS ==");
		PaymentResource payment = PaymentResourceFactory
			.createPaymentResource(
			"paymentgateway.payment.core.PaymentResourceImpl"
			);
		PaymentResource invoice = PaymentResourceFactory
			.createPaymentResource(
			"paymentgateway.payment.invoice.PaymentResourceImpl"
			,
			PaymentResourceFactory.createPaymentResource(
			"paymentgateway.payment.core.PaymentResourceImpl"));
		
		PaymentResource paymentlink = PaymentResourceFactory
				.createPaymentResource(
				"paymentgateway.payment.paymentlink.PaymentResourceImpl"
				,
				PaymentResourceFactory.createPaymentResource(
				"paymentgateway.payment.core.PaymentResourceImpl"));
		
//		PaymentResource midtransimpl = PaymentResourceFactory
//				.createPaymentResource(
//				"paymentgateway.payment.midtransimpl.PaymentResourceImpl"
//				,
//				paymentlink);
		
//		PaymentResource paymentlinkmidtrans = PaymentResourceFactory
//			.createPaymentResource(
//			"paymentgateway.payment.paymentlinkmidtrans.PaymentResourceImpl"
//			,
//			paymentlink);
		PaymentResource virtualaccount = PaymentResourceFactory
			.createPaymentResource(
			"paymentgateway.payment.virtualaccount.PaymentResourceImpl"
			,
			PaymentResourceFactory.createPaymentResource(
			"paymentgateway.payment.core.PaymentResourceImpl"));
		PaymentResource ewallet = PaymentResourceFactory
			.createPaymentResource(
			"paymentgateway.payment.ewallet.PaymentResourceImpl"
			,
			PaymentResourceFactory.createPaymentResource(
			"paymentgateway.payment.core.PaymentResourceImpl"));
		PaymentResource debitcard = PaymentResourceFactory
			.createPaymentResource(
			"paymentgateway.payment.debitcard.PaymentResourceImpl"
			,
			PaymentResourceFactory.createPaymentResource(
			"paymentgateway.payment.core.PaymentResourceImpl"));
		PaymentResource creditcard = PaymentResourceFactory
			.createPaymentResource(
			"paymentgateway.payment.creditcard.PaymentResourceImpl"
			,
			PaymentResourceFactory.createPaymentResource(
			"paymentgateway.payment.core.PaymentResourceImpl"));
		PaymentResource retailoutlet = PaymentResourceFactory
			.createPaymentResource(
			"paymentgateway.payment.retailoutlet.PaymentResourceImpl"
			,
			PaymentResourceFactory.createPaymentResource(
			"paymentgateway.payment.core.PaymentResourceImpl"));

		PaymentResource paymentrouting = PaymentResourceFactory
				.createPaymentResource(
						"paymentgateway.payment.paymentrouting.PaymentResourceImpl"
						,
						PaymentResourceFactory.createPaymentResource(
								"paymentgateway.payment.core.PaymentResourceImpl"));
		
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

		System.out.println("retailoutlet endpoints binding");
		Router.route(retailoutlet);
		
		System.out.println("creditcard endpoints binding");
		Router.route(creditcard);
//
		System.out.println("debitcard endpoints binding");
		Router.route(debitcard);
//
		System.out.println("ewallet endpoints binding");
		Router.route(ewallet);

		System.out.println("virtualaccount endpoints binding");
		Router.route(virtualaccount);
//
		System.out.println("paymentlink endpoints binding");
		Router.route(paymentlink);

		System.out.println("paymentrouting endpoints binding");
		Router.route(paymentrouting);
		
		System.out.println("invoice endpoints binding");
		Router.route(invoice);
		
		System.out.println("payment endpoints binding");
		Router.route(payment);
		
		
		System.out.println("auth endpoints binding");
		Router.route(userCore);
		Router.route(userPassworded);
		Router.route(userSocial);
		Router.route(role);
		System.out.println();
	}
}