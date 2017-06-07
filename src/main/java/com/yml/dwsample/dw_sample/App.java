package com.yml.dwsample.dw_sample;



import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yml.dwsample.dw_sample.auth.AppAuthorizer;
import com.yml.dwsample.dw_sample.auth.User;
import com.yml.dwsample.dw_sample.healthcheck.AppHealthCheck;
import com.yml.dwsample.dw_sample.healthcheck.HealthCheckController;
import com.yml.dwsample.dw_sample.resources.ClientResource;
import com.yml.dwsample.dw_sample.resources.ContactResource;

import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import javax.ws.rs.client.Client;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.client.JerseyClientBuilder;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
/**
 * Hello world!
 *
 */
public class App extends Application<PhonebookConfiguration> {
	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) throws Exception {
		new App().run(args);
	}

	@Override
	public void initialize(Bootstrap<PhonebookConfiguration> b) {
	}

	@Override
	public void run(PhonebookConfiguration c, Environment e) throws Exception {
		LOGGER.info("Method App#run() called");
		
		// Create a DBI factory and build a JDBI instance
		final DBIFactory factory = new DBIFactory();
		final DBI jdbi = factory.build(e, c.getDataSourceFactory(), "mysql");
		// Add the resource to the environment
		e.jersey().register(new ContactResource(jdbi, e.getValidator()));
		
		final Client client = new JerseyClientBuilder(e).build("DemoRESTClient");
        e.jersey().register(new ClientResource(client));
 
        // Application health check
        e.healthChecks().register("APIHealthCheck", new AppHealthCheck(client));
 
        // Run multiple health checks
        e.jersey().register(new HealthCheckController(e.healthChecks()));
 
        //****** Dropwizard security - custom classes ***********/
        e.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                                .setAuthenticator(new PhonebookAuthenticator())
                                .setAuthorizer(new AppAuthorizer())
                                .setRealm("BASIC-AUTH-REALM")
                                .buildAuthFilter()));
        e.jersey().register(RolesAllowedDynamicFeature.class);
        e.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
		
		
		
		
	}
}
