package com.yld.learningspringboot.config;

import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

// Configuration file to use Resteasy api in the application
@Configuration
@ApplicationPath("/")
public class ResteasyConfig extends Application {
}
