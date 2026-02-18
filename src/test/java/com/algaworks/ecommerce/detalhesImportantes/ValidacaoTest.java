package com.algaworks.ecommerce.detalhesImportantes;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;
import org.junit.jupiter.api.Test;

class ValidacaoTest extends EntityManagerTest {

    @Test
    void validarCliente() {
        entityManager.getTransaction().begin();

        Cliente cliente = new Cliente();
        entityManager.merge(cliente);

        entityManager.getTransaction().commit();
    }
}
