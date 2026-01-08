package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.TimeZone;

class FuncoesTest extends EntityManagerTest {

    @Test
    void aplicarFuncaoAgregacao() {
        // avg, count, min, max, sum
        String jpql = "select sum(p.total) from Pedido p";

        TypedQuery<Number> typedQuery = entityManager.createQuery(jpql, Number.class);

        List<Number> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(n -> System.out.println("Valor: " + n));
    }

    @Test
    void aplicarFuncaoNativas() {

        String jpql = "select function('dayname', p.dataCriacao), p.total from Pedido p" +
                " where function('acima_media_faturamento', p.total) = 1";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println("Dia: " + arr[0] + " | Total: " + arr[1]));
    }

    @Test
    void aplicarFuncaoColecao() {
        /* Total de Itens de um pedido:
         * "select sum(ip.precoProduto * ip.quantidade) from ItemPedido ip join ip.pedido p where p.id = :pedidoId"
         * Total de itens por pedidos
         * "select p.id, sum(ip.precoProduto * ip.quantidade) from ItemPedido ip join ip.pedido p group by p.id"
        */

        String jpql = "select p, size(p.itens) from Pedido p where size(p.itens) > 1";
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println("Id: " + ((Pedido)arr[0]).getId() + " - itens: " + arr[1]));
    }

    @Test
    void aplicarFuncaoNumero() {
        // abs(n) - retorna numero absoluto(positivo)
        // mod(n, divisor) - retorna resto da divisão
        // sqrt(n) - retorna a razi quadrada
        String jpql = "select abs(-10), mod(p.id, 2), sqrt(p.total) from Pedido p where abs(p.total) > 1000";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + " | " + arr[1] + " | " + arr[2]));
    }

    @Test
    void aplicarFuncaoData() {
        // TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        // current_date, current_time, current_timestamp
        // year(p.dataCriacao), month(p.dataCriacao), day(p.dataCriacao)
        // hour(p.dataCriacao), minute(p.dataCriacao), second(p.dataCriacao)

        String jpql = "select hour(p.dataCriacao), minute(p.dataCriacao), second(p.dataCriacao) from Pedido p" +
                " where hour(p.dataCriacao) > 15";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + " | " + arr[1] + " | " + arr[2]));
    }

    @Test
    void aplicarFuncaoString() {
        // concat('Categoria: ', c.nome)
        // length(c.nome)
        // locate('a', c.nome)
        // substring(c.nome, 1, 2)
        // lower(c.nome)
        // upper(c.nome)
        // trim(c.nome)

        String jpql = "select c.nome, upper(c.nome) from Categoria c" +
                " where substring(c.nome, 1, 1) = 'N'";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + " - " + arr[1]));
    }
}
