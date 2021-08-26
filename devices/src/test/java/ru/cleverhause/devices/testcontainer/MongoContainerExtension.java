package ru.cleverhause.devices.testcontainer;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class MongoContainerExtension implements BeforeAllCallback, AfterAllCallback {
    private static MongoContainerWrapper mongoContainerWrapper;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (mongoContainerWrapper == null) {
            mongoContainerWrapper = new MongoContainerWrapper();
        }
        mongoContainerWrapper.start();
        System.setProperty("spring.data.mongodb.host", mongoContainerWrapper.getHost());
        System.setProperty("spring.data.mongodb.port", mongoContainerWrapper.getFirstMappedPort().toString());
        System.setProperty("spring.data.mongodb.database", "cleverhause_devices_db");
        System.setProperty("spring.data.mongodb.username", "cleverhause_devices_db");
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {

    }
}
