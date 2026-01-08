package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class PathExpressionTest extends EntityManagerTest {

    @Test
    void buscarPedidosComProdutoEspecifico() {
        String jpql = "select p from Pedido p join fetch p.itens i where i.id.produtoId = 4";

        // 2: String jpql = "select p from Pedido p join p.itens i where i.produto.id = 4";
        // 3: String jpql = "select p from Pedido p join fetch p.itens i join i.produto pro where pro.id = 1";

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);

        List<Pedido> lista = typedQuery.getResultList();

        lista.forEach(p -> System.out.println("PedidoId: " + p.getId()));

        Assertions.assertFalse(lista.isEmpty());
    }
    @Test
    void fazerPathExpression() {
        String jpql = "select p.cliente.nome from Pedido p";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = typedQuery.getResultList();

        lista.forEach(arr -> System.out.println(arr[0]));

        Assertions.assertFalse(lista.isEmpty());
    }
}
