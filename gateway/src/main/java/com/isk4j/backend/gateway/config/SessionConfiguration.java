package com.isk4j.backend.gateway.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.session.FlushMode;
import org.springframework.session.MapSession;
import org.springframework.session.ReactiveMapSessionRepository;
import org.springframework.session.ReactiveSessionRepository;
import org.springframework.session.SaveMode;
import org.springframework.session.config.SessionRepositoryCustomizer;
import org.springframework.session.config.annotation.web.server.EnableSpringWebSession;
import org.springframework.session.hazelcast.HazelcastIndexedSessionRepository;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Slf4j
@Configuration
@EnableConfigurationProperties(SessionConfiguration.HazelcastCacheProperties.class)
@EnableSpringWebSession
public class SessionConfiguration {

    private static final String SESSIONS_MAP_NAME = "ufr_azon_minio_sessions";

    @Bean
    public ReactiveSessionRepository sessionRepository(HazelcastInstance instance) {
        return new ReactiveMapSessionRepository(instance.getMap(SESSIONS_MAP_NAME));
    }

    @Bean
    public SessionRepositoryCustomizer<HazelcastIndexedSessionRepository> customize() {
        return (sessionRepository) -> {
            sessionRepository.setFlushMode(FlushMode.IMMEDIATE);
            sessionRepository.setSaveMode(SaveMode.ALWAYS);
            sessionRepository.setSessionMapName(SESSIONS_MAP_NAME);
            sessionRepository.setDefaultMaxInactiveInterval(900);
        };
    }

    @Bean
    @Profile("!local & !test")
    public HazelcastInstance getHazelcastInstance(HazelcastCacheProperties properties) {

        log.info("Connecting to Hazelcast hosts: {}, login: {}", properties.getHosts(), properties.getLogin());

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getGroupConfig()
                .setName(properties.getLogin())
                .setPassword(properties.getPassword());
        clientConfig.getNetworkConfig()
                .setAddresses(properties.getHosts())
                .setConnectionAttemptLimit(10);
        clientConfig.getUserCodeDeploymentConfig().setEnabled(true).addClass(Session.class)
                .addClass(MapSession.class);
        return HazelcastClient.newHazelcastClient(clientConfig);
    }

    @Bean
    @Profile("local")
    public HazelcastInstance getLocalHazelcastInstance() {
        Config clientConfig = new Config();
        JoinConfig join = clientConfig.getNetworkConfig().getJoin();
        join.getTcpIpConfig().setEnabled(false);
        join.getMulticastConfig().setEnabled(false);
        return Hazelcast.newHazelcastInstance(clientConfig);
    }

    @Data
    @Validated
    @ConfigurationProperties("hazelcast")
    static class HazelcastCacheProperties {
        @NotBlank
        private String login;
        @NotBlank
        private String password;
        @NotEmpty
        private List<String> hosts;
    }
}