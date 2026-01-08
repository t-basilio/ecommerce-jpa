package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

class ExpressoesCondicionalTest extends EntityManagerTest {

    @Test
    void passarExpressaoIN() {

        Cliente cliente1 = entityManager.find(Cliente.class, 1);
        Cliente cliente2 = entityManager.find(Cliente.class, 2);
        List<Cliente> clientes = Arrays.asList(cliente1, cliente2);

        String jpql = "select p from Pedido p where p.cliente in (:clientes)";

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        typedQuery.setParameter("clientes", clientes);

        List<Pedido> lista = typedQuery.getResultList();
        lista.forEach(p -> System.out.println("Id: " + p.getId() + ", status: " + p.getStatus()));

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void passarExpressaoCase() {
        /* com type(tipo do objeto):
        String jpql = "select p.id," +
                " case type(p.pagamento)" +
                "   when PagamentoBoleto then 'Pago com boleto'" +
                "   when PagamentoCartao then 'Pago com cartão'" +
                "   else 'Ainda não pago'" +
                " end" +
                " from Pedido p"
        */
        String jpql = "select p.id," +
                " case p.status" +
                "   when 'PAGO' then 'Está pago'" +
                "   when 'CANCELADO' then 'Foi cancelado'" +
                "   else 'Está aguardando'" +
                " end" +
                " from Pedido p";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = typedQuery.getResultList();
        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void passarExpressaoCondicionalDiferente() {
        String jpql = "select p from Produto p where p.preco <> 1500";

        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);

        List<Produto> lista = typedQuery.getResultList();
        lista.forEach(p -> System.out.println(p.getNome()));

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void passarBetweenComDatas() {
        String jpql = "select p from Pedido p" +
                " where p.dataCriacao between :dataInicial and :dataFinal";

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        typedQuery.setParameter("dataInicial", LocalDateTime.now().minusDays(10));
        typedQuery.setParameter("dataFinal", LocalDateTime.now().minusDays(5));

        List<Pedido> lista = typedQuery.getResultList();
        lista.forEach(p -> System.out.println(p.getId() + " - " + p.getStatus()));

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void passarBetween() {
        String jpql = "select p from Produto p" +
                " where p.preco between :precoInicial and :precoFinal";

        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);
        typedQuery.setParameter("precoInicial", new BigDecimal(499));
        typedQuery.setParameter("precoFinal", new BigDecimal(1500));

        List<Produto> lista = typedQuery.getResultList();
        lista.forEach(p -> System.out.println(p.getNome()));

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void passarMaiorMenorComDatas() {
        String jpql = "select p from Pedido p" +
                " where p.dataCriacao > :dataInicial and p.dataCriacao <= :dataFinal";

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        typedQuery.setParameter("dataInicial", LocalDateTime.now().minusDays(2));
        typedQuery.setParameter("dataFinal", LocalDateTime.now().minusDays(1));

        List<Pedido> lista = typedQuery.getResultList();
        lista.forEach(p -> System.out.println(p.getId() + " - " + p.getStatus()));

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void passarMaiorMenor() {
        String jpql = "select p from Produto p" +
                " where p.preco >= :precoInicial and p.preco <= :precoFinal";

        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);
        typedQuery.setParameter("precoInicial", new BigDecimal(400));
        typedQuery.setParameter("precoFinal", new BigDecimal(1500));

        List<Produto> lista = typedQuery.getResultList();
        lista.forEach(p -> System.out.println(p.getNome()));

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void passarIsNull() {
        String jpql = "select p from Produto p where p.foto is null";

        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);

        List<Produto> lista = typedQuery.getResultList();
        lista.forEach(p -> System.out.println(p.getNome()));

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void passarIsEmpty() {
        String jpql = "select p from Produto p where p.categorias is empty";

        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);

        List<Produto> lista = typedQuery.getResultList();
        lista.forEach(p -> System.out.println(p.getNome()));

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void passarExpressaoCondicinalLike() {
        String jpql = "select c from Cliente c where c.nome like concat('%', :nome, '%')";

        TypedQuery<Cliente> typedQuery = entityManager.createQuery(jpql, Cliente.class);
        typedQuery.setParameter("nome", "ar");

        List<Cliente> lista = typedQuery.getResultList();
        lista.forEach(c -> System.out.println(c.getNome()));

        Assertions.assertFalse(lista.isEmpty());
    }
}
