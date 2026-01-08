package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Produto;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class DynamicQueryTest extends EntityManagerTest {

    @Test
    void executarConsultaDinamica() {
        Produto produto = new Produto();
        produto.setNome("Câmero GoPro");

        List<Produto> lista = pesquisar(produto);

        Assertions.assertFalse(lista.isEmpty());
        Assertions.assertEquals("Câmero GoPro Hero 7", lista.get(0).getNome());
    }

    private List<Produto> pesquisar(Produto consultado) {
        StringBuilder jpql = new StringBuilder("select p from Produto p where 1 = 1");

        if (consultado.getNome() != null)
            jpql.append(" and p.nome like concat('%', :nome, '%')");

        if (consultado.getDescricao() != null)
            jpql.append(" and p.descricao like concat('%', :descricao, '%')");

        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql.toString(), Produto.class);

        if (consultado.getNome() != null)
            typedQuery.setParameter("nome", consultado.getNome());

        if (consultado.getDescricao() != null)
            typedQuery.setParameter("descricao", consultado.getDataCriacao());

        return typedQuery.getResultList();
    }
}
