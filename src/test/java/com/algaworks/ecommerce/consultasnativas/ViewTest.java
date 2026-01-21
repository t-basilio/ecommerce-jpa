package com.algaworks.ecommerce.consultasnativas;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;

import java.util.List;

class ViewTest extends EntityManagerTest {

    @Test
    void executarView() {
        Query query = entityManager.createNativeQuery(
                "select cli.id, cli.nome, sum(ped.total) from pedido ped" +
                        " join view_clientes_acima_media cli on cli.id = ped.cliente_id" +
                        " group by ped.cliente_id");

        List<Object[]> lista = query.getResultList();
        lista.forEach(arr -> System.out.printf("Cliente => ID: %s, Nome: %s, Total: %.2f", arr));
    }

    @Test
    void executarViewRetornandoCliente() {
        Query query = entityManager
                .createNativeQuery("select * from view_clientes_acima_media", Cliente.class);

        List<Cliente> lista = query.getResultList();
        lista.forEach(c -> System.out.printf("Cliente => ID: %s, Nome: %s", c.getId(), c.getNome()));
    }
}
