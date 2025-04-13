package paymentgateway.product.basic;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Type;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import vmj.routing.route.VMJCors;
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
import paymentgateway.disbursement.DisbursementServiceFactory;
import paymentgateway.disbursement.core.DisbursementService;

public class Basic {

	public static void main(String[] args) {

		// get hostAddress and portnum from env var
        // ex:
        // AMANAH_HOST_BE --> "localhost"
        // AMANAH_PORT_BE --> 7776
		String hostAddress= getEnvVariableHostAddress("AMANAH_HOST_BE");
        int portNum = getEnvVariablePortNumber("AMANAH_PORT_BE");
        activateServer(hostAddress, portNum);
		setCors();

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
		configuration.addAnnotatedClass(paymentgateway.disbursement.international.InternationalImpl.class);
		configuration.addAnnotatedClass(paymentgateway.disbursement.special.SpecialImpl.class);
		configuration.addAnnotatedClass(paymentgateway.disbursement.agent.AgentImpl.class);
		configuration.addAnnotatedClass(paymentgateway.disbursement.specifiedrecipient.SpecifiedRecipientImpl.class);

		Map<String, Object> featureModelMappings = mappingFeatureModel();
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, Map<String, String[]>>>(){}.getType();
        String convertedFeatureModelMappings = gson.toJson(featureModelMappings, type);
		
        configuration.setProperty("feature.model.mappings", convertedFeatureModelMappings);
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

        DisbursementService disbursementDisbursement2Service = DisbursementServiceFactory
            .createDisbursementService("paymentgateway.disbursement.core.DisbursementServiceImpl"
            	);		

        DisbursementResource disbursementDisbursement2Resource = DisbursementResourceFactory
            .createDisbursementResource("paymentgateway.disbursement.core.DisbursementResourceImpl"
                );
			
        DisbursementService internationalDisbursement2Service = DisbursementServiceFactory
            .createDisbursementService("paymentgateway.disbursement.international.DisbursementServiceImpl"
            	, disbursementDisbursement2Service);		

        DisbursementResource internationalDisbursement2Resource = DisbursementResourceFactory
            .createDisbursementResource("paymentgateway.disbursement.international.DisbursementResourceImpl"
                , disbursementDisbursement2Resource, disbursementDisbursement2Service);
			
        DisbursementService internationaldisbursementvalidatorDisbursement2Service = DisbursementServiceFactory
            .createDisbursementService("paymentgateway.disbursement.internationaldisbursementvalidator.DisbursementServiceImpl"
            	, internationalDisbursement2Service);		

        DisbursementResource internationaldisbursementvalidatorDisbursement2Resource = DisbursementResourceFactory
            .createDisbursementResource("paymentgateway.disbursement.internationaldisbursementvalidator.DisbursementResourceImpl"
                , internationalDisbursement2Resource, internationalDisbursement2Service);
			
        DisbursementService exchangerateDisbursement2Service = DisbursementServiceFactory
            .createDisbursementService("paymentgateway.disbursement.exchangerate.DisbursementServiceImpl"
            	, disbursementDisbursement2Service);		

        DisbursementResource exchangerateDisbursement2Resource = DisbursementResourceFactory
            .createDisbursementResource("paymentgateway.disbursement.exchangerate.DisbursementResourceImpl"
                , disbursementDisbursement2Resource, disbursementDisbursement2Service);
			
        DisbursementService specialDisbursement2Service = DisbursementServiceFactory
            .createDisbursementService("paymentgateway.disbursement.special.DisbursementServiceImpl"
            	, disbursementDisbursement2Service);		

        DisbursementResource specialDisbursement2Resource = DisbursementResourceFactory
            .createDisbursementResource("paymentgateway.disbursement.special.DisbursementResourceImpl"
                , disbursementDisbursement2Resource, disbursementDisbursement2Service);
			
        DisbursementService domesticdisbursementvalidatorDisbursement2Service = DisbursementServiceFactory
            .createDisbursementService("paymentgateway.disbursement.domesticdisbursementvalidator.DisbursementServiceImpl"
            	, specialDisbursement2Service);		

        DisbursementResource domesticdisbursementvalidatorDisbursement2Resource = DisbursementResourceFactory
            .createDisbursementResource("paymentgateway.disbursement.domesticdisbursementvalidator.DisbursementResourceImpl"
                , specialDisbursement2Resource, specialDisbursement2Service);
			
        DisbursementService internationaldisbursementvalidatorDisbursement4Service = DisbursementServiceFactory
            .createDisbursementService("paymentgateway.disbursement.internationaldisbursementvalidator.DisbursementServiceImpl"
            	, domesticdisbursementvalidatorDisbursement2Service);		

        DisbursementResource internationaldisbursementvalidatorDisbursement4Resource = DisbursementResourceFactory
            .createDisbursementResource("paymentgateway.disbursement.internationaldisbursementvalidator.DisbursementResourceImpl"
                , domesticdisbursementvalidatorDisbursement2Resource, domesticdisbursementvalidatorDisbursement2Service);
			
        DisbursementService fixedcurrencyDisbursement2Service = DisbursementServiceFactory
            .createDisbursementService("paymentgateway.disbursement.fixedcurrency.DisbursementServiceImpl"
            	, disbursementDisbursement2Service);		

        DisbursementResource fixedcurrencyDisbursement2Resource = DisbursementResourceFactory
            .createDisbursementResource("paymentgateway.disbursement.fixedcurrency.DisbursementResourceImpl"
                , disbursementDisbursement2Resource, disbursementDisbursement2Service);
			
        DisbursementService agentDisbursement2Service = DisbursementServiceFactory
            .createDisbursementService("paymentgateway.disbursement.agent.DisbursementServiceImpl"
            	, disbursementDisbursement2Service);		

        DisbursementResource agentDisbursement2Resource = DisbursementResourceFactory
            .createDisbursementResource("paymentgateway.disbursement.agent.DisbursementResourceImpl"
                , disbursementDisbursement2Resource, disbursementDisbursement2Service);
			
        DisbursementService domesticdisbursementvalidatorDisbursement4Service = DisbursementServiceFactory
            .createDisbursementService("paymentgateway.disbursement.domesticdisbursementvalidator.DisbursementServiceImpl"
            	, agentDisbursement2Service);		

        DisbursementResource domesticdisbursementvalidatorDisbursement4Resource = DisbursementResourceFactory
            .createDisbursementResource("paymentgateway.disbursement.domesticdisbursementvalidator.DisbursementResourceImpl"
                , agentDisbursement2Resource, agentDisbursement2Service);
			
        DisbursementService agentdisbursementDisbursement2Service = DisbursementServiceFactory
            .createDisbursementService("paymentgateway.disbursement.agentdisbursement.DisbursementServiceImpl"
            	, disbursementDisbursement2Service);		

        DisbursementResource agentdisbursementDisbursement2Resource = DisbursementResourceFactory
            .createDisbursementResource("paymentgateway.disbursement.agentdisbursement.DisbursementResourceImpl"
                , disbursementDisbursement2Resource, disbursementDisbursement2Service);
			
        DisbursementService specifiedrecipientDisbursement2Service = DisbursementServiceFactory
            .createDisbursementService("paymentgateway.disbursement.specifiedrecipient.DisbursementServiceImpl"
            	, disbursementDisbursement2Service);		

        DisbursementResource specifiedrecipientDisbursement2Resource = DisbursementResourceFactory
            .createDisbursementResource("paymentgateway.disbursement.specifiedrecipient.DisbursementResourceImpl"
                , disbursementDisbursement2Resource, disbursementDisbursement2Service);
			

		System.out.println("specifiedrecipientDisbursement2Resource endpoints binding");
		Router.route(specifiedrecipientDisbursement2Resource);
		
		System.out.println("specifiedrecipientDisbursement2Service endpoints binding");
		Router.route(specifiedrecipientDisbursement2Service);
		
		System.out.println("agentdisbursementDisbursement2Resource endpoints binding");
		Router.route(agentdisbursementDisbursement2Resource);
		
		System.out.println("agentdisbursementDisbursement2Service endpoints binding");
		Router.route(agentdisbursementDisbursement2Service);
		
		System.out.println("domesticdisbursementvalidatorDisbursement4Resource endpoints binding");
		Router.route(domesticdisbursementvalidatorDisbursement4Resource);
		
		System.out.println("domesticdisbursementvalidatorDisbursement4Service endpoints binding");
		Router.route(domesticdisbursementvalidatorDisbursement4Service);
		
		System.out.println("agentDisbursement2Resource endpoints binding");
		Router.route(agentDisbursement2Resource);
		
		System.out.println("agentDisbursement2Service endpoints binding");
		Router.route(agentDisbursement2Service);
		
		System.out.println("fixedcurrencyDisbursement2Resource endpoints binding");
		Router.route(fixedcurrencyDisbursement2Resource);
		
		System.out.println("fixedcurrencyDisbursement2Service endpoints binding");
		Router.route(fixedcurrencyDisbursement2Service);
		
		System.out.println("internationaldisbursementvalidatorDisbursement4Resource endpoints binding");
		Router.route(internationaldisbursementvalidatorDisbursement4Resource);
		
		System.out.println("internationaldisbursementvalidatorDisbursement4Service endpoints binding");
		Router.route(internationaldisbursementvalidatorDisbursement4Service);
		
		System.out.println("domesticdisbursementvalidatorDisbursement2Resource endpoints binding");
		Router.route(domesticdisbursementvalidatorDisbursement2Resource);
		
		System.out.println("domesticdisbursementvalidatorDisbursement2Service endpoints binding");
		Router.route(domesticdisbursementvalidatorDisbursement2Service);
		
		System.out.println("specialDisbursement2Resource endpoints binding");
		Router.route(specialDisbursement2Resource);
		
		System.out.println("specialDisbursement2Service endpoints binding");
		Router.route(specialDisbursement2Service);
		
		System.out.println("exchangerateDisbursement2Resource endpoints binding");
		Router.route(exchangerateDisbursement2Resource);
		
		System.out.println("exchangerateDisbursement2Service endpoints binding");
		Router.route(exchangerateDisbursement2Service);
		
		System.out.println("internationaldisbursementvalidatorDisbursement2Resource endpoints binding");
		Router.route(internationaldisbursementvalidatorDisbursement2Resource);
		
		System.out.println("internationaldisbursementvalidatorDisbursement2Service endpoints binding");
		Router.route(internationaldisbursementvalidatorDisbursement2Service);
		
		System.out.println("internationalDisbursement2Resource endpoints binding");
		Router.route(internationalDisbursement2Resource);
		
		System.out.println("internationalDisbursement2Service endpoints binding");
		Router.route(internationalDisbursement2Service);
		
		System.out.println("disbursementDisbursement2Resource endpoints binding");
		Router.route(disbursementDisbursement2Resource);
		
		System.out.println("disbursementDisbursement2Service endpoints binding");
		Router.route(disbursementDisbursement2Service);
		
		System.out.println("authResource endpoints binding");
		Router.route(userPasswordedResource);
		Router.route(roleResource);
		Router.route(userResource);
	}

