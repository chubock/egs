package com.chubock.assignment.egs.integration;

import org.junit.ClassRule;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class IntegrationTestContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @ClassRule
    public static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:12.6")
            .withDatabaseName("egs")
            .withUsername("egs")
            .withPassword("egs");

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        postgres.start();

        TestPropertyValues.of("spring.datasource.url=" + postgres.getJdbcUrl())
                .applyTo(applicationContext.getEnvironment());

    }
}
