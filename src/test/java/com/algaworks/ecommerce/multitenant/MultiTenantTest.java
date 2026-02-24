package com.algaworks.ecommerce.multitenant;

import com.algaworks.ecommerce.EntityManagerFactoryTest;
import com.algaworks.ecommerce.hibernate.EcmCurrentTenantIdentifierResolver;
import com.algaworks.ecommerce.model.Produto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class MultiTenantTest extends EntityManagerFactoryTest {

    @Test
    void usarAbordagemPorMaquina() {
        EcmCurrentTenantIdentifierResolver.setTenantIdentifier("algaworks_ecommerce");
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        Produto produto1 = entityManager1.find(Produto.class, 1);
        Assertions.assertEquals("Kindle", produto1.getNome());
        entityManager1.close();

        EcmCurrentTenantIdentifierResolver.setTenantIdentifier("loja_ecommerce");
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();
        Produto produto2 = entityManager2.find(Produto.class, 1);
        Assertions.assertEquals("Kindle Paperwhite", produto2.getNome());
        entityManager2.close();
    }

    @Test
    void usarAbordagemPorSchema() {
        EcmCurrentTenantIdentifierResolver.setTenantIdentifier("algaworks_ecommerce");
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();

        Produto produto1 = entityManager1.find(Produto.class, 1);

        List<Produto> lista1 = entityManager1
                .createQuery("select p from Produto  p", Produto.class).getResultList();

        entityManager1.close();
        Assertions.assertEquals("Kindle", produto1.getNome());
        System.out.println(lista1.size());

        EcmCurrentTenantIdentifierResolver.setTenantIdentifier("loja_ecommerce");
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();

        Produto produto2 = entityManager2.find(Produto.class, 1);

        List<Produto> lista2 = entityManager2
                .createQuery("select p from Produto p", Produto.class).getResultList();

        entityManager2.close();
        Assertions.assertEquals("Kindle Paperwhite", produto2.getNome());
        System.out.println(lista2.size());
    }
}
