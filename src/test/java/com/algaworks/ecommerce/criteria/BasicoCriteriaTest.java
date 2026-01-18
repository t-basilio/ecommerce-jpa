package com.algaworks.ecommerce.criteria;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.dto.ItemPedidoDTO;
import com.algaworks.ecommerce.dto.ProdutoDTO;
import com.algaworks.ecommerce.model.*;
import com.algaworks.ecommerce.model.Cliente_;
import com.algaworks.ecommerce.model.Pedido_;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class BasicoCriteriaTest extends EntityManagerTest {

    @Test
    void userDistinct() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        root.join(Pedido_.itens);

        criteriaQuery.select(root);
        criteriaQuery.distinct(true); //apesar de usar, o hirbernate 6 ja aplica por padrão
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Pedido> lista = typedQuery.getResultList();
        lista.forEach(p -> System.out.println("ID: " + p.getId()));
    }

    @Test
    void ordenarResultados() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        Root<Cliente> root = criteriaQuery.from(Cliente.class);

        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(Cliente_.nome)));

        TypedQuery<Cliente> typedQuery = entityManager
                .createQuery(criteriaQuery);

        List<Cliente> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(c -> System.out.println("ID: " + c.getId() + ", Nome: " + c.getNome()));
    }
    @Test
    void usarPathExpression() {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<ItemPedidoDTO> criteriaQuery = criteriaBuilder.createQuery(ItemPedidoDTO.class);
        final Root<ItemPedido> root = criteriaQuery.from(ItemPedido.class);
        final Join<ItemPedido, Produto> joinProduto = root.join("produto");

        Selection<ItemPedidoDTO> itemPedidoDTOSelection = criteriaBuilder.construct(
                ItemPedidoDTO.class,
                root.get("precoProduto"),
                root.get("quantidade"),
                joinProduto.get("id"),
                joinProduto.get("nome")
        );

        criteriaQuery.select(itemPedidoDTOSelection);

        TypedQuery<ItemPedidoDTO> typedQuery = entityManager.createQuery(criteriaQuery);
        List<ItemPedidoDTO> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void projetarResultadoDTO() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProdutoDTO> criteriaQuery = criteriaBuilder.createQuery(ProdutoDTO.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.select(criteriaBuilder.construct(ProdutoDTO.class, root.get("id"), root.get("nome")));

        TypedQuery<ProdutoDTO> typedQuery = entityManager.createQuery(criteriaQuery);
        List<ProdutoDTO> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(dto -> System.out.println("ID: " + dto.getId() + ", Nome: " + dto.getNome()));
    }

    @Test
    void projetarResultadoTuple() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.select(criteriaBuilder
                .tuple(root.get("id").alias("id"), root.get("nome").alias("nome")));

        TypedQuery<Tuple> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Tuple> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(t -> System.out.println(t.get("id") + ", " + t.get("nome")));
    }

    @Test
    void projetarResultado() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.multiselect(root.get("id"), root.get("nome"));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @Test
    void retornarTodosProdutosExercicio() {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.select(root);
        TypedQuery<Produto> typedQuery = entityManager
                .createQuery(criteriaQuery);

        List<Produto> produtos = typedQuery.getResultList();
        Assertions.assertFalse(produtos.isEmpty());
    }

    @Test
    void selecionarUmAtributoParaRetorno() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.select(root.get("cliente"));
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), 1));

        TypedQuery<Cliente> typedQuery = entityManager
                .createQuery(criteriaQuery);

        Cliente cliente = typedQuery.getSingleResult();
        Assertions.assertEquals("Fernando Medeiros", cliente.getNome());
    }

    @Test
    void buscarPorIdentificador() {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), 1));

        TypedQuery<Pedido> typedQuery = entityManager
                .createQuery(criteriaQuery);

        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }
}
