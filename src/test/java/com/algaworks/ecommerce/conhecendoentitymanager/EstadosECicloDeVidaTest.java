package com.algaworks.ecommerce.conhecendoentitymanager;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Categoria;
import org.junit.jupiter.api.Test;

public class EstadosECicloDeVidaTest extends EntityManagerTest {

    @Test
    public void analisarEstados() {
        Categoria categoriaNovo = new Categoria(); //estado Transient, não gerenciado
        categoriaNovo.setNome("Eletrônicos");
        Categoria categoriaGerenciadaMerge = entityManager.merge(categoriaNovo);

        Categoria categoriaGerenciada = entityManager.find(Categoria.class, 1);

        entityManager.remove(categoriaGerenciada); //estado Removed, após commit remove do BD
        entityManager.persist(categoriaGerenciada); //estado Managed, após commit salva no BD

        entityManager.detach(categoriaGerenciada); //estado Detached, desanexo do EntityManager
    }
}
