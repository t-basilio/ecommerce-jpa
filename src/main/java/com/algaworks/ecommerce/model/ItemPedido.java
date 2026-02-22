package com.algaworks.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SqlResultSetMapping(name = "item_pedido-produto.ItemPedido-Produto",
        entities = { @EntityResult(entityClass = ItemPedido.class), @EntityResult(entityClass = Produto.class)})
@Entity
@Table(name = "item_pedido")
public class ItemPedido {

    @Version
    private Integer versao;

    @EmbeddedId
    private ItemPedidoId id;

    @NotNull
    @MapsId("pedidoId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_item_pedido_pedido"))
    private Pedido pedido;

    @NotNull
    @MapsId("produtoId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_item_pedido_produto"))
    private Produto produto;

    @NotNull
    @Positive
    @Column(name = "preco_produto", nullable = false)
    private BigDecimal precoProduto;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer quantidade;
}
