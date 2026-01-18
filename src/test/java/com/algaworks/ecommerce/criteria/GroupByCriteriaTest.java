package com.algaworks.ecommerce.criteria;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.*;
import com.algaworks.ecommerce.model.Categoria_;
import com.algaworks.ecommerce.model.Cliente_;
import com.algaworks.ecommerce.model.ItemPedido_;
import com.algaworks.ecommerce.model.Pedido_;
import com.algaworks.ecommerce.model.Produto_;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

class GroupByCriteriaTest extends EntityManagerTest {

    @Test
    void condicionarAgrupamentoComHaving() {
        /* Total de vendas dentre as categorias que mais vendem
       "select cat.nome, sum(ip.precoProduto * ip.quantidade), avg(ip.precoProduto * ip.quantidade)" +
       " from ItemPedido ip join ip.produto pro join pro.categorias cat" +
            " group by cat.id having sum(ip.precoProduto * ip.quantidade) > 3000"
        */
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<ItemPedido> root = criteriaQuery.from(ItemPedido.class);
        Join<ItemPedido, Produto> joinItemPedidoProduto = root.join(ItemPedido_.produto);
        Join<Produto, Categoria> joinProdutoCategoria = joinItemPedidoProduto.join(Produto_.categorias);

        var mediaDasVendas = criteriaBuilder.avg(
                criteriaBuilder.prod(root.get(ItemPedido_.precoProduto), root.get(ItemPedido_.quantidade)))
                .as(BigDecimal.class);
        var somaDasVendas = criteriaBuilder.sum(
                criteriaBuilder.prod(root.get(ItemPedido_.precoProduto), root.get(ItemPedido_.quantidade)));

        criteriaQuery
                .multiselect(joinProdutoCategoria.get(Categoria_.nome), somaDasVendas, mediaDasVendas)
                .groupBy(joinProdutoCategoria.get(Categoria_.id))
                .having(criteriaBuilder.greaterThan(mediaDasVendas, new BigDecimal(2000)));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Object[]> lista = typedQuery.getResultList();

        lista.forEach(arr -> System.out.println("Categoria: " + arr[0]
                + ", SUM: " + arr[1]
                + ", AVG:" + arr[2]));
    }
    @Test
    void agruparResultadoComFuncoes() {
        /* Total de vendas por mês:
            "select concat(year(p.dataCriacao), '/', function('monthname', p.dataCriacao)), sum(p.total)" +
                " from Pedido p group by concat(year(p.dataCriacao), '/', function('monthname', p.dataCriacao))"*/
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        Expression<Integer> anoCriacaoPedido = criteriaBuilder
                .function("year", Integer.class, root.get(Pedido_.dataCriacao));
        Expression<Integer> mesCriacaoPedido = criteriaBuilder
                .function("month", Integer.class, root.get(Pedido_.dataCriacao));
        Expression<String> nomeMesCriacaoPedido = criteriaBuilder
                .function("monthname", String.class, root.get(Pedido_.dataCriacao));

        Expression<String> anoMesConcat = criteriaBuilder.concat(
                criteriaBuilder.concat(anoCriacaoPedido.as(String.class), "/"),
                nomeMesCriacaoPedido
        );

        criteriaQuery.multiselect(
                anoMesConcat,
                criteriaBuilder.sum(root.get(Pedido_.total))
        );

        criteriaQuery.groupBy(anoMesConcat, mesCriacaoPedido);

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Object[]> lista = typedQuery.getResultList();

        lista.forEach(arr -> System.out.println("Ano/Mês: " + arr[0] + ", Sum: " + arr[1]));
    }

    @Test
    void agruparResultado03ExercicioEmItemPedido() {
        //"select c.nome, sum(ip.precoProduto * ip.quantidade) from ItemPedido ip" +
        // "join ip.pedido p join p.cliente c group by c.id"
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<ItemPedido> root = criteriaQuery.from(ItemPedido.class);
        Join<ItemPedido, Pedido> joinItemPedidoPedido = root.join(ItemPedido_.pedido);

        criteriaQuery.multiselect(
                joinItemPedidoPedido.get(Pedido_.cliente).get(Cliente_.nome),
                criteriaBuilder.sum(
                        criteriaBuilder.prod(
                                root.get(ItemPedido_.precoProduto),
                                root.get(ItemPedido_.quantidade))
                )
        );

        criteriaQuery.groupBy(joinItemPedidoPedido.get(Pedido_.cliente).get(Cliente_.id));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Object[]> lista = typedQuery.getResultList();

        lista.forEach(arr -> System.out.println("Nome: " + arr[0] + ", total: " + arr[1]));
    }

    @Test
    void agruparResultado03Exercicio() {
        //"select c.nome, sum(p.total) from Pedido p join p.cliente c group by c.id"
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        Join<Pedido, Cliente> joinPedidoCliente = root.join(Pedido_.cliente);

        criteriaQuery.multiselect(
                joinPedidoCliente.get(Cliente_.nome),
                criteriaBuilder.sum(root.get(Pedido_.total)));

        criteriaQuery.groupBy(joinPedidoCliente.get(Cliente_.id));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Object[]> lista = typedQuery.getResultList();

        lista.forEach(arr -> System.out.println("Nome: " + arr[0] + ", total: " + arr[1]));
    }

    @Test
    void agruparResultado02() {
        /*"select c.nome, sum(ip.precoProduto * ip.quantidade) from ItemPedido ip" +
                " join ip.produto pro join pro.categorias c group by c.id"*/
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<ItemPedido> root = criteriaQuery.from(ItemPedido.class);
        Join<ItemPedido, Produto> joinProduto = root.join(ItemPedido_.produto);
        Join<Produto, Categoria> joinProdutoCategoria = joinProduto.join(Produto_.categorias);

        criteriaQuery.multiselect(
                joinProdutoCategoria.get(Categoria_.nome),
                criteriaBuilder.sum(criteriaBuilder
                        .prod(root.get(ItemPedido_.precoProduto), root.get(ItemPedido_.quantidade)))
        );

        criteriaQuery.groupBy(joinProdutoCategoria.get(Categoria_.nome));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Object[]> lista = typedQuery.getResultList();

        lista.forEach(arr -> System.out.println("Nome: " + arr[0] + ", Soma: " + arr[1]));
    }

    @Test
    void agruparResultado01() {
        //"select c.nome, count(p.id) from Categoria c join c.produtos p group by c.id"
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Categoria> root = criteriaQuery.from(Categoria.class);
        Join<Categoria, Produto> joinProduto = root.join(Categoria_.produtos, JoinType.LEFT);

        criteriaQuery.multiselect(
                root.get(Categoria_.nome),
                criteriaBuilder.count(joinProduto.get(Produto_.id))
        );

        criteriaQuery.groupBy(root.get(Categoria_.id));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Object[]> lista = typedQuery.getResultList();

        lista.forEach(arr -> System.out.println("Nome: " + arr[0] + ", Count: " + arr[1]));
    }
}
