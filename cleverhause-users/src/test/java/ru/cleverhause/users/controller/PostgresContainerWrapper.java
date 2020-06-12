package ru.cleverhause.users.controller;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

@Slf4j
public class PostgresContainerWrapper extends PostgreSQLContainer<PostgresContainerWrapper> {
    private static final String POSTGRES_IMAGE_NAME = "postgres:11";

    public PostgresContainerWrapper() {
        super(POSTGRES_IMAGE_NAME);
        this.withLogConsumer(new Slf4jLogConsumer(log));
//        this.withEnv("POSTGRES_URL", this.getJdbcUrl());
//        this.withEnv("POSTGRES_USER_NAME", this.getUsername());
//        this.withEnv("POSTGRES_PASSWORD", this.getPassword());
    }
}
