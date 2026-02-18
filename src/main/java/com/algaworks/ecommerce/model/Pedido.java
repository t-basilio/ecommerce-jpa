package com.algaworks.ecommerce.model;

import com.algaworks.ecommerce.listener.GenericoListener;
import com.algaworks.ecommerce.listener.GerarNotaFiscalListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NamedEntityGraphs({
        @NamedEntityGraph(name = "Pedido.dadosEssencias", attributeNodes = {
            @NamedAttributeNode("dataCriacao"),
            @NamedAttributeNode("status"),
            @NamedAttributeNode("total"),
            @NamedAttributeNode(value = "cliente", subgraph = "cli"),
        }, subgraphs = {
                @NamedSubgraph(name = "cli", attributeNodes = {
                    @NamedAttributeNode("nome"),
                    @NamedAttributeNode("cpf")
                })
        })
})
@EntityListeners({ GerarNotaFiscalListener.class, GenericoListener.class })
@Entity
@Table(name = "pedido")
public class Pedido extends EntidadeBaseInteger {

    @NotNull
    @PastOrPresent
    @Column(name = "data_criacao", updatable = false, nullable = false)
    private LocalDateTime dataCriacao;

    @PastOrPresent
    @Column(name = "data_ultima_atualizacao", insertable = false)
    private LocalDateTime dataUltimaAtualizacao;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_pedido_cliente"))
    private Cliente cliente;

    @PastOrPresent
    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @OneToOne(mappedBy = "pedido")
    private NotaFiscal notaFiscal;

    @NotNull
    @Positive
    @Column(nullable = false)
    private BigDecimal total;

    @NotNull
    @Column(length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    @NotEmpty
    @OneToMany(mappedBy = "pedido")
    private List<ItemPedido> itens;

    @OneToOne(mappedBy = "pedido")
    private Pagamento pagamento;

    @Embedded
    private EnderecoEntregaPedido enderecoEntrega;

    public boolean isPago(){
        return StatusPedido.PAGO.equals(status);
    }

    @PrePersist
    public void aoPersistir() {
        dataCriacao = LocalDateTime.now();
        calcularTotal();
    }

    @PreUpdate
    public void aoAtualizar() {
        dataUltimaAtualizacao = LocalDateTime.now();
        calcularTotal();
    }

    @PostPersist
    public void aposPersistir() {
        System.out.println("Após persistir Pedido.");
    }

    @PostUpdate
    public void aposAtualizar() {
        System.out.println("Após atualizar Pedido.");
    }

    @PreRemove
    public void aoRemover() {
        System.out.println("Antes de remover Pedido.");
    }

    @PostRemove
    public void aposRemover() {
        System.out.println("Após remover Pedido.");
    }

    @PostLoad
    public void aoCarregar() {
        System.out.println("Após carregar o Pedido.");
    }

//    @PrePersist
//    @PreUpdate
    private void calcularTotal() {
        if(itens != null) {
            total = itens.stream()
                    .map(i -> new BigDecimal(i.getQuantidade()).multiply(i.getPrecoProduto()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            total = BigDecimal.ZERO;
        }
    }
}
