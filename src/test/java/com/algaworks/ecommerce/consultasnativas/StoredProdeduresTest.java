package com.algaworks.ecommerce.consultasnativas;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

class StoredProdeduresTest extends EntityManagerTest {

    @Test
    void chamarNamedStoredProcedure() {
        StoredProcedureQuery procedure = entityManager.createNamedStoredProcedureQuery("compraram_acima_media");

        procedure.setParameter("ano", 2025);

        List<Cliente> lista = procedure.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void atualizarPrecoProdutoExercicioProcedure() {
        StoredProcedureQuery procedure = entityManager
                .createStoredProcedureQuery("ajustar_preco_produto");

        procedure.registerStoredProcedureParameter("produto_id", Integer.class, ParameterMode.IN);
        procedure.registerStoredProcedureParameter("percentual_ajuste", Double.class, ParameterMode.IN);
        procedure.registerStoredProcedureParameter("preco_ajustado", BigDecimal.class, ParameterMode.OUT);

        procedure.setParameter("produto_id", 1);
        procedure.setParameter("percentual_ajuste", 0.1);
        BigDecimal precoAjustado = (BigDecimal) procedure.getOutputParameterValue("preco_ajustado");

        Assertions.assertEquals(new BigDecimal("878.9"),precoAjustado);
    }

    @Test
    void receberListaDaProcedure() {
        StoredProcedureQuery procedure = entityManager
                .createStoredProcedureQuery("compraram_acima_media", Cliente.class);

        procedure.registerStoredProcedureParameter("ano", Integer.class, ParameterMode.IN);
        procedure.setParameter("ano", 2025);

        List<Cliente> lista = procedure.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void usarParametrosInEOut() {
        StoredProcedureQuery procedure = entityManager.createStoredProcedureQuery("buscar_nome_produto");

        procedure.registerStoredProcedureParameter("produto_id", Integer.class, ParameterMode.IN);
        procedure.registerStoredProcedureParameter("produto_nome", String.class, ParameterMode.OUT);

        procedure.setParameter("produto_id", 1);
        String nome = (String) procedure.getOutputParameterValue("produto_nome");

        Assertions.assertEquals("Kindle", nome);
    }
}
