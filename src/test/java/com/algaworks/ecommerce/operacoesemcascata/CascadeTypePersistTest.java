package com.algaworks.ecommerce.operacoesemcascata;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CascadeTypePersistTest extends EntityManagerTest {

    //@Test
    public void persistirProdutoComCategoria() {
        Produto produto = new Produto();
        produto.setDataCriacao(LocalDateTime.now());
        produto.setPreco(BigDecimal.TEN);
        produto.setNome("Fones de Ouvido");
        produto.setDescricao("A melhor qualidade de som");

        Categoria categoria = new Categoria();
        categoria.setNome("Áudio");

        produto.setCategorias(List.of(categoria)); // CascadeType.PERSIST

        entityManager.getTransaction().begin();
        entityManager.persist(produto);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Produto produtoVerificacao = entityManager.find(Produto.class, produto.getId());
        Assertions.assertFalse(produtoVerificacao.getCategorias().isEmpty());
    }

    //@Test
    public void persistirPedidoComItens() {
        Cliente cliente = entityManager.find(Cliente.class, 1);
        Produto produto = entityManager.find(Produto.class, 1);

        Pedido pedido = new Pedido();
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setCliente(cliente);
        pedido.setTotal(produto.getPreco());
        pedido.setStatus(StatusPedido.AGUARDANDO);

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId(new ItemPedidoId());
        itemPedido.setPedido(pedido);
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(1);
        itemPedido.setPrecoProduto(produto.getPreco());

        pedido.setItens(List.of(itemPedido)); // CascadeType.PERSIST

        entityManager.getTransaction().begin();
        entityManager.persist(pedido);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Pedido pedidoVerificacao = entityManager.find(Pedido.class, pedido.getId());
        Assertions.assertNotNull(pedidoVerificacao);
        Assertions.assertFalse(pedidoVerificacao.getItens().isEmpty());
    }

    @Test
    public void persistirItemPedidoComPedido() {
        Cliente cliente = entityManager.find(Cliente.class, 1);
        Produto produto = entityManager.find(Produto.class, 1);

        Pedido pedido = new Pedido();
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setCliente(cliente);
        pedido.setTotal(produto.getPreco());
        pedido.setStatus(StatusPedido.AGUARDANDO);

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId(new ItemPedidoId());
        itemPedido.setPedido(pedido); // Não é necessário CascadeType.PERSIST porque possui @MaspsId
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(1);
        itemPedido.setPrecoProduto(produto.getPreco());

        entityManager.getTransaction().begin();
        entityManager.persist(itemPedido);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Pedido pedidoVerificacao = entityManager.find(Pedido.class, pedido.getId());
        Assertions.assertNotNull(pedidoVerificacao);
    }

    //@Test
    public void persistirPedidoComCliente() {
        Cliente cliente = new Cliente();
        cliente.setDataNascimento(LocalDate.of(1980, 1, 1));
        cliente.setSexo(SexoCliente.MASCULINO);
        cliente.setNome("José Carlos");
        cliente.setCpf("123987456");

        Pedido pedido = new Pedido();
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setCliente(cliente); // CascadeType.PERSIST
        pedido.setTotal(BigDecimal.ZERO);
        pedido.setStatus(StatusPedido.AGUARDANDO);

        entityManager.getTransaction().begin();
        entityManager.persist(pedido);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Cliente clienteVerificacao = entityManager.find(Cliente.class, cliente.getId());
        Assertions.assertNotNull(clienteVerificacao);
    }
}
