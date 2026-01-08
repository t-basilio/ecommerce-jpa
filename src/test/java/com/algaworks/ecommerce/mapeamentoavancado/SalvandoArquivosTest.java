package com.algaworks.ecommerce.mapeamentoavancado;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.NotaFiscal;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class SalvandoArquivosTest extends EntityManagerTest {

    @Test
    public void salvarXmlNota() {
        Pedido pedido = entityManager.find(Pedido.class, 1);

        NotaFiscal notaFiscal = new NotaFiscal();
        notaFiscal.setPedido(pedido);
        notaFiscal.setDataEmissao(new Date());
        notaFiscal.setXml(carregarArquivo("/nota-fiscal.xml"));

        entityManager.getTransaction().begin();
        entityManager.persist(notaFiscal);
        entityManager.getTransaction().commit();

        entityManager.clear();

        NotaFiscal notaFiscalVerificacao = entityManager.find(NotaFiscal.class, notaFiscal.getId());
        Assertions.assertNotNull(notaFiscalVerificacao.getXml());
        Assertions.assertTrue(notaFiscalVerificacao.getXml().length > 0);

        /* try {
            OutputStream out = new FileOutputStream(
                    Files.createFile(Paths.get(System.getProperty("user.home") + "/xml.xml")).toFile());
            out.write(notaFiscalVerificacao.getXml());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } */
    }

    @Test
    public void salvarFotoProduto() {
        entityManager.getTransaction().begin();

        Produto produto = entityManager.find(Produto.class, 3);
        produto.setFoto(carregarArquivo("/go-pro-hero.jpg"));

        entityManager.getTransaction().commit();

        entityManager.clear();

        Produto produtoVerificacao = entityManager.find(Produto.class, produto.getId());
        Assertions.assertNotNull(produtoVerificacao.getFoto());
        Assertions.assertTrue(produto.getFoto().length > 0);

        /* try {
            OutputStream out = new FileOutputStream(
                    Files.createFile(Paths.get(System.getProperty("user.home") + "/xml.xml")).toFile());
            out.write(notaFiscalVerificacao.getXml());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } */
    }

    private byte [] carregarArquivo(String path) {
        try {
            return SalvandoArquivosTest.class.getResourceAsStream(path).readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
