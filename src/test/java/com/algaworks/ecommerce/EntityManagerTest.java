package com.algaworks.ecommerce;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityManagerTest {

    protected static EntityManagerFactory entityManagerFactory;
    protected static ILoggerFactory loggerFactory;
    protected EntityManager entityManager;
    protected Logger logger;

    @BeforeAll
    static void setUpBeforClass() {
        loggerFactory = LoggerFactory.getILoggerFactory();
        entityManagerFactory = Persistence.createEntityManagerFactory("Ecommerce-PU");
    }

    @AfterAll
    static void tearDownAfterClass() {
        entityManagerFactory.close();
    }

    @BeforeEach
    void setUp() {
        logger = loggerFactory.getLogger(this.getClass().getName());
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterEach
    void tearDown() {
        entityManager.close();
    }

}
