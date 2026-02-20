package com.algaworks.ecommerce.cache;

import com.algaworks.ecommerce.model.Pedido;
import jakarta.persistence.Cache;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.CacheStoreMode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

class CacheTest {
    protected static EntityManagerFactory entityManagerFactory;

    @BeforeEach
    void setUpBeforeEach() {
       entityManagerFactory = Persistence.createEntityManagerFactory("Ecommerce-PU");
    }

    @AfterEach
    void tearDownAfterEach() {
        entityManagerFactory.close();
    }

    private static void esperar(int segundos) {
       try {
           Thread.sleep(segundos * 1000);
       } catch (InterruptedException ignored){}
    }

    private static void log(Object object) {
        System.out.println("[LOG " + System.currentTimeMillis() + "] " + object);
    }

    @Test
    void ehCache() {
        Cache cache = entityManagerFactory.getCache();

        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();

        log("Buscando e incluindo no cache...");
        entityManager1.createQuery("select p from Pedido p", Pedido.class).getResultList();
        log("---");

        esperar(1);
        Assertions.assertTrue(cache.contains(Pedido.class, 3));
        entityManager2.find(Pedido.class, 3);

        esperar(3);
        Assertions.assertFalse(cache.contains(Pedido.class, 3));

        entityManager1.close();
        entityManager2.close();
    }

    @Test
    void controlarCacheDinamicamente() {
        //jakarta.persistence.cache.retrieveMode CacheRetrieveMode
        //jakarta.persistence.cache.storeMode CacheStoreMode

        Cache cache = entityManagerFactory.getCache();

        System.out.println("Buscando todos os pedidos..........................");
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        entityManager1.setProperty("jakarta.persistence.cache.storeMode", CacheStoreMode.BYPASS);
        entityManager1.createQuery("select p from Pedido p", Pedido.class)
                .setHint("jakarta.persistence.cache.storeMode", CacheStoreMode.USE)
                .getResultList();

        System.out.println("Buscando o pedido de ID igual a 3..................");
        var propriedades = new HashMap<String, Object>();
        //propriedades.put("jakarta.persistence.cache.storeMode", CacheStoreMode.BYPASS)
        //propriedades.put("jakarta.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS)
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();
        entityManager2.find(Pedido.class, 3, propriedades);

        System.out.println("Buscando todos os pedidos (de novo)................");
        EntityManager entityManager3 = entityManagerFactory.createEntityManager();
        entityManager3.createQuery("select p from Pedido p", Pedido.class)
                //.setHint("jakarta.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS)
                .getResultList();

        entityManager1.close();
        entityManager2.close();
        entityManager3.close();
    }

    @Test
    void analisarOpcoesCache() {
        Cache cache = entityManagerFactory.getCache();

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        System.out.println("Buscando a partir da instância 1");
        entityManager.createQuery("select p from Pedido p", Pedido.class).getResultList();

        Assertions.assertTrue(cache.contains(Pedido.class, 1));

        entityManager.close();
    }

    @Test
    void verificarSeEstaNoCache() {
        Cache cache = entityManagerFactory.getCache();

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        System.out.println("Buscando a partir da instância 1");
        entityManager.createQuery("select p from Pedido p", Pedido.class).getResultList();


        Assertions.assertTrue(cache.contains(Pedido.class, 1));
        Assertions.assertTrue(cache.contains(Pedido.class, 3));

        entityManager.close();
    }

    @Test
    void removerDoCache() {
        Cache cache = entityManagerFactory.getCache();

        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();

        System.out.println("Buscando a partir da instância 1");
        entityManager1.createQuery("select p from Pedido p", Pedido.class).getResultList();

        System.out.println("Removendo pedido 1 do cache");
        cache.evict(Pedido.class, 1);
        // cache.evict(Pedido.class) remove todos os pedidos
        // cache.evictAll() remove todas as entidades(tudo)
        System.out.println("Buscando a partir da instância 2");
        entityManager2.find(Pedido.class, 1);
        entityManager2.find(Pedido.class, 3);

        entityManager1.close();
        entityManager2.close();
    }

    @Test
    void adicionarPedidosNoCache() {
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();

        System.out.println("Buscando a partir da instância 1");
        entityManager1.createQuery("select p from Pedido p", Pedido.class).getResultList();

        System.out.println("Buscando a partir da instância 2");
        entityManager2.find(Pedido.class, 1);

        entityManager1.close();
        entityManager2.close();
    }

    @Test
    void buscarDoCache() {
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();

        System.out.println("Buscando a partir da instância 1");
        entityManager1.find(Pedido.class, 1);

        System.out.println("Buscando a partir da instância 2");
        entityManager2.find(Pedido.class, 1);

        entityManager1.close();
        entityManager2.close();
    }
}
