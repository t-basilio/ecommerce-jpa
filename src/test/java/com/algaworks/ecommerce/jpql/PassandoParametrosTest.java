package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.dto.ProdutoDTO;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.NotaFiscal;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.StatusPagamento;
import jakarta.persistence.Query;
import jakarta.persistence.TemporalType;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PassandoParametrosTest extends EntityManagerTest {

    @Test
    void passarParametro() {
        String jpql = "select p from Pedido p join p.pagamento pag " +
                "where p.id = :pedidoId and pag.status = ?1";

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        typedQuery.setParameter("pedidoId", 3);
        typedQuery.setParameter(1, StatusPagamento.RECEBIDO);


        List<Pedido> lista = typedQuery.getResultList();
        lista.forEach(p -> System.out.println(p.getStatus()));

        Assertions.assertEquals(1, lista.size());
    }

    @Test
    void passarParametroDate() {
        String jpql = "select nf from NotaFiscal nf where nf.dataEmissao <= ?1";

        TypedQuery<NotaFiscal> typedQuery = entityManager.createQuery(jpql, NotaFiscal.class);
        typedQuery.setParameter(1, new Date(), TemporalType.TIMESTAMP);

        List<NotaFiscal> lista = typedQuery.getResultList();
        lista.forEach(nf -> System.out.println(nf.getDataEmissao()));

        Assertions.assertEquals(1, lista.size());
    }
}
