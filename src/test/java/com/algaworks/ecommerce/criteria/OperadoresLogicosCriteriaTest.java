package com.algaworks.ecommerce.criteria;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Pedido_;
import com.algaworks.ecommerce.model.StatusPedido;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

class OperadoresLogicosCriteriaTest extends EntityManagerTest {

    @Test
    void usarOperadores() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.select(root);
        // select p from Pedido where (p.status = 'AGUARDANDO' or p.status = 'PAGO') and p.total > 499
        criteriaQuery.where(
                criteriaBuilder.or(
                        criteriaBuilder.equal(root.get(Pedido_.status),StatusPedido.AGUARDANDO),
                        criteriaBuilder.equal(root.get(Pedido_.status),StatusPedido.PAGO)
                ), // virgula aqui representa um AND
                criteriaBuilder.greaterThan(root.get(Pedido_.total), new BigDecimal(499))
        );

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", Total: " + p.getTotal()));
    }
}
