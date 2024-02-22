package paymentgateway.product.flip;

import java.util.ArrayList;
import java.util.logging.Logger;

import vmj.routing.route.VMJServer;
import vmj.routing.route.Router;
import vmj.hibernate.integrator.HibernateUtil;
import org.hibernate.cfg.Configuration;

import paymentgateway.disbursement.DisbursementResourceFactory;
import paymentgateway.disbursement.core.DisbursementResource;

import prices.auth.vmj.model.UserResourceFactory;
import prices.auth.vmj.model.RoleResourceFactory;
import prices.auth.vmj.model.core.UserResource;
import prices.auth.vmj.model.core.RoleResource;

public class Flip {
	private static final Logger LOGGER = Logger.getLogger(Flip.class.getName());

	public static void main(String[] args) {
		activateServer("localhost", 7776);
		Configuration configuration = new Configuration();
		configuration.addAnnotatedClass(paymentgateway.disbursement.core.Disbursement.class);
		configuration.addAnnotatedClass(paymentgateway.disbursement.core.DisbursementComponent.class);
		configuration.addAnnotatedClass(paymentgateway.disbursement.core.DisbursementDecorator.class);
		configuration.addAnnotatedClass(paymentgateway.disbursement.core.DisbursementImpl.class);
		configuration.addAnnotatedClass(paymentgateway.disbursement.moneytransfer.MoneyTransferImpl.class);
		configuration.addAnnotatedClass(paymentgateway.disbursement.special.SpecialImpl.class);
		configuration.addAnnotatedClass(paymentgateway.disbursement.agent.AgentImpl.class);
//		configuration.addAnnotatedClass(paymentgateway.disbursement.scheduled.ScheduledImpl.class);
//		configuration.addAnnotatedClass(paymentgateway.disbursement.approval.ApprovalImpl.class);
		configuration.addAnnotatedClass(paymentgateway.disbursement.international.InternationalImpl.class);
//		configuration.addAnnotatedClass(paymentgateway.disbursement.internationaltransfer.InternationalTransferImpl.class);
//		configuration.addAnnotatedClass(paymentgateway.disbursement.scheduledtransfer.ScheduledTransferImpl.class);
//		configuration.addAnnotatedClass(paymentgateway.disbursement.approvaltransfer.ApprovalTransferImpl.class);

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
		DisbursementResource disbursement = DisbursementResourceFactory
				.createDisbursementResource(
						"paymentgateway.disbursement.core.DisbursementResourceImpl");
		LOGGER.info("================================");
		DisbursementResource moneytransfer = DisbursementResourceFactory
				.createDisbursementResource(
						"paymentgateway.disbursement.moneytransfer.MoneyTransferResourceImpl",
						DisbursementResourceFactory.createDisbursementResource(
								"paymentgateway.disbursement.core.DisbursementResourceImpl"));

		DisbursementResource specialmoneytransfer = DisbursementResourceFactory
				.createDisbursementResource(
						"paymentgateway.disbursement.specialmoneytransfer.SpecialMoneyTransferResourceImpl",
						moneytransfer);

		DisbursementResource agentmoneytransfer = DisbursementResourceFactory
				.createDisbursementResource(
						"paymentgateway.disbursement.agentmoneytransfer.AgentMoneyTransferResourceImpl",
						moneytransfer);

		DisbursementResource internationalmoneytransfer = DisbursementResourceFactory
				.createDisbursementResource(
						"paymentgateway.disbursement.internationalmoneytransfer.InternationalMoneyTransferResourceImpl",
						moneytransfer);


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

		LOGGER.info("Binding MoneyTransfer endpoints");
		Router.route(moneytransfer);

		LOGGER.info("Binding SpecialMoneyTransfer endpoints");
		Router.route(specialmoneytransfer);

		LOGGER.info("Binding AgentMoneyTransfer endpoints");
		Router.route(agentmoneytransfer);

		LOGGER.info("Binding InternationalMoneyTransfer endpoints");
		Router.route(internationalmoneytransfer);

//		System.out.println("multiplemoneytransfer endpoints binding");
//		Router.route(multipletransfer);

//		System.out.println("scheduledtransfer endpoints binding");
//		Router.route(scheduledtransfer);
//
//		System.out.println("approvaltransfer endpoints binding");
//		Router.route(approvaltransfer);

		LOGGER.info("Binding Disbursement endpoints");
		Router.route(disbursement);

		LOGGER.info("Binding Authorization endpoints");
		Router.route(userCore);
		Router.route(userPassworded);
		Router.route(userSocial);
		Router.route(role);
	}
}