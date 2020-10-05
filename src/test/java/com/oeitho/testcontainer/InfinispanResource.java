package com.oeitho.testcontainer;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class InfinispanResource implements QuarkusTestResourceLifecycleManager {
    
    private static GenericContainer INFINISPAN;
    private static final Integer INFINISPAN_PORT = 11222;

    @Override
    public Map<String, String> start() {
        INFINISPAN = new GenericContainer<>("infinispan/server:11.0.3.Final")
            .withEnv("USER", "admin")
            .withEnv("PASS", "admin")
            .waitingFor(new LogMessageWaitStrategy().withRegEx(".*Infinispan Server.*started in.*\\s"))
            .withStartupTimeout(Duration.ofMillis(20000))
            .withExposedPorts(INFINISPAN_PORT, INFINISPAN_PORT);

        INFINISPAN.start();
        final String hosts = INFINISPAN.getContainerIpAddress() + ":" + INFINISPAN.getMappedPort(INFINISPAN_PORT);

        return Collections.singletonMap("quarkus.infinispan-client.server-list", hosts);
    }

    @Override
    public void stop() {
        INFINISPAN.stop();
    }

}
