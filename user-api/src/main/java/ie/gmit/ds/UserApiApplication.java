package ie.gmit.ds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ie.gmit.ds.health.UserHealthCheck;
import ie.gmit.ds.resources.UserApiResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * 
 * Adopted from:
 * 	https://howtodoinjava.com/dropwizard/tutorial-and-hello-world-example/
 *
 */

public class UserApiApplication extends Application<UserApiConfig>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserApiApplication.class);
	
	@Override
    public void initialize(Bootstrap<UserApiConfig> b) {
    }
 
	
	@Override
	public void run(UserApiConfig configuration, Environment environment) throws Exception {
		LOGGER.info("Registering REST resources");
		
		final UserApiResource resource = new UserApiResource(environment.getValidator(), configuration);

        final UserHealthCheck healthCheck = new UserHealthCheck();
        environment.healthChecks().register("user", healthCheck);
       
        environment.jersey().register(resource);
	}
	
	public static void main(String[] args) throws Exception{
		new UserApiApplication().run(args);
	}
}
