package ru.cleverhause.users.controller;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.utility.MountableFile;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class PostgresContainerWrapper extends PostgreSQLContainer<PostgresContainerWrapper> {
    private static final String POSTGRES_IMAGE_NAME = "postgres:11";

    public PostgresContainerWrapper() {
        super(POSTGRES_IMAGE_NAME);
//        this.waitingFor(new HostPortWaitStrategy());
        this.withLogConsumer(new Slf4jLogConsumer(log))
//        .withExposedPorts(5432);
        .withUsername("clever_admin")
        .withPassword("WindowsVista123")
        .withDatabaseName("cleverhause_users_db");
//        .withInitScript("script/initUsersDb.sql");
//        .withInitScript("script/initUsersDb.sql");
//        this.withEnv("POSTGRES_URL", this.getJdbcUrl());
//        this.withEnv("POSTGRES_USER", this.getUsername());
//        this.withEnv("POSTGRES_DB", this.getDatabaseName());
//        this.withEnv("POSTGRES_USER", this.getUsername());
//        this.withEnv("POSTGRES_PASSWORD", this.getPassword());
        log.info("!!! BoundPortNumbers: {}", this.getBoundPortNumbers());
        log.info("!!! ExposedPorts: {}", this.getExposedPorts());
//        this.withEnv("POSTGRES_PASSWORD", this.getPassword());
    }
}
