package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Categoria;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class PaginacaoJPQLTest extends EntityManagerTest {

    @Test
    void paginarResultados() {
        String jpql = "select c from Categoria c order by c.nome";

        TypedQuery<Categoria> typedQuery = entityManager.createQuery(jpql, Categoria.class);
        int PAGINA = 4;
        int MAX_RESULT = 2;
        int FIRST_RESULT = MAX_RESULT * (PAGINA - 1);
        typedQuery.setFirstResult(FIRST_RESULT);
        typedQuery.setMaxResults(MAX_RESULT);

        List<Categoria> lista = typedQuery.getResultList();

        lista.forEach(c -> System.out.println(c.getId() + " - " + c.getNome()));

        Assertions.assertFalse(lista.isEmpty());
    }
}
