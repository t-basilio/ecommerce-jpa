package com.algaworks.ecommerce.criteria;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.*;
import com.algaworks.ecommerce.model.Categoria_;
import com.algaworks.ecommerce.model.ItemPedidoId_;
import com.algaworks.ecommerce.model.ItemPedido_;
import com.algaworks.ecommerce.model.Pedido_;
import com.algaworks.ecommerce.model.Produto_;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

class SubqueriesCriteriaTest extends EntityManagerTest {
    /*
        Subqueries são utilizadas quando não temos a informação necessaria em nossa base
        para aplicar nossos filtros, Ex: O maior preço entre os produtos
    */
    @Test
    void pesquisarSubqueriesComAllExercicio() {
        /* Produtos que SEMPRE foram vendidos pelo mesmo preço
            "select distinct p from ItemPedido ip join ip.produto p where" +
             " ip.precoProduto = ALL (select precoProduto from ItemPedido ip2 where produto = p and ip2.id <> ip.id)"
         */
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<ItemPedido> root = criteriaQuery.from(ItemPedido.class);
        Join<ItemPedido, Produto> joinProduto = root.join(ItemPedido_.produto);

        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);

        subquery.select(subqueryRoot.get(ItemPedido_.precoProduto))
                .where(
                        criteriaBuilder.equal(subqueryRoot.get(ItemPedido_.produto), joinProduto),
                        criteriaBuilder.notEqual(subqueryRoot, root));

        criteriaQuery.select(joinProduto).distinct(true)
                .where(
                        criteriaBuilder.equal(root.get(ItemPedido_.precoProduto), criteriaBuilder.all(subquery)));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Produto> lista = typedQuery.getResultList();
        lista.forEach(p -> System.out.println("Nome: " + p.getNome() + ", Preço: " + p.getPreco()));

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void pesquisarSubqueriesComAny02() {
         /* Produtos que já foram vendidos por um preço diferente do atual
            String jpql = "select p from Produto p where" +
                    " p.preco <> ANY (select precoProduto from ItemPedido where produto = p)"
          */
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);

        subquery.select(subqueryRoot.get(ItemPedido_.precoProduto))
                .where(criteriaBuilder.equal(subqueryRoot.get(ItemPedido_.produto), root));

        criteriaQuery.select(root)
                .where(
                        criteriaBuilder.notEqual(root.get(Produto_.preco), criteriaBuilder.any(subquery)));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Produto> lista = typedQuery.getResultList();
        lista.forEach(p -> System.out.println("Nome: " + p.getNome() + ", Preço: " + p.getPreco()));

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void pesquisarSubqueriesComAny01() {
         /* Produtos que já foram vendido, pelo menos, uma vez pelo preço atual - ANY ou SOME
                "select p from Produto p where" +
                " p.preco = ANY (select precoProduto from ItemPedido where produto = p)"
          */
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);

        subquery.select(subqueryRoot.get(ItemPedido_.precoProduto))
                .where(criteriaBuilder.equal(subqueryRoot.get(ItemPedido_.produto), root));

        criteriaQuery.select(root)
                .where(
                        criteriaBuilder.equal(root.get(Produto_.preco), criteriaBuilder.any(subquery)));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Produto> lista = typedQuery.getResultList();
        lista.forEach(p -> System.out.println("Nome: " + p.getNome() + ", Preço: " + p.getPreco()));

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void pesquisarSubqueriesComAll02() {
        /* Produtos que não foram vendidos mais depois que encareceram
            String jpql = "select p from Produto p where" +
                " p.preco > ALL (select precoProduto from ItemPedido where produto = p)" +
                " and exists (select 1 from ItemPedido where produto = p)"
         */
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);

        subquery.select(subqueryRoot.get(ItemPedido_.precoProduto))
                .where(criteriaBuilder.equal(subqueryRoot.get(ItemPedido_.produto), root));

