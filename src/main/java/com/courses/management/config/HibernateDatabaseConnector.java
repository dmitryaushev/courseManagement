package com.courses.management.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Objects;

public class HibernateDatabaseConnector {

    private static final Logger LOG = LogManager.getLogger(HibernateDatabaseConnector.class);
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    public static synchronized void init() {
        try {
            registry = new StandardServiceRegistryBuilder().configure().build();
            MetadataSources sources = new MetadataSources(registry);
            Metadata metadata = sources.getMetadataBuilder().build();
            sessionFactory = metadata.getSessionFactoryBuilder().build();
        } catch (Exception e) {
            LOG.error("Init hibernate", e);
            throw new RuntimeException("Init error", e);
        }
    }

    public static synchronized void destroy() {
        if (Objects.nonNull(sessionFactory)) {
            sessionFactory.close();
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
