package com.algaworks.ecommerce.mapeamentoavancado;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class HerancaTest extends EntityManagerTest {

    @Test
    public void salvarCliente() {
        Cliente cliente = new Cliente();
        cliente.setNome("Fernanda Morais");
        cliente.setSexo(SexoCliente.FEMININO);
        cliente.setCpf("987654");

        entityManager.getTransaction().begin();
        entityManager.persist(cliente);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Cliente clienteVerificacao = entityManager.find(Cliente.class, cliente.getId());
        Assertions.assertNotNull(cliente.getId());
    }

    @Test
    public void buscarPagamentos() {
        var pagamentos = entityManager
                .createQuery("select p from Pagamento p")
                .getResultList();
        System.out.println(pagamentos);

        Optional<PagamentoBoleto> boleto = pagamentos.stream().filter(p -> p instanceof PagamentoBoleto).findFirst();
        Assertions.assertEquals("8889999966666666", boleto.get().getCodigoBarras());
    }

    @Test
    public void incluirPagamentoPedido() {
        Pedido pedidoUm = entityManager.find(Pedido.class, 1);

        PagamentoCartao pagamentoCartao = new PagamentoCartao();
        pagamentoCartao.setPedido(pedidoUm);
        pagamentoCartao.setStatus(StatusPagamento.PROCESSANDO);
        pagamentoCartao.setNumeroCartao("123456");


        entityManager.getTransaction().begin();
        entityManager.persist(pagamentoCartao);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Pedido pedidoUmVerificacao = entityManager.find(Pedido.class, pedidoUm.getId());
        Assertions.assertNotNull(pedidoUmVerificacao.getPagamento());

    }
}
