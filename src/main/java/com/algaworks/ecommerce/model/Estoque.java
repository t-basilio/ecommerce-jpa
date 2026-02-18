package com.algaworks.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "estoque")
public class Estoque extends EntidadeBaseInteger{

    @NotNull
    @OneToOne(optional = false)
    @JoinColumn(name = "produto_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_estoque_produto"))
    private Produto produto;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Integer quantidade;
}