	private static Map<String, Object> mappingFeatureModel() {
		Map<String, Object> featureModelMappings = new HashMap<>();

		featureModelMappings.put(
            paymentgateway.disbursement.core.DisbursementComponent.class.getName(),
			new HashMap<String, String[]>() {{
				put("components", new String[] {
					paymentgateway.disbursement.core.DisbursementComponent.class.getName()
				});
				put("deltas", new String[] {
					paymentgateway.disbursement.international.InternationalImpl.class.getName(),
					paymentgateway.disbursement.special.SpecialImpl.class.getName(),
					paymentgateway.disbursement.agent.AgentImpl.class.getName(),
					paymentgateway.disbursement.specifiedrecipient.SpecifiedRecipientImpl.class.getName()
				});
			}}
        );

		return featureModelMappings;
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

	public static void setCors() {
    	Properties properties = new Properties();
        String propertyValue = "";
        
        try (FileInputStream fileInput = new FileInputStream("cors.properties")) {
            properties.load(fileInput);
            propertyValue = properties.getProperty("allowedMethod");
            VMJCors.setAllowedMethod(propertyValue);
            
            propertyValue = properties.getProperty("allowedOrigin");
            VMJCors.setAllowedOrigin(propertyValue);
            
        } catch (IOException e) {
			VMJCors.setAllowedMethod("GET, POST, PUT, PATCH, DELETE");
			VMJCors.setAllowedOrigin("*");
			System.out.println("Buat file cors.properties terlebih dahulu pada src-gen/(namaProduk) dengan contoh sebagai berikut:");
			System.out.println("allowedMethod = GET, POST");
			System.out.println("allowedOrigin = http://example.com");
        }
    }


}