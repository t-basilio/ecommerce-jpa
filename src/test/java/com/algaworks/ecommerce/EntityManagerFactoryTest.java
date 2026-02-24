package com.algaworks.ecommerce;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.util.Arrays;

public class EntityManagerFactoryTest {

    protected static EntityManagerFactory entityManagerFactory;

    @BeforeAll
    static void setUpBeforAll() {
        entityManagerFactory = Persistence.createEntityManagerFactory("Ecommerce-PU");
    }

    @AfterAll
    static void tearDownAfterAll() {
        entityManagerFactory.close();
    }

    public static void log(Object obj, Object... args) {
        System.out.printf("[LOG %s] %s, %s%n", System.currentTimeMillis(), obj, Arrays.toString(args));
    }

    public static void esperar(int segundos) {
        try {
            Thread.sleep(segundos * 1000L);
        } catch (InterruptedException ignored) {}
    }

}
