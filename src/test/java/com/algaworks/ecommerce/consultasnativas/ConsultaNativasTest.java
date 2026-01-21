package com.algaworks.ecommerce.consultasnativas;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.dto.CategoriaDTO;
import com.algaworks.ecommerce.dto.ProdutoDTO;
import com.algaworks.ecommerce.model.Categoria;
import com.algaworks.ecommerce.model.ItemPedido;
import com.algaworks.ecommerce.model.Produto;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;

import java.util.List;

class ConsultaNativasTest extends EntityManagerTest {

    @Test
    void mapearConsultaParaDTOEmArquivoXMLExercicio() {

        Query query = entityManager.createNamedQuery("ecm_categoria.listar.dto");

        List<CategoriaDTO> lista = query.getResultList();

        lista.forEach(c -> System.out
                .printf("CategoriaDTO => ID: %s, Nome: %s%n", c.getId(), c.getNome()));
    }

    @Test
    void usarNamedNativeQueryArquivoXML() {

        Query query = entityManager.createNamedQuery("ecm_categoria.listar");

        List<Categoria> lista = query.getResultList();

        lista.forEach(c -> System.out
                .printf("Categoria => ID: %s, Nome: %s%n", c.getId(), c.getNome()));
    }

    @Test
    void usarUmaNamedNativeQuery02() {

        Query query = entityManager.createNamedQuery("ecm_produto.listar");

        List<Produto> lista = query.getResultList();

        lista.forEach(p -> System.out
                .printf("ProdutoDTO => ID: %s, Nome: %s%n", p.getId(), p.getNome()));
    }

    @Test
    void usarUmaNamedNativeQuery01() {

        Query query = entityManager.createNamedQuery("produto_loja.listar");

        List<Produto> lista = query.getResultList();

        lista.forEach(p -> System.out
                .printf("ProdutoDTO => ID: %s, Nome: %s%n", p.getId(), p.getNome()));
    }

    @Test
    void usarColumnResultRetornarDTO() {

        String sql = "select * from ecm_produto";

        Query query = entityManager.createNativeQuery(sql, "ecm_produto.ProdutoDTO");

        List<ProdutoDTO> lista = query.getResultList();

        lista.forEach(p -> System.out
                .printf("ProdutoDTO => ID: %s, Nome: %s%n", p.getId(), p.getNome()));
    }

    @Test
    void usarFieldResult() {

        String sql = "select * from ecm_produto";

        Query query = entityManager.createNativeQuery(sql, "ecm_produto.Produto");

        List<Produto> lista = query.getResultList();

        lista.forEach(p -> System.out
                .printf("Produto => ID: %s, Nome: %s%n", p.getId(), p.getNome()));
    }

    @Test
    void usarSQLResultSetMapping02() {

        String sql = "select ip.*, p.* from item_pedido ip join produto p on p.id = ip.produto_id";

        Query query = entityManager.createNativeQuery(sql, "item_pedido-produto.ItemPedido-Produto");

        List<Object[]> lista = query.getResultList();

        lista.forEach(arr -> System.out.printf(
                "Pedido => ID: %s --- Produto => ID: %s, Nome: %s%n", ((ItemPedido) arr[0]).getId().getPedidoId(),
                ((Produto) arr[1]).getId(), ((Produto) arr[1]).getNome()));
    }

    @Test
    void usarSQLResultSetMapping01() {

        String sql = "select id, nome, descricao, data_criacao, data_ultima_atualizacao, preco, foto" +
                " from produto_loja";

        Query query = entityManager.createNativeQuery(sql, "produto_loja.Produto");

        List<Produto> lista = query.getResultList();

        lista.forEach(p -> System.out
                .printf("Produto => ID: %s, Nome: %s%n", p.getId(), p.getNome()));
    }

    @Test
    void executarSQLPassarParametro() {

        String sql = "select prd_id id, prd_nome nome, prd_descricao descricao," +
                " prd_data_criacao data_criacao, prd_data_ultima_atualizacao data_ultima_atualizacao," +
                " prd_preco preco, prd_foto foto from ecm_produto" +
                " where prd_id = :id";

        Query query = entityManager.createNativeQuery(sql, Produto.class);
        query.setParameter("id", 201);

        List<Produto> lista = query.getResultList();

        lista.forEach(p -> System.out
                .printf("Produto => ID: %s, Nome: %s%n", p.getId(), p.getNome()));
    }

    @Test
    void executarSQLRetornandoEntidade() {
//        String sql = "select id, nome, descricao, data_criacao," +
//                "           data_ultima_atualizacao, preco, foto from produto"

//        String sql = "select prd_id id, prd_nome nome, prd_descricao descricao," +
//                " prd_data_criacao data_criacao, prd_data_ultima_atualizacao data_ultima_atualizacao," +
//                " prd_preco preco, prd_foto foto from ecm_produto"

        String sql = "select id, nome, descricao," +
                " null data_criacao, null data_ultima_atualizacao," +
                " preco, null foto from erp_produto";

        Query query = entityManager.createNativeQuery(sql, Produto.class);

        List<Produto> lista = query.getResultList();

        lista.forEach(p -> System.out
                .printf("Produto => ID: %s, Nome: %s%n", p.getId(), p.getNome()));
    }

    @Test
    void executarSQL() {
        String sql = "select id, nome from produto";
        Query query = entityManager.createNativeQuery(sql);

        List<Object[]> lista = query.getResultList();

        lista.forEach(arr -> System.out
                .printf("Produto => ID: %s, Nome: %s%n", arr[0], arr[1]));
    }

}
