package com.yld.learningspringboot.clientproxy;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Configures a RESTful client for integration testing of our web resource
@Configuration
public class ClientProxyConfig {

    @Value("${users.api.url.v1}")
    private String usersEndpointUrl;

    // Bean annotation helps in autowiring the UserResourceV1 proxy interface where ever it is required
    // Note: It is actually used in integration tests present in LearningSpringBootApplicationTests class
    @Bean
    public UserResourceV1 getUserResourceV1() {
        // create ResteasyClient class instance using its builder
        ResteasyClient client = new ResteasyClientBuilder().build();

        // ResteasyWebTarget class helps to connect to our web resource server
        ResteasyWebTarget target = client.target(usersEndpointUrl);

        // Connect target with the ResteasyClient proxy(mirror of our web resource)
        UserResourceV1 proxy = target.proxy(UserResourceV1.class);
        return proxy;
    }
}
