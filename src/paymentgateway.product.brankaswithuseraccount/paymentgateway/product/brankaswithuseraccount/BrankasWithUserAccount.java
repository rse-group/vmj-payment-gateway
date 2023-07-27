package paymentgateway.product.brankaswithuseraccount;

import java.util.ArrayList;

import vmj.routing.route.VMJServer;
import vmj.routing.route.Router;
import vmj.hibernate.integrator.HibernateUtil;
import org.hibernate.cfg.Configuration;

import paymentgateway.apiauth.APIAuthResourceFactory;
import paymentgateway.apiauth.core.APIAuthResource;
import paymentgateway.client.ClientResourceFactory;
import paymentgateway.client.core.ClientResource;
import paymentgateway.fundtransfer.FundTransferResourceFactory;
import paymentgateway.fundtransfer.core.FundTransferResource;

import prices.auth.vmj.model.UserResourceFactory;
import prices.auth.vmj.model.RoleResourceFactory;
import prices.auth.vmj.model.core.UserResource;
import prices.auth.vmj.model.core.RoleResource;


public class BrankasWithUserAccount {

	public static void main(String[] args) {
		activateServer("localhost", 7776);
		Configuration configuration = new Configuration();
		configuration.addAnnotatedClass(paymentgateway.apiauth.core.APIAuth.class);
		configuration.addAnnotatedClass(paymentgateway.apiauth.core.APIAuthComponent.class);
		configuration.addAnnotatedClass(paymentgateway.apiauth.core.APIAuthDecorator.class);
		configuration.addAnnotatedClass(paymentgateway.apiauth.core.APIAuthImpl.class);
		configuration.addAnnotatedClass(paymentgateway.apiauth.basicauth.basicAuthImpl.class);
		configuration.addAnnotatedClass(paymentgateway.apiauth.apikey.APIKeyImpl.class);
		configuration.addAnnotatedClass(paymentgateway.client.core.Client.class);
		configuration.addAnnotatedClass(paymentgateway.client.core.ClientComponent.class);
		configuration.addAnnotatedClass(paymentgateway.client.core.ClientDecorator.class);
		configuration.addAnnotatedClass(paymentgateway.client.core.ClientImpl.class);
		configuration.addAnnotatedClass(paymentgateway.client.oy.OyImpl.class);
		configuration.addAnnotatedClass(paymentgateway.client.brankas.BrankasImpl.class);
		configuration.addAnnotatedClass(paymentgateway.fundtransfer.core.FundTransfer.class);
		configuration.addAnnotatedClass(paymentgateway.fundtransfer.core.FundTransferComponent.class);
		configuration.addAnnotatedClass(paymentgateway.fundtransfer.core.FundTransferDecorator.class);
		configuration.addAnnotatedClass(paymentgateway.fundtransfer.core.FundTransferImpl.class);
		configuration.addAnnotatedClass(paymentgateway.fundtransfer.withuseraccount.WithUserAccountImpl.class);
		
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
		APIAuthResource apiauth = APIAuthResourceFactory
			.createAPIAuthResource(
			"paymentgateway.apiauth.core.APIAuthResourceImpl"
			);
		APIAuthResource basicauth = APIAuthResourceFactory
			.createAPIAuthResource(
			"paymentgateway.apiauth.basicauth.basicAuthResourceImpl"
			,
			APIAuthResourceFactory.createAPIAuthResource(
			"paymentgateway.apiauth.core.APIAuthResourceImpl"));
		APIAuthResource apikey = APIAuthResourceFactory
			.createAPIAuthResource(
			"paymentgateway.apiauth.apikey.APIKeyResourceImpl"
			,
			APIAuthResourceFactory.createAPIAuthResource(
			"paymentgateway.apiauth.core.APIAuthResourceImpl"));
		ClientResource client = ClientResourceFactory
			.createClientResource(
			"paymentgateway.client.core.ClientResourceImpl"
			);
		ClientResource oy = ClientResourceFactory
			.createClientResource(
			"paymentgateway.client.oy.OyResourceImpl"
			,
			ClientResourceFactory.createClientResource(
			"paymentgateway.client.core.ClientResourceImpl"));
		ClientResource brankas = ClientResourceFactory
			.createClientResource(
			"paymentgateway.client.brankas.BrankasResourceImpl"
			,
			ClientResourceFactory.createClientResource(
			"paymentgateway.client.core.ClientResourceImpl"));
		FundTransferResource fundtransfer = FundTransferResourceFactory
			.createFundTransferResource(
			"paymentgateway.fundtransfer.core.FundTransferResourceImpl"
			);
		FundTransferResource withuseraccount = FundTransferResourceFactory
			.createFundTransferResource(
			"paymentgateway.fundtransfer.withuseraccount.WithUserAccountResourceImpl"
			,
			FundTransferResourceFactory.createFundTransferResource(
			"paymentgateway.fundtransfer.core.FundTransferResourceImpl"));
		
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

		System.out.println("withuseraccount endpoints binding");
		Router.route(withuseraccount);
		
		System.out.println("fundtransfer endpoints binding");
		Router.route(fundtransfer);
		
		System.out.println("brankas endpoints binding");
		Router.route(brankas);
		
		System.out.println("oy endpoints binding");
		Router.route(oy);
		
		System.out.println("client endpoints binding");
		Router.route(client);
		
		System.out.println("apikey endpoints binding");
		Router.route(apikey);
		
		System.out.println("basicauth endpoints binding");
		Router.route(basicauth);
		
		System.out.println("apiauth endpoints binding");
		Router.route(apiauth);
		
		
		System.out.println("auth endpoints binding");
		Router.route(userCore);
		Router.route(userPassworded);
		Router.route(userSocial);
		Router.route(role);
		System.out.println();
	}
}