package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.StatusPedido;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class OperadoresLogicosTest extends EntityManagerTest {

    @Test
    void usarOperadores() {
        String jpql = "select p from Pedido p" +
                " where (p.status = :status or p.status = 'AGUARDANDO') and p.total > 2400";

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        typedQuery.setParameter("status", StatusPedido.PAGO);

        List<Pedido> lista = typedQuery.getResultList();
        lista.forEach(p -> System.out.println(p.getId() + " - " + p.getStatus()));

        Assertions.assertFalse(lista.isEmpty());
    }
}
