package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pagamento;
import com.algaworks.ecommerce.model.Pedido;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class JoinTest extends EntityManagerTest {

    @Test
    void fazerJoinFetch() {
        String jpql = "select p from Pedido p"
                + " left join fetch p.pagamento"
                + " join fetch p.cliente"
                + " left join fetch p.notaFiscal";

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        List<Pedido> lista = typedQuery.getResultList();

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void fazerLeftJoin() {
        /* Com LEFT JOIN, todos os registros(Pedido) da esquerda são retornados, mesmo que não haja
         * correspondencia com a tabela da direita(Pagamento)
         * Com a clausula ON: o filtro é aplicado apenas nos registros da tabela á direita(Pagamento)
         * Com a clausula WHERE: o filtro é aplicado em toda a querie trazendo apenas registros correspondentes
        */
        String jpql = "select p, pag from Pedido p left join p.pagamento pag on pag.status = 'PROCESSANDO'";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println( "(" +
                ((Pedido)arr[0]).getId() + ")" +
                ((Pedido)arr[0]).getStatus() + " - " +
                (arr[1] != null ? ((Pagamento)arr[1]).getStatus() : "NULL")));
    }

    @Test
    void fazerJoin() {
        /* Com JOIN(INNER JOIN), apenas os registros correspondentes nas tabelas (Pedido e Pagamento) são retornados,
         * Ou seja, será retornado apenas Pedidos que contém Pagamento
         */
        String jpql = "select p, pag from Pedido p join p.pagamento pag";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println( "(" +
                ((Pedido)arr[0]).getId() + ")" +
                ((Pedido)arr[0]).getStatus() + " - " +
                ((Pagamento)arr[1]).getStatus()));
    }
}
