package ru.cleverhause.devices.testcontainer;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import java.util.List;

@Slf4j
public class MongoContainerWrapper extends MongoDBContainer {
    private static final String MONGO_IMAGE_NAME = "mongo:4.2.10";

    public MongoContainerWrapper() {
        super(MONGO_IMAGE_NAME);
        this.withLogConsumer(new Slf4jLogConsumer(log));
        this.setPortBindings(List.of("27017:27017"));
    }
}
