package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;
import com.algaworks.ecommerce.model.StatusPedido;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class NamedQueryTest extends EntityManagerTest {

    @Test
    void executarConsultaArquivoXMLEspecificoProduto() {
        TypedQuery<Produto> typedQuery = entityManager.createNamedQuery("Produto.buscarMaiorPreco", Produto.class);
        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("Id: " + p.getId()));
    }

    @Test
    void executarConsultaArquivoXMLEspecificoPedido() {
        TypedQuery<Pedido> typedQuery = entityManager.createNamedQuery("Pedido.buscarPorStatus", Pedido.class);
        typedQuery.setParameter("status", StatusPedido.CANCELADO);
        List<Pedido> lista = typedQuery.getResultList();

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void executarConsultaArquivoXML() {
        TypedQuery<Pedido> typedQuery = entityManager.createNamedQuery("Pedido.listar", Pedido.class);
        List<Pedido> lista = typedQuery.getResultList();

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void executarConsulta() {
        TypedQuery<Produto> typedQuery = entityManager.createNamedQuery("Produto.listarPorCategoria", Produto.class);
        typedQuery.setParameter("categoria", 2);
        List<Produto> lista = typedQuery.getResultList();

        Assertions.assertFalse(lista.isEmpty());
    }
}
