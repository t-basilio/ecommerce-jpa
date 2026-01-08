package com.algaworks.ecommerce.conhecendoentitymanager;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.StatusPedido;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FlushTest extends EntityManagerTest {

    @Test
    public void abrirFecharCancelarTransacao() {
        Assertions.assertThrows(Exception.class, () -> {

            try {
                entityManager.getTransaction().begin();

                Pedido pedido = entityManager.find(Pedido.class, 1);
                pedido.setStatus(StatusPedido.PAGO);

                entityManager.flush();

                if(pedido.getPagamento() == null) {
                    throw new RuntimeException("Pedido ainda não foi pago.");
                }
            /*  Uma consulta obriga o JPA a sincronizar o que ele tem na memória
                Pedido pedidoPago = entityManager
                        .createQuery("select p from Pedido p where p.id = 1", Pedido.class)
                        .getSingleResult();
                Assertions.assertEquals(pedidoPago.getStatus(), pedidoPago.getStatus());
            */
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                entityManager.getTransaction().rollback();
                throw e;
            }
        });

    }
}
