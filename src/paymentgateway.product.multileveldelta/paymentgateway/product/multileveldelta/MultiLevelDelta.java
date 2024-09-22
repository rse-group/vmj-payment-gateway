package paymentgateway.product.multileveldelta;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import vmj.routing.route.VMJServer;
import com.google.gson.Gson;
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

public class MultiLevelDelta {
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
        configuration.addAnnotatedClass(paymentgateway.disbursement.international.InternationalImpl.class);
        configuration.addAnnotatedClass(paymentgateway.disbursement.special.SpecialImpl.class);
        configuration.addAnnotatedClass(paymentgateway.disbursement.agent.AgentImpl.class);
        String[] disbursementDeltas = { 
            paymentgateway.disbursement.international.InternationalImpl.class.getSimpleName(),
            paymentgateway.disbursement.special.SpecialImpl.class.getSimpleName(),
            paymentgateway.disbursement.agent.AgentImpl.class.getSimpleName()
        };
        Map<String, String[]> foreignKeyRules = new HashMap<>();
        foreignKeyRules.put(
            paymentgateway.disbursement.core.DisbursementImpl.class.getSimpleName(),
            disbursementDeltas
        );
        Gson gson = new Gson();
        String foreignKeyRulesMap = gson.toJson(foreignKeyRules);
        configuration.setProperty("custom.foreign.key.rules", foreignKeyRulesMap);
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
        
        DisbursementService disbursementDisbursementService = DisbursementServiceFactory
                .createDisbursementService("paymentgateway.disbursement.core.DisbursementServiceImpl"
                );
        
        DisbursementResource disbursementDisbursementResource = DisbursementResourceFactory
            .createDisbursementResource("paymentgateway.disbursement.core.DisbursementResourceImpl"
            );
        
        DisbursementResource internationalDisbursementResource = DisbursementResourceFactory
            .createDisbursementResource("paymentgateway.disbursement.international.DisbursementResourceImpl"
            ,
            disbursementDisbursementResource, disbursementDisbursementService);
        
        DisbursementService internationalDisbursementService = DisbursementServiceFactory
                .createDisbursementService("paymentgateway.disbursement.international.DisbursementServiceImpl"
                ,
                disbursementDisbursementService);
        
        DisbursementResource internationaldisbursementvalidatorDisbursementResource1 = DisbursementResourceFactory
            .createDisbursementResource("paymentgateway.disbursement.internationaldisbursementvalidator.DisbursementResourceImpl"
            ,
            internationalDisbursementResource, internationalDisbursementService);
        
        DisbursementService internationaldisbursementvalidatorDisbursementService1 = DisbursementServiceFactory
            .createDisbursementService("paymentgateway.disbursement.internationaldisbursementvalidator.DisbursementServiceImpl"
            ,
            internationalDisbursementService);
        
        DisbursementResource internationaldisbursementDisbursementResource = DisbursementResourceFactory
            .createDisbursementResource("paymentgateway.disbursement.internationaldisbursement.DisbursementResourceImpl"
            ,
            internationaldisbursementvalidatorDisbursementResource1, internationaldisbursementvalidatorDisbursementService1);
        
        DisbursementService internationaldisbursementDisbursementService = DisbursementServiceFactory
                .createDisbursementService("paymentgateway.disbursement.internationaldisbursement.DisbursementServiceImpl"
                ,
                internationaldisbursementvalidatorDisbursementService1);
        
        DisbursementResource specialDisbursementResource = DisbursementResourceFactory
            .createDisbursementResource("paymentgateway.disbursement.special.DisbursementResourceImpl"
            ,
            disbursementDisbursementResource, disbursementDisbursementService);
        
        DisbursementService specialDisbursementService = DisbursementServiceFactory
                .createDisbursementService("paymentgateway.disbursement.special.DisbursementServiceImpl"
                ,
                disbursementDisbursementService);
        
        DisbursementResource domesticdisbursementvalidatorDisbursementResource1 = DisbursementResourceFactory
                .createDisbursementResource("paymentgateway.disbursement.domesticdisbursementvalidator.DisbursementResourceImpl"
                ,
                specialDisbursementResource, specialDisbursementService);
        
        DisbursementService domesticdisbursementvalidatorDisbursementService1 = DisbursementServiceFactory
                .createDisbursementService("paymentgateway.disbursement.domesticdisbursementvalidator.DisbursementServiceImpl"
                ,
                specialDisbursementService);
        
