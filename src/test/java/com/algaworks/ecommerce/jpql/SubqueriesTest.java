package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;

class SubqueriesTest extends EntityManagerTest {

    @Test
    void pesquisarComAny() {
         /* Produtos que já foram vendidos por um preço diferente do atual
            String jpql = "select p from Produto p where" +
                    " p.preco <> ALL (select precoProduto from ItemPedido where produto = p)"
          */

         // Produtos que já foram vendido, pelo menos, uma vez pelo preço atual - ANY ou SOME
          String jpql = "select p from Produto p where" +
                " p.preco = ANY (select precoProduto from ItemPedido where produto = p)";

        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);

        List<Produto> lista = typedQuery.getResultList();
        lista.forEach(p -> System.out.println("Nome: " + p.getNome() + ", Preço: " + p.getPreco()));

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void pesquisarComAll() {
        /* Produtos que sempre foram vendidos pelo preço atual
            String jpql = "select p from Produto p where" +
                    " p.preco = ALL (select precoProduto from ItemPedido where produto = p)
         * Produtos que não foram vendidos mais depois que encareceram
            String jpql = "select p from Produto p where" +
                " p.preco > ALL (select precoProduto from ItemPedido where produto = p)"
        */
        // Produtos que sempre foram vendidos pelo mesmo preço
        String jpql = "select distinct p from ItemPedido ip join ip.produto p where" +
                " ip.precoProduto = ALL" +
                " (select precoProduto from ItemPedido ip2 where produto = p and ip2.id <> ip.id)";

        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);

        List<Produto> lista = typedQuery.getResultList();
        lista.forEach(p -> System.out.println("Nome: " + p.getNome() + ", Preço: " + p.getPreco()));

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void pesquisarComExistsDesafio() {
        // Produtos que não foram vendidos com o preço atual
        String jpql = "select p from Produto p where exists" +
                " (select 1 from ItemPedido ip2" +
                " where ip2.produto = p" +
                " and ip2.precoProduto <> p.preco)";

        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);

        List<Produto> lista = typedQuery.getResultList();
        lista.forEach(p -> System.out.println("Nome: " + p.getNome() + ", Preço: " + p.getPreco()));

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void pesquisarComSubquerieDesafio() {
        // Clientes que fizeram pelo menos 2 pedidos
        String jpql = "select c from Cliente c where" +
                " 1 < (select count(p2.cliente) from Pedido p2" +
                " where p2.cliente = c)";

        TypedQuery<Cliente> typedQuery = entityManager.createQuery(jpql, Cliente.class);

        List<Cliente> lista = typedQuery.getResultList();
        lista.forEach(c -> System.out.println("Nome: " + c.getNome()));

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void pesquisarComINDesafio() {
        // Pedidos que contem produto da categoria 7(Smartphone)
        String jpql = "select p from Pedido p where p in" +
                " (select ip2.pedido from ItemPedido ip2" +
                "   join ip2.produto pro2 join pro2.categorias cat2" +
                "   where cat2.id = 7)";

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);

        List<Pedido> lista = typedQuery.getResultList();
        lista.forEach(p -> System.out.println("Id: " + p.getId() + ", Status: " + p.getStatus()));

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void pesquisarComExists() {
        // Produtos que já foram vendidos(contem no ItemPedido)
        String jpql = "select p from Produto p where exists" +
                " (select 1 from ItemPedido ip2 join ip2.produto p2 where p2 = p)";

        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);

        List<Produto> lista = typedQuery.getResultList();
        lista.forEach(p -> System.out.println("Id: " + p.getId() + ", Nome: " + p.getNome()));

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void pesquisarComIN() {
        String jpql = "select p from Pedido p where p.id in" +
                " (select p2.id from ItemPedido i2 join i2.pedido p2 join i2.produto pro2 " +
                "       where pro2.preco > 500)";

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);

        List<Pedido> lista = typedQuery.getResultList();
        lista.forEach(p -> System.out.println("Id: " + p.getId() + ", status: " + p.getStatus()));

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void pesquisarSubqueries() {
        /* O(s) produto(s) mais caros da base
        String jpql = "select p from Produto p" +
              " where p.preco = (select max(preco) from Produto)"
         * Todos os pedidos acima da media de vendas
        String jpql = "select p from Pedido p" +
                " where p.total > (select avg(total) from Pedido)"
         * Bons clientes versão 1
         String jpql = "select c from Cliente c" +
                " where 500 < (select sum(p.total) from c.pedidos p)"
*/
        // Bons clientes versão 2
        String jpql = "select c from Cliente c" +
                " where 500 < (select sum(p.total) from Pedido p where p.cliente = c)";

        TypedQuery<Cliente> typedQuery = entityManager.createQuery(jpql, Cliente.class);

        List<Cliente> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println("ID: " + p.getId() + ", Nome: " + p.getNome()));
    }
}
