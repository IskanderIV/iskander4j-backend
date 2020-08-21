package ru.cleverhause.users.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.utility.MountableFile;

import java.util.Map;

@Slf4j
public class PostgresContainerExtension implements BeforeAllCallback, AfterAllCallback {
    public static PostgresContainerWrapper postgresContainer;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (postgresContainer == null) {
            postgresContainer = new PostgresContainerWrapper();
        }
        postgresContainer.start();
//        postgresContainer.withEnv(Map.of("POSTGRES_DB", postgresContainer.getD));
//        postgresContainer.withEnv("POSTGRES_URL", postgresContainer.getJdbcUrl());
//        postgresContainer.withInitScript("script/initUsersDb.sql");
//        postgresContainer.waitingFor(new HostPortWaitStrategy());
        postgresContainer.copyFileToContainer(
                MountableFile.forClasspathResource("/script/init-users-db.sh"),
                "/docker-entrypoint-initdb.d/init-users-db.sh");
        postgresContainer.copyFileToContainer(
                MountableFile.forClasspathResource("/script/initUsersDb.sql"),
                "/var/lib/postgresql/init/initUsersDb.sql");
        Container.ExecResult lsResult = postgresContainer.execInContainer("chmod", "u+x", "/docker-entrypoint-initdb.d/init-users-db.sh");
        log.info("Result2: {}", lsResult);
//        lsResult = postgresContainer.execInContainer("sh", "/docker-entrypoint-initdb.d/init-users-db.sh");
//        log.info("Result3: {}", lsResult);

//        postgresContainer.withInitScript("/script/init-users-db.sh");
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {

    }
}