        DisbursementResource internationaldisbursementvalidatorDisbursementResource2 = DisbursementResourceFactory
            .createDisbursementResource("paymentgateway.disbursement.internationaldisbursementvalidator.DisbursementResourceImpl"
            ,
            domesticdisbursementvalidatorDisbursementResource1, domesticdisbursementvalidatorDisbursementService1);
        
        DisbursementService internationaldisbursementvalidatorDisbursementService2 = DisbursementServiceFactory
            .createDisbursementService("paymentgateway.disbursement.internationaldisbursementvalidator.DisbursementServiceImpl"
            ,
            domesticdisbursementvalidatorDisbursementService1);
        
        DisbursementResource specialdisbursementDisbursementResource = DisbursementResourceFactory
            .createDisbursementResource("paymentgateway.disbursement.specialdisbursement.DisbursementResourceImpl"
            ,
            internationaldisbursementvalidatorDisbursementResource2, internationaldisbursementvalidatorDisbursementService2);

        
        DisbursementService specialdisbursementDisbursementService = DisbursementServiceFactory
                .createDisbursementService("paymentgateway.disbursement.specialdisbursement.DisbursementServiceImpl"
                ,
                internationaldisbursementvalidatorDisbursementService2);

        DisbursementResource agentDisbursementResource = DisbursementResourceFactory
            .createDisbursementResource("paymentgateway.disbursement.agent.DisbursementResourceImpl"
            ,
            disbursementDisbursementResource, disbursementDisbursementService);
        
        DisbursementService agentDisbursementService = DisbursementServiceFactory
                .createDisbursementService("paymentgateway.disbursement.agent.DisbursementServiceImpl"
                ,
                disbursementDisbursementService);
        
        DisbursementResource domesticdisbursementvalidatorDisbursementResource2 = DisbursementResourceFactory
            .createDisbursementResource("paymentgateway.disbursement.domesticdisbursementvalidator.DisbursementResourceImpl"
            ,
            agentDisbursementResource, agentDisbursementService);
        
        DisbursementService domesticdisbursementvalidatorDisbursementService2 = DisbursementServiceFactory
                .createDisbursementService("paymentgateway.disbursement.domesticdisbursementvalidator.DisbursementServiceImpl"
                ,
                agentDisbursementService);
        
        DisbursementResource agentdisbursementDisbursementResource = DisbursementResourceFactory
            .createDisbursementResource("paymentgateway.disbursement.agentdisbursement.DisbursementResourceImpl"
            ,
            domesticdisbursementvalidatorDisbursementResource2, domesticdisbursementvalidatorDisbursementService2);
        
        DisbursementService agentdisbursementDisbursementService = DisbursementServiceFactory
                .createDisbursementService("paymentgateway.disbursement.agentdisbursement.DisbursementServiceImpl"
                ,
                domesticdisbursementvalidatorDisbursementService2);
        
        System.out.println("agentdisbursementDisbursementResource endpoints binding");
        Router.route(agentdisbursementDisbursementResource);
        System.out.println("domesticdisbursementvalidatorDisbursementResource2 endpoints binding");
        Router.route(domesticdisbursementvalidatorDisbursementResource2);
        System.out.println("agentDisbursementResource endpoints binding");
        Router.route(agentDisbursementResource);
        System.out.println("specialdisbursementDisbursementResource endpoints binding");
        Router.route(specialdisbursementDisbursementResource);
        System.out.println("internationaldisbursementvalidatorDisbursementResource2 endpoints binding");
        Router.route(internationaldisbursementvalidatorDisbursementResource2);
        System.out.println("domesticdisbursementvalidatorDisbursementResource1 endpoints binding");
        Router.route(domesticdisbursementvalidatorDisbursementResource1);
        System.out.println("specialDisbursementResource endpoints binding");
        Router.route(specialDisbursementResource);
        System.out.println("internationaldisbursementDisbursementResource endpoints binding");
        Router.route(internationaldisbursementDisbursementResource);
        System.out.println("internationaldisbursementvalidatorDisbursementResource1 endpoints binding");
        Router.route(internationaldisbursementvalidatorDisbursementResource1);
        System.out.println("internationalDisbursementResource endpoints binding");
        Router.route(internationalDisbursementResource);
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
