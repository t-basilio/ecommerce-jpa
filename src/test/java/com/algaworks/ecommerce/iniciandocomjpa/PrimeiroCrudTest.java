package com.algaworks.ecommerce.iniciandocomjpa;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.SexoCliente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PrimeiroCrudTest extends EntityManagerTest {

    @Test
    public void inserirRegistro() {
        Cliente cliente = new Cliente();
        //cliente.setId(3); utilizando IDENTITY na estrategia
        cliente.setCpf("456123");
        cliente.setSexo(SexoCliente.MASCULINO);
        cliente.setNome("José Lucas");
        entityManager.getTransaction().begin();
        entityManager.persist(cliente);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Cliente clienteVerificacao = entityManager.find(Cliente.class, cliente.getId());
        Assertions.assertNotNull(clienteVerificacao);
    }

    @Test
    public void atualizarRegistro() {
        Cliente cliente = entityManager.find(Cliente.class, 1);

        entityManager.getTransaction().begin();
        cliente.setNome("Matheus Fernandes");
        entityManager.getTransaction().commit();

        entityManager.clear();

        Cliente clienteVerificacao = entityManager.find(Cliente.class, cliente.getId());
        Assertions.assertEquals("Matheus Fernandes", clienteVerificacao.getNome());
    }

    @Test
    public void removerRegistro(){
        Cliente cliente = entityManager.find(Cliente.class, 2);

        entityManager.getTransaction().begin();
        entityManager.remove(cliente);
        entityManager.getTransaction().commit();

        Cliente clienteVerificacao = entityManager.find(Cliente.class, 2);
        Assertions.assertNull(clienteVerificacao);
    }

    @Test
    public void buscarRegistro() {
        Cliente cliente = entityManager.find(Cliente.class, 3);
        Assertions.assertEquals("Arthur Basilio", cliente.getNome());
    }
}
