package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.dto.ProdutoDTO;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.Pedido;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class BasicoJPQLTest extends EntityManagerTest {

    @Test
    void usarDistinct() {
        String jpql = "select distinct p from Pedido p" +
                " join p.itens i join i.produto pro" +
                " where pro.id in (1, 2, 3, 4, 5)";
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);

        List<Pedido> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        System.out.println(lista.size());
    }

    @Test
    void ordenarResultados() {
        String jpql = "select c from Cliente c order by c.nome asc"; //desc
        TypedQuery<Cliente> typedQuery = entityManager.createQuery(jpql, Cliente.class);
        List<Cliente> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(c -> System.out.println(c.getId() + " - " + c.getNome()));
    }

    @Test
    void buscarPorIdentificador() {
        //entityManager.find(Pedido.class, 1);

        TypedQuery<Pedido> typedQuery = entityManager
                .createQuery("select p from Pedido p where p.id = 1", Pedido.class);

        Pedido pedido = typedQuery.getSingleResult();
        Assertions.assertNotNull(pedido);

        //List<Pedido> lista = typedQuery.getResultList();
        //Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void mostrarDiferencaQueries() {
        String jpql = "select p from Pedido p where p.id = 1";

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        Pedido pedido1 = typedQuery.getSingleResult();
        Assertions.assertNotNull(pedido1);

        Query query = entityManager.createQuery(jpql);
        Pedido pedido2 = (Pedido) query.getSingleResult();
        Assertions.assertNotNull(pedido2);

        //List<Pedido> lista = query.getResultList();
        //Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    void selecionarUmAtributiParaRetorno() {
        String jpql = "select p.nome from Produto p";

        TypedQuery<String> typedQuery = entityManager.createQuery(jpql, String.class);
        List<String> lista = typedQuery.getResultList();
        Assertions.assertEquals(String.class, lista.get(0).getClass());

        String jpqlCliente = "select p.cliente from Pedido p";
        TypedQuery<Cliente> typedQueryCliente = entityManager.createQuery(jpqlCliente, Cliente.class);
        List<Cliente> listaCliente = typedQueryCliente.getResultList();
        Assertions.assertEquals(Cliente.class, listaCliente.get(0).getClass());

    }

    @Test
    void projetarOResultado() {
        String jpql = "select id, nome from Produto";
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertEquals(2, lista.get(0).length);

        Map<Integer, String> idNomeMap = new HashMap<>();

        lista.forEach(result -> idNomeMap.put((Integer)result[0], (String)result[1]));
        idNomeMap.forEach((key, value) -> System.out.printf("Id: %s, Nome: %s%n", key, value));
    }

    @Test
    void projetarNoDTO() {
        String jpql = "select new com.algaworks.ecommerce.dto.ProdutoDTO(id, nome) from Produto";
        TypedQuery<ProdutoDTO> typedQuery = entityManager.createQuery(jpql, ProdutoDTO.class);
        List<ProdutoDTO> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println(p.getId() + ", " + p.getNome()));
    }

}
