package com.algaworks.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ItemPedidoDTO {

    private BigDecimal preco;
    private Integer quantidade;
    private ProdutoDTO produto;

    public ItemPedidoDTO(BigDecimal preco, Integer quantidade, Integer id, String nome) {
        this.preco = preco;
        this.quantidade = quantidade;
        this.produto = new ProdutoDTO(id, nome);
    }
}
