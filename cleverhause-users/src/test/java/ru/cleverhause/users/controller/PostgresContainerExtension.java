package ru.cleverhause.users.controller;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.utility.MountableFile;

public class PostgresContainerExtension implements BeforeAllCallback, AfterAllCallback {
    private static PostgresContainerWrapper postgresContainer;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (postgresContainer == null) {
            postgresContainer = new PostgresContainerWrapper();
        }
        postgresContainer.start();
        postgresContainer.copyFileToContainer(
                MountableFile.forClasspathResource("/database.sql"),
                "/docker-entrypoint-initdb.d/database.sql");
        postgresContainer.copyFileToContainer(
                MountableFile.forClasspathResource("/insert.sql"),
                "/docker-entrypoint-initdb.d/init.sql");
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {

    }
}