        criteriaQuery.select(root)
                .where(
                        criteriaBuilder.greaterThan(root.get(Produto_.preco), criteriaBuilder.all(subquery)),
                        criteriaBuilder.exists(subquery)
                );

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Produto> lista = typedQuery.getResultList();

        lista.forEach(p -> System.out.printf("Nome: %s, Preço: %.2f%n", p.getNome(), p.getPreco()));
    }

    @Test
    void pesquisarSubqueriesComAll01() {
        /* Produtos que SEMPRE foram vendidos pelo preço atual
            "select p from Produto p where" +
                    " p.preco = ALL (select precoProduto from ItemPedido where produto = p)" +
                    " and exists (select 1 from ItemPedido where produto = p)"
         */
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);

        subquery.select(subqueryRoot.get(ItemPedido_.precoProduto))
                .where(criteriaBuilder.equal(subqueryRoot.get(ItemPedido_.produto), root));

        criteriaQuery.select(root)
                .where(
                        criteriaBuilder.equal(root.get(Produto_.preco), criteriaBuilder.all(subquery)),
                        criteriaBuilder.exists(subquery));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Produto> lista = typedQuery.getResultList();

        lista.forEach(p -> System.out.printf("Nome: %s, Preço: %.2f%n", p.getNome(), p.getPreco()));
    }

    @Test
    void pesquisarSubqueriesComExistsExercicio() {
        /* Produtos que foram vendidos com o preço diferente do preço atual
            "select p from Produto p where exists (" +
                " select 1 from ItemPedido ip2 where ip2.produto = p" +
                " and ip2.precoProduto <> p.preco)"
        */
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        subquery.select(criteriaBuilder.literal(1));
        subquery.where(
                criteriaBuilder.equal(subqueryRoot.get(ItemPedido_.produto), root),
                criteriaBuilder.notEqual(
                        subqueryRoot.get(ItemPedido_.precoProduto), root.get(Produto_.preco))
        );

        criteriaQuery.select(root)
                .where(criteriaBuilder.exists(subquery));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Produto> lista = typedQuery.getResultList();

        lista.forEach(p -> System.out.println("Nome: " + p.getNome() + ", Preço: " + p.getPreco()));
    }

    @Test
    void pesquisarSubqueriesComINExercicio() {
        /* Pedidos que tem algum produto de categoria 2, abaixo querie montada a partir de um JPQL:
            "select p from Pedido p where p.id in (" +
                  " select ip2.id.pedidoId from ItemPedido ip2 join ip2.produto pro2 join pro2.categorias c2" +
                  " where c2.id = 2)"
        */
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        Join<ItemPedido, Produto> subqueryJoinProduto = subqueryRoot.join(ItemPedido_.produto);
        Join<Produto, Categoria> subqueryJoinCategoria = subqueryJoinProduto.join(Produto_.categorias);

        subquery.select(subqueryRoot.get(ItemPedido_.id).get(ItemPedidoId_.pedidoId))
                        .where(criteriaBuilder.equal(subqueryJoinCategoria.get(Categoria_.id), 2));

        criteriaQuery.select(root)
                .where(root.get(Pedido_.id).in(subquery));

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.printf("ID: %d, STATUS: %s%n", p.getId(), p.getStatus()));
    }

    @Test
    void pesquisarSubqueriesExercicio() {
        // Clientes que fizeram mais de 1 pedido
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        Root<Cliente> root = criteriaQuery.from(Cliente.class);

        Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
        Root<Pedido> subqueryRoot = subquery.from(Pedido.class);
        subquery.select(criteriaBuilder.count(subqueryRoot))
                        .where(criteriaBuilder.equal(subqueryRoot.get(Pedido_.cliente), root));

        criteriaQuery.select(root)
                .where(criteriaBuilder.greaterThan(subquery, 2L));

        TypedQuery<Cliente> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Cliente> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(c -> System.out.printf("Nome: %s%n", c.getNome()));
    }

    @Test
    void pesquisarSubqueriesComExists() {
        /* Produtos que foram vendidos
            "select p from Produto p where exists" +
                " (select 1 from ItemPedido ip2 join ip2.produto p2 where p2 = p)"
         */
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
        Root<ItemPedido> subQueryRoot = subquery.from(ItemPedido.class);

        subquery.select(criteriaBuilder.literal(1))
                        .where(criteriaBuilder.equal(subQueryRoot.get(ItemPedido_.produto), root));

        criteriaQuery.select(root)
                .where(criteriaBuilder.exists(subquery));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.printf("ID: %d%n",p.getId()));
    }
    @Test
    void pesquisarSubqueriesComIN() {
        /* Pedidos que contém produtos com preços atuais(podem ter sido reduzido antes) acima de 800
            "select p from Pedido p where p.id in" +
                        " (select p2.id from ItemPedido i2 join i2.pedido p2 join i2.produto pro2 where pro2.preco > 800)"
        */
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
        Root<ItemPedido> subQueryRoot = subquery.from(ItemPedido.class);
        Join<ItemPedido, Pedido> subqueryJoinItemPedidoPedido = subQueryRoot.join(ItemPedido_.pedido);
        Join<ItemPedido, Produto> subqueryJoinItemPedidoProduto = subQueryRoot.join(ItemPedido_.produto);

        subquery.select(subqueryJoinItemPedidoPedido.get(Pedido_.id))
                .where(criteriaBuilder.greaterThan(
                        subqueryJoinItemPedidoProduto.get(Produto_.preco), new BigDecimal(800))
                );

        criteriaQuery.select(root)
                .where(root.get(Pedido_.id).in(subquery));

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.printf("ID: %d, Status: %s%n", p.getId(), p.getStatus()));
    }

    @Test
    void pesquisarSubqueries03() {
        /* Bons clientes(compraram acima de 2000)
            "select c from Cliente c where 2000 < (select sum(p.total) from Pedido p where p.cliente = c)"
        */
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        Root<Cliente> root = criteriaQuery.from(Cliente.class);

        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<Pedido> subQueryRoot = subquery.from(Pedido.class);
        subquery.select(criteriaBuilder.sum(subQueryRoot.get(Pedido_.total)))
                        .where(criteriaBuilder.equal(subQueryRoot.get(Pedido_.cliente), root));

        criteriaQuery.select(root)
                .where(criteriaBuilder.greaterThan(subquery, new BigDecimal(2000)));

        TypedQuery<Cliente> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Cliente> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(c -> System.out.printf("ID: %d, Nome: %s%n", c.getId(), c.getNome()));
    }

    @Test
    void persquisarSubqueries02() {
        /* Pedidos com venda acima da média
            "select p from Pedido p where p.total > (select avg(total) from Pedido)"
        */
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<Pedido> subQueryRoot = subquery.from(Pedido.class);
        subquery.select(criteriaBuilder.avg(subQueryRoot.get(Pedido_.total)).as(BigDecimal.class));

        criteriaQuery.select(root)
                .where(criteriaBuilder.greaterThan(root.get(Pedido_.total), subquery));

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.printf("ID: %d, Status: %s, Total: %.2f%n",
                p.getId(), p.getStatus(), p.getTotal()));
    }

    @Test
    void persquisarSubqueries01() {
        /* Produtos com os maiores preços
            "select p from Produto p where p.preco = (select max(preco) from Produto)"
        */
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<Produto> subQueryRoot = subquery.from(Produto.class);
        subquery.select(criteriaBuilder.max(subQueryRoot.get(Produto_.preco)));

        criteriaQuery.select(root)
                .where(criteriaBuilder.equal(root.get(Produto_.preco), subquery));

        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Produto> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.printf("ID: %d, Nome: %s, Preço: %.2f%n",
                p.getId(), p.getNome(), p.getPreco()));
    }
}
