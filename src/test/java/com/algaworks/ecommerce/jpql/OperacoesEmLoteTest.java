package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Produto;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;

class OperacoesEmLoteTest extends EntityManagerTest {

    private static final int LIMITE_INSERCOES = 4;

    @Test
    void removerEmLote() {
        entityManager.getTransaction().begin();
        String jpql = "delete from Produto p where p.id between 8 and 12";

        Query query = entityManager.createQuery(jpql);
        query.executeUpdate();

        entityManager.getTransaction().commit();
    }

    @Test
    void atualizarEmLote() {
        /*  Somar preços dos produtos até o id = 10
            String jpql = "update Produto p set p.preco = p.preco + 1 where id between 1 and 10"
         */
        entityManager.getTransaction().begin();
        // Aumentar o preço em 10% dos produtos de categoria 2(Livros)
        String jpql = "update Produto p set p.preco = (p.preco * 1.1)" +
                " where exists" +
                " (select 1 from p.categorias c2 where c2.id = :categoria)";

        Query query = entityManager.createQuery(jpql);
        query.setParameter("categoria", 2);
        query.executeUpdate();

        entityManager.getTransaction().commit();
    }

    @Test
    void inserirEmLote() throws IOException {
        InputStream in = OperacoesEmLoteTest.class.getClassLoader()
                .getResourceAsStream("produtos/importar.txt");

        Assertions.assertNotNull(in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        entityManager.getTransaction().begin();

        int contador = 0;

        for(String linha : reader.lines().toList()) {
            if (linha.isBlank())
                continue;

            String[] produtoColuna = linha.split(";");
            Produto produto = new Produto();
            produto.setNome(produtoColuna[0]);
            produto.setDescricao(produtoColuna[1]);
            produto.setPreco(new BigDecimal(produtoColuna[2]));
            produto.setDataCriacao(LocalDateTime.now());

            entityManager.persist(produto);

            if( ++contador == LIMITE_INSERCOES) {
                entityManager.flush();
                entityManager.clear();

                contador = 0;
                System.out.println("--------------------------------------");
            }
        }

        entityManager.getTransaction().commit();
    }
}
