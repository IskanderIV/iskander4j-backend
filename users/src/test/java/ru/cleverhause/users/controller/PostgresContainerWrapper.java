package ru.cleverhause.users.controller;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

@Slf4j
public class PostgresContainerWrapper extends PostgreSQLContainer<PostgresContainerWrapper> {
    private static final String POSTGRES_IMAGE_NAME = "postgres:11";

    public PostgresContainerWrapper() {
        super(POSTGRES_IMAGE_NAME);
//        this.waitingFor(new HostPortWaitStrategy());
        this.withLogConsumer(new Slf4jLogConsumer(log))
        .withUsername("clever_admin")
        .withPassword("WindowsVista123")
        .withDatabaseName("cleverhause_users_db");
        log.info("!!! BoundPortNumbers: {}", this.getBoundPortNumbers());
        log.info("!!! ExposedPorts: {}", this.getExposedPorts());
    }
}
