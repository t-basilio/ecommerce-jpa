package com.algaworks.ecommerce.criteria;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.*;
import com.algaworks.ecommerce.model.Cliente_;
import com.algaworks.ecommerce.model.PagamentoBoleto_;
import com.algaworks.ecommerce.model.Pedido_;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class FuncoesCriteriaTest extends EntityManagerTest {

    @Test
    void aplicarFuncaoAgregacao() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.multiselect(
                criteriaBuilder.count(root.get(Pedido_.id)),
                criteriaBuilder.avg(root.get(Pedido_.total)),
                criteriaBuilder.sum(root.get(Pedido_.total)),
                criteriaBuilder.min(root.get(Pedido_.total)),
                criteriaBuilder.max(root.get(Pedido_.total))
        );

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println("count: " + arr[0] + ", svg: " + arr[1] +
                ", sum: " + arr[2] + ", min: " + arr[3] + ", max: " + arr[4]));
    }

    @Test
    void aplicarFuncaoNativa() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.multiselect(
                root.get(Pedido_.id),
                criteriaBuilder.function("dayname", String.class, root.get(Pedido_.dataCriacao))
        );

        criteriaQuery.where(criteriaBuilder.isTrue(criteriaBuilder.function(
                "acima_media_faturamento", Boolean.class, root.get(Pedido_.total)))
        );

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(
                arr[0] + ", dayname: " + arr[1]));
    }

    @Test
    void aplicarFuncaoColecao() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.multiselect(
                root.get(Pedido_.id),
                criteriaBuilder.size(root.get(Pedido_.itens))
        );

        criteriaQuery.where(criteriaBuilder.greaterThan( criteriaBuilder.size(root.get(Pedido_.itens)), 1));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + ", size: " + arr[1]));
    }

    @Test
    void aplicarFuncaoComNumeros() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.multiselect(
                root.get(Pedido_.id),
                criteriaBuilder.abs(criteriaBuilder.prod(root.get(Pedido_.id), -1)),
                criteriaBuilder.mod(root.get(Pedido_.id), 2),
                criteriaBuilder.sqrt(root.get(Pedido_.total))
        );

        criteriaQuery.where(criteriaBuilder.greaterThan(
                criteriaBuilder.sqrt(root.get(Pedido_.total)), 10.0)
        );
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(
                arr[0]
                        + ", abs: " + arr[1]
                        + ", mod: " + arr[2]
                        + ", sqrt: " + arr[3]));
    }

   @Test
   void aplicarFuncaoComDatas() {
       CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
       CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
       Root<Pedido> root = criteriaQuery.from(Pedido.class);
       Join<Pedido, Pagamento> joinPagamento = root.join(Pedido_.pagamento);
       Join<Pedido, PagamentoBoleto> joinPagamentoBoleto =
               criteriaBuilder.treat(joinPagamento, PagamentoBoleto.class);

       criteriaQuery.multiselect(
               root.get(Pedido_.id),
               criteriaBuilder.currentDate(),
               criteriaBuilder.currentTime(),
               criteriaBuilder.currentTimestamp()
       );

       criteriaQuery.where(
               criteriaBuilder.between(criteriaBuilder.currentDate(),
                       root.get(Pedido_.dataCriacao).as(java.sql.Date.class),
                       joinPagamentoBoleto.get(PagamentoBoleto_.dataVencimento).as(java.sql.Date.class)),
               criteriaBuilder.equal(root.get(Pedido_.status), StatusPedido.AGUARDANDO)
       );


       TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);

       List<Object[]> lista = typedQuery.getResultList();
       Assertions.assertFalse(lista.isEmpty());

       lista.forEach(arr -> System.out.println(
               arr[0]
                       + " - current_date: " + arr[1]
                       + " - current_time: " + arr[2]
                       + " - current_timestamp: " + arr[3]));
   }

    @Test
    void aplicarFuncaoString() {
        // concat('Categoria: ', c.nome)
        // length(c.nome)
        // locate('a', c.nome)
        // substring(c.nome, 1, 2)
        // lower(c.nome)
        // upper(c.nome)
        // trim(c.nome)
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
        Root<Cliente> root = criteriaQuery.from(Cliente.class);

        criteriaQuery.multiselect(
                root.get(Cliente_.nome),
                criteriaBuilder.concat("Cliente: ", root.get(Cliente_.nome))
        );

        criteriaQuery.where(criteriaBuilder.greaterThan(
                criteriaBuilder.length(root.get(Cliente_.nome)), 15
        ));

        TypedQuery<Tuple> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Tuple> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(t -> System.out.println(t.get(0) + " - " + t.get(1)));
    }
}
