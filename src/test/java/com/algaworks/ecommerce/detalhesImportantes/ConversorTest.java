package com.algaworks.ecommerce.detalhesImportantes;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Produto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class ConversorTest extends EntityManagerTest {

    @Test
    void converter() {
        Produto produto = new Produto();
        produto.setDataCriacao(LocalDateTime.now());
        produto.setNome("Carregador de Notebook Dell");
        produto.setAtivo(Boolean.TRUE);

        entityManager.getTransaction().begin();
        entityManager.persist(produto);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Produto produtoVerificacao = entityManager.find(Produto.class, produto.getId());
        Assertions.assertTrue(produtoVerificacao.getAtivo());
    }
}
